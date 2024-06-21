package com.nirmiteepublic.clink.adapters.viewholders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.util.Log;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.adapters.ChipBtnAdapter;
import com.nirmiteepublic.clink.databinding.ItemChipBtnBinding;
import com.nirmiteepublic.clink.functions.listeners.ChipBtnListener;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.ui.activity.pages.GraphicsFilterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemChipBtnViewHolder extends RecyclerView.ViewHolder {

    private final Context context;
    private final String chipTitle;
    ItemChipBtnBinding binding;

    public ItemChipBtnViewHolder(ItemChipBtnBinding binding, Context context, String chipTitle) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
        this.chipTitle = chipTitle;
    }

    public void bind(ChipBtnAdapter adapter, int position) {
//        if (position == 0) {
//            select();
//        }

        ChipBtnListener chipBtnListener = this::unselect;
        adapter.chipBtnListenerList.add(position, chipBtnListener);

        binding.title.setText(chipTitle);
        itemView.setOnClickListener(v -> {
//            if (adapter.selected == position) {
//                return;
//            }
            adapter.chipBtnListenerList.get(adapter.selected).onUnselect();
            adapter.selected = position;
            select();
        });

if(UserUtils.isSuperAdmin()){
    itemView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            // Create an AlertDialog with options
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Options");
            builder.setItems(new CharSequence[]{"Add Chip Button", "Update Chip Button", "Delete Chip Button"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            addChipButtonDialog();
                            break;
                        case 1:
                            updateChipButtonDialog();
                            break;
                        case 2:
                            deleteChipButton();
                            break;
                    }
                }
            });
            builder.show();
            return true;
        }
    });

}
    }

    private void select() {
        startGraphicsFilterActivity();
        itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.round_cyan_bg));
        binding.title.setTextColor(ContextCompat.getColor(context, R.color.cyan_process));

    }

    private void unselect() {
        itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.round_dim_bg));
        binding.title.setTextColor(ContextCompat.getColor(context, R.color.charcoal));
    }

    private void startGraphicsFilterActivity() {
        Intent intent = new Intent(context, GraphicsFilterActivity.class);
        intent.putExtra("chip", chipTitle);
        context.startActivity(intent);
    }


    private void addChipButtonDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Chip Button");
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_chip_button, null);
        EditText editText = dialogView.findViewById(R.id.editMeetName);
        Button addButton = dialogView.findViewById(R.id.btnUploadImage);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buttonText = editText.getText().toString().trim();
                if (!buttonText.isEmpty()) {
                    JSONObject requestBodyJson = new JSONObject();
                    try {
                        requestBodyJson.put("chipButtonList", buttonText);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyJson.toString());

                    RetrofitClient.getInstance(context).getApiInterfaces().AddChipBtns(
                            requestBody
                    ).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code()==200) {
                                Toast.makeText(context, "Added Chip Button: " + buttonText, Toast.LENGTH_SHORT).show();
                            } else {
                                // Check if response code indicates that the button text is not unique
                                try {
                                    Toast.makeText(context, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                                    Log.d("ResponseBody", response.errorBody().string());
                                } catch (IOException e) {
                                    Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(context, "Failed To Add Chip Button", Toast.LENGTH_SHORT).show();

                        }
                    });
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(context, "Please enter text", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.show();
    }

    private void updateChipButtonDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Chip Button");
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_chip_button, null);
        EditText editText = dialogView.findViewById(R.id.editMeetName);
        Button updateButton = dialogView.findViewById(R.id.btnUploadImage);
        updateButton.setText("Update");
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buttonText = editText.getText().toString().trim();
                if (!buttonText.isEmpty()) {
                    JSONObject requestBodyJson = new JSONObject();
                    try {
                        requestBodyJson.put("chipButtonList", buttonText);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyJson.toString());

                    RetrofitClient.getInstance(context).getApiInterfaces().EditChipBtns(
                            chipTitle,
                            requestBody
                    ).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code()==200) {
                                Toast.makeText(context, "Updated Chip Button: " + buttonText, Toast.LENGTH_SHORT).show();
                            } else {
                                // Check if response code indicates that the button text is not unique
                                try {
                                    Toast.makeText(context, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                                    Log.d("ResponseBody", response.errorBody().string());
                                } catch (IOException e) {
                                    Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(context, "Failed To Add Chip Button", Toast.LENGTH_SHORT).show();

                        }
                    });
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(context, "Please enter text", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.show();
    }

    private void deleteChipButton() {

        RetrofitClient.getInstance(context).getApiInterfaces().DeleteChipBtns(chipTitle).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    Toast.makeText(context, "Chip Deleted Successfully", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
