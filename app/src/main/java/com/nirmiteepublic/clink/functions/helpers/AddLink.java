//package com.nirmiteepublic.clink.functions.helpers;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.widget.Toast;
//
//import com.bmlntech.mycareersaathi.sarthi.databinding.AddLinkLayoutBinding;
//import com.bmlntech.mycareersaathi.sarthi.functions.listeners.AddLinkCallBackListener;
//import com.nirmiteepublic.clink.functions.listeners.AddLinkCallBackListener;
//
//import java.util.Objects;
//
//public class AddLink {
//    Activity context;
//    Dialog dialog;
//
//    AddLinkLayoutBinding binding;
//
//    Prefs prefs;
//
//    public AddLink(Activity context) {
//        this.context = context;
//    }
//
//    public void show(AddLinkCallBackListener listener) {
//        dialog = new Dialog(context);
//        binding = AddLinkLayoutBinding.inflate(context.getLayoutInflater());
//        dialog.setContentView(binding.getRoot());
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        prefs = new Prefs(context);
//
//
//        binding.confirm.setOnClickListener(v -> {
//            String gameName = Objects.requireNonNull(binding.link.getText()).toString().trim();
//            if (gameName.isEmpty()) {
//                Toast.makeText(context, "Link cannot be empty", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            if (!gameName.startsWith("http://") && !gameName.startsWith("https://")) {
//                Toast.makeText(context, "Invalid URL Provided", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            dialog.dismiss();
//            listener.onLinkAdd(gameName);
//
//        });
//        dialog.create();
//        dialog.show();
//    }
//
//
//    public void dismiss() {
//        if (dialog != null) {
//            dialog.dismiss();
//        }
//    }
//}
