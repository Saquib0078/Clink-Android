package com.nirmiteepublic.clink.functions.viewmanagers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.functions.listeners.OTPCompleteCallBack;

/**
 * Created by Sahil The Geek
 * Date : 10-01-2022.
 *
 * <p>This {@link PegaOTPViewCreator} Help to create dynamic OTP view</p>
 */
public class PegaOTPViewCreator {

    private final LinearLayout otpContainer, keyboardContainer;
    private final Context context;
    int selected = 0;
    int key = 1;
    private OTPCompleteCallBack otpCompleteCallBack;

    public PegaOTPViewCreator(LinearLayout otpContainer, LinearLayout keyboardContainer, Context context) {
        this.otpContainer = otpContainer;
        this.keyboardContainer = keyboardContainer;
        this.context = context;
    }

    public void setOnCompleteListener(OTPCompleteCallBack otpCompleteCallBack) {
        this.otpCompleteCallBack = otpCompleteCallBack;
    }

    public void createOTPView(int count) {

        for (int i = 0; i < count; i++) {
            otpContainer.addView(createOTP(i));
        }
    }

    private View createOTP(int i) {
        TextView textView = new TextView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) context.getResources().getDimension(com.intuit.sdp.R.dimen._30sdp), (int) context.getResources().getDimension(com.intuit.sdp.R.dimen._35sdp));
        if (i != 0) {
            layoutParams.setMarginStart(((int) context.getResources().getDimension(com.intuit.sdp.R.dimen._7sdp)));
            textView.setBackgroundResource(R.drawable.otp_unselected);
        } else {
            textView.setBackgroundResource(R.drawable.otp_selected);
        }
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(ContextCompat.getColor(context, R.color.charcoal));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spToPx((int) context.getResources().getDimension(com.intuit.ssp.R.dimen._13ssp)));
        textView.setTypeface(null, Typeface.BOLD);
        return textView;
    }

    public void createKeyBoard() {
        for (int i = 0; i < 4; i++) {
            LinearLayout linearLayout = new LinearLayout(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.bottomMargin = (int) context.getResources().getDimension(com.intuit.sdp.R.dimen._5sdp);
            linearLayout.setLayoutParams(layoutParams);
            for (int i2 = 0; i2 < 3; i2++) {
                linearLayout.addView(createKey(i2));
                key++;
            }
            keyboardContainer.addView(linearLayout);
        }
    }

    @SuppressLint("SetTextI18n")
    private View createKey(int i) {
        RelativeLayout relativeLayout = new RelativeLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, (int) context.getResources().getDimension(com.intuit.sdp.R.dimen._40sdp));
        if (i == 0) {
            layoutParams.setMarginStart((int) context.getResources().getDimension(com.intuit.sdp.R.dimen._8sdp));
        } else if (i == 1) {
            layoutParams.setMarginStart((int) context.getResources().getDimension(com.intuit.sdp.R.dimen._5sdp));
            layoutParams.setMarginEnd((int) context.getResources().getDimension(com.intuit.sdp.R.dimen._5sdp));
        } else {
            layoutParams.setMarginEnd((int) context.getResources().getDimension(com.intuit.sdp.R.dimen._8sdp));
        }
        layoutParams.weight = 0.33f;
        relativeLayout.setLayoutParams(layoutParams);
        if (key != 10) {
            if (key == 12) {
                ImageView imageView = new ImageView(context);
                int width = (int) context.getResources().getDimension(com.intuit.sdp.R.dimen._25sdp);
                int height = (int) context.getResources().getDimension(com.intuit.sdp.R.dimen._25sdp);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
                params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                imageView.setLayoutParams(params);
                imageView.setImageResource(R.drawable.backspace);
                imageView.setColorFilter(ContextCompat.getColor(context, R.color.charcoal), PorterDuff.Mode.SRC_IN);
                relativeLayout.addView(imageView);
                relativeLayout.setTag("back");
            } else {

                TextView textView = new TextView(context);
                RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                relativeLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                textView.setLayoutParams(relativeLayoutParams);
                if (key == 11) {
                    textView.setText("0");
                    relativeLayout.setTag("0");
                } else {
                    textView.setText(String.valueOf(key));
                    relativeLayout.setTag(String.valueOf(key));
                }
                textView.setTextColor(ContextCompat.getColor(context, R.color.charcoal));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(com.intuit.ssp.R.dimen._15ssp));
                textView.setTypeface(null, Typeface.BOLD);
                textView.setGravity(Gravity.CENTER);
                relativeLayout.addView(textView);
            }
            relativeLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.key_bg));
            relativeLayout.setOnClickListener(v -> {
                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(70, VibrationEffect.DEFAULT_AMPLITUDE));
                }

                TextView textView = (TextView) otpContainer.getChildAt(selected);
                if (textView != null) {

                    textView.setBackgroundResource(R.drawable.otp_unselected);
                    if (!relativeLayout.getTag().toString().equals("back")) {
                        textView.setText(relativeLayout.getTag().toString());
                        selected++;
                        if (selected <= 4) {
                            otpContainer.getChildAt(selected).setBackgroundResource(R.drawable.otp_selected);
                        } else {
                            if (otpCompleteCallBack != null) {
                                otpCompleteCallBack.onOTPComplete(getOTP());
                            }
                        }
                    } else {
                        textView.setText("");
                        if (selected > 0) {
                            selected--;
                        }
                        otpContainer.getChildAt(selected).setBackgroundResource(R.drawable.otp_selected);
                        TextView tv = (TextView) otpContainer.getChildAt(selected);
                        tv.setText("");
                    }

                } else {
                    resetOTP();
                }

            });
        }
        return relativeLayout;
    }

    public void resetOTP() {
        for (int i = 0; i < otpContainer.getChildCount(); i++) {
            TextView textView = (TextView) otpContainer.getChildAt(i);
            if (i == 0) {
                textView.setBackgroundResource(R.drawable.otp_selected);
            }
            textView.setText("");
            selected = 0;
        }
    }

    public String getOTP() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < otpContainer.getChildCount(); i++) {
            TextView textView = (TextView) otpContainer.getChildAt(i);
            otp.append(textView.getText().toString().trim());
        }
        return otp.toString();
    }

    /**
     * Used to Convert the Sp to Px
     */
    private int spToPx(float sp) {

        return (int) (sp / context.getResources().getDisplayMetrics().density);

    }
}
