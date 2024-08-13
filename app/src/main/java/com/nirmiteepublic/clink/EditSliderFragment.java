package com.nirmiteepublic.clink;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.nirmiteepublic.clink.adapters.ImageSliderAdapter;
import com.nirmiteepublic.clink.databinding.FragmentEditSliderBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.models.SlidersItem;
import com.nirmiteepublic.clink.ui.activity.pages.CreateMeetActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditSliderFragment extends BottomSheetDialogFragment implements ImageSliderAdapter.OnItemClickListener{
    Bitmap bitmap;
    FragmentEditSliderBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditSliderBinding.inflate(inflater, container, false);




        return binding.getRoot();
    }

//    private void ConvertImage() {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        if (bitmap != null) {
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//            byte[] byteArray = byteArrayOutputStream.toByteArray();
//            String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
//
//
//            // Create RequestBody for text data
//
//            RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), Base64.decode(base64Image, Base64.DEFAULT));
//
//            // Create MultipartBody.Part for the image
//            MultipartBody.Part imageID = MultipartBody.Part.createFormData("imageID", "image.jpg", imageRequestBody);
//
//            // Assuming you have a Retrofit instance named retrofit
//            RetrofitClient.getInstance(requireContext()).getApiInterfaces().UpdateSlider(
//
//                    imageID
//            ).enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    if (response.isSuccessful()) {
//
//                        binding.progressBar.setVisibility(View.GONE);
//                        Toast.makeText(requireContext(), "Uploaded", Toast.LENGTH_SHORT).show();
//                    } else {
//                        try {
//                            String res = response.errorBody().string();
//                            System.out.println(res);
//                            binding.progressBar.setVisibility(View.GONE);
//
//                        } catch (IOException e) {
//                            binding.progressBar.setVisibility(View.GONE);
//
//                            Toast.makeText(requireContext(), "Error" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                        }
//
//                        Toast.makeText(requireContext(), "Error" + response.errorBody(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    binding.progressBar.setVisibility(View.GONE);
//
//                    Toast.makeText(requireContext(), "" + t.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                    binding.img.setImageBitmap(bitmap);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }

        }
    });

    @Override
    public void onItemClick(String id) {
        Toast.makeText(requireContext(), id, Toast.LENGTH_SHORT).show();
    }
}