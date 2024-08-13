package com.visticsolution.posterbanao.navfragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.visticsolution.posterbanao.MainActivity;
import com.visticsolution.posterbanao.R;

import com.visticsolution.posterbanao.adapter.InvitationPagerAdapter;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.classes.PermissionUtils;
import com.visticsolution.posterbanao.databinding.FragmentCreateBinding;
import com.visticsolution.posterbanao.editor.Constants;
import com.visticsolution.posterbanao.editor.EditorActivity;
import com.visticsolution.posterbanao.editor.Fragment.SizeSelection;
import com.visticsolution.posterbanao.editor.interfaces.GetSelectSize;
import com.visticsolution.posterbanao.editor.movie.UriUtil;
import com.visticsolution.posterbanao.editor.utils.TamplateUtils;
import com.visticsolution.posterbanao.model.InvitationModel;
import com.visticsolution.posterbanao.model.PostsModel;
import com.visticsolution.posterbanao.responses.HomeResponse;
import com.visticsolution.posterbanao.viewmodel.HomeViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CreateFragment extends Fragment {

    public CreateFragment() {
    }

    Activity context;
    FragmentCreateBinding binding;
    HomeViewModel homeViewModel;
    PermissionUtils takePermissionUtils;
    String ratio = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateBinding.inflate(getLayoutInflater());
        Functions.fadeIn(binding.getRoot(), getContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        takePermissionUtils = new PermissionUtils(getActivity(), mPermissionResult);
        binding.customPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (takePermissionUtils.isStorageCameraPermissionGranted()) {
                    selectSize(v);
                } else {
                    takePermissionUtils.takeStorageCameraPermission();
                }
            }
        });
        binding.customVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (takePermissionUtils.isStorageCameraPermissionGranted()) {
                    startAction(v);
                } else {
                    takePermissionUtils.takeStorageCameraPermission();
                }
            }
        });
        binding.photoToVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (takePermissionUtils.isStorageCameraPermissionGranted()) {
                    selectSize(v);
                } else {
                    takePermissionUtils.takeStorageCameraPermission();
                }
            }
        });

        binding.searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    Functions.showLoader(context);
                    callgetCards(textView.getText().toString());
                    return true;
                }
                return false;
            }
        });

        callgetCards("");
    }

    private void callgetCards(String toString) {
        binding.invitationLay.setVisibility(View.GONE);
        binding.shimmerLay.startShimmer();
        binding.shimmerLay.setVisibility(View.VISIBLE);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getInvitationCategories(toString).observe(getViewLifecycleOwner(), new Observer<HomeResponse>() {
            @Override
            public void onChanged(HomeResponse homeResponse) {
                Functions.cancelLoader();
                if (homeResponse.getInvitationcategories() != null) {
                    binding.invitationLay.setVisibility(View.VISIBLE);
                    binding.shimmerLay.stopShimmer();
                    binding.shimmerLay.setVisibility(View.GONE);
                    binding.cardPager.setAdapter(new InvitationPagerAdapter(getChildFragmentManager(), homeResponse.getInvitationcategories(), new InvitationPagerAdapter.OnCardSelect() {
                        @Override
                        public void onSelect(InvitationModel frameModel) {
                            if (frameModel.getPremium().equals("1")){
                                PostsModel postsModel = new PostsModel();
                                postsModel.setPremium("1");
                                EditorActivity.postsModel = postsModel;
                            }
                            selectedJson = frameModel.getJson();

                            Functions.showLoader(context);
                            StartTamplateProcess process = new StartTamplateProcess();
                            process.doInBackground();
                        }
                    }));
                    binding.TabLayout.setupWithViewPager(binding.cardPager);
                }
            }
        });
    }

    String selectedJson = "";
    private final class StartTamplateProcess extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Functions.showLoader(context);
        }

        @Override
        protected String doInBackground(Void... params) {
            TamplateUtils tamplateUtils = new TamplateUtils(context);
            tamplateUtils.openEditorActivity(selectedJson);
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            Functions.cancelLoader();
        }
    }


    private void selectSize(View v) {
        new SizeSelection(new GetSelectSize() {
            @Override
            public void ratioOptions(String str) {
                ratio = str;
                startAction(v);
            }

            @Override
            public void sizeOptions(String str) {
                String[] split = str.split(":");
                int parseInt = Integer.parseInt(split[0]);
                int parseInt2 = Integer.parseInt(split[1]);
                int gcd = gcd(parseInt, parseInt2);
                ratio = (parseInt / gcd) + ":" + (parseInt2 / gcd) + ":" + parseInt + ":" + parseInt2;
                startAction(v);

            }
        }).show(getChildFragmentManager(), "");
    }

    private void startAction(View v) {
        Intent intent = new Intent();
        int id = v.getId();
        if (id == R.id.photoToVideo) {
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            resultCallbackForMovie.launch(intent);
        } else if (id == R.id.custom_photo) {
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            resultCallbackForCustome.launch(intent);
        } else if (id == R.id.custom_video) {
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            resultCallbackForCustome.launch(intent);
        }
    }

    private int gcd(int i, int i2) {
        return i2 == 0 ? i : gcd(i2, i % i2);
    }

    private final ActivityResultLauncher<String[]> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onActivityResult(Map<String, Boolean> result) {
                    boolean allPermissionClear = true;
                    List<String> blockPermissionCheck = new ArrayList<>();
                    for (String key : result.keySet()) {
                        if (!(result.get(key))) {
                            allPermissionClear = false;
                            blockPermissionCheck.add(Functions.getPermissionStatus(getActivity(), key));
                        }
                    }
                    if (blockPermissionCheck.contains("blocked")) {
                        Functions.showPermissionSetting(getActivity(), getString(R.string.we_need_storage_and_camera_permission_for_upload_profile_pic));
                    } else if (allPermissionClear) {

                    }
                }
            });

    ActivityResultLauncher<Intent> resultCallbackForCustome = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (Functions.isLogin(context)) {

                            String path = UriUtil.getPath(context, result.getData().getData());
                            if (path.endsWith(".mp4")) {
                                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                                retriever.setDataSource(path);
                                int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                                int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                                try {
                                    retriever.release();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                ratio = width + ":" + height;
                            }

                            Intent intent = new Intent(context, EditorActivity.class);
                            intent.putExtra("ratio", ratio);
                            intent.putExtra("backgroundImage", path);
                            intent.putExtra("type", "post");
                            startActivity(intent);
                        } else {
                            startActivity(new Intent(context, MainActivity.class));
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> resultCallbackForMovie = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data.getClipData() != null) {
                            ClipData mClipData = data.getClipData();
                            for (int i = 0; i < mClipData.getItemCount(); i++) {
                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();
                                Constants.movieImageList.add(UriUtil.getPath(context, uri));
                            }
                            if (Constants.movieImageList.size() > 3) {
                                if (Functions.isLogin(context)) {
                                    Intent intent = new Intent(context, EditorActivity.class);
                                    intent.putExtra("ratio", ratio);
                                    intent.putExtra("backgroundImage", "https://betterstudio.com/wp-content/uploads/2019/05/1-1-instagram-1024x1024.jpg");
                                    intent.putExtra("type", "Movie");
                                    startActivity(intent);
                                } else {
                                    startActivity(new Intent(context, MainActivity.class));
                                }
                            } else {
                                Functions.showToast(context, getString(R.string.select_minimum_3_images));
                            }
                        } else {
                            Functions.showToast(context, getString(R.string.select_minimum_3_images));
                        }
                    }
                }
            });

}