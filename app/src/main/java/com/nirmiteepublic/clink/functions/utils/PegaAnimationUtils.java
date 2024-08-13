package com.nirmiteepublic.clink.functions.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.nirmiteepublic.clink.functions.listeners.PegaAnimationUtilsCallBack;

import java.util.List;

/**
 * Created by Sahil The Geek
 * Date : 13-05-2022.
 *
 * <p>This {@link PegaAnimationUtils} helps a view to animate</p>
 */
public class PegaAnimationUtils {
    private static int endCount = 1;

    /**
     * This method will shake the view for once
     */
    public static void shake(View v) {
        if (v != null) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(v, "translationX", 0, 20, 0, -20, 0);
            animator.setDuration(500);

            // Add an AnimatorListener to reset the translationX property when the animation ends
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    v.setTranslationX(0);
                }
            });

            animator.start();
        }
    }

    /**
     * Change the status bar color with animation
     */
    public static void changeColor(Window window, ValueAnimator valueAnimator) {
        valueAnimator.setDuration(150);
        valueAnimator.addUpdateListener(animation -> window.setStatusBarColor((int) animation.getAnimatedValue()));
        valueAnimator.start();
    }


    public static void showToast(Activity context, String msg) {
        if (!context.isFinishing()) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static void invalidData(Context context, String msg, View view) {
        PegaAnimationUtils.shake(view);
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

    }

    public static void expandText(TextView textView, int targetWidth, int targetHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(0, targetWidth, targetHeight);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(200);

        animator.addUpdateListener(valueAnimator -> {
            int value = (int) valueAnimator.getAnimatedValue();

            textView.getLayoutParams().width = value;
            textView.getLayoutParams().height = value;
            textView.requestLayout();
        });
    }

    /**
     * Animate All Views Of Parent Container when activity starts
     * Animate Like View is fading in among with translating in Y axis
     */
    public static void fadeUp(ViewGroup container, List<Integer> exclude, Activity activity, PegaAnimationUtilsCallBack listener) {
        if (listener != null) {
            listener.onStart();
        }
        Thread thread = new Thread(() -> {
            int childCount = container.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = container.getChildAt(i);
                if (!exclude.contains(view.getId())) {
                    view.setTranslationY(100f);
                    view.setAlpha(0f);
                }
            }
            if (listener != null) {
                activity.runOnUiThread(listener::onMid);
            }
            for (int i = 0; i < childCount; i++) {
                View view = container.getChildAt(i);
                if (!exclude.contains(view.getId())) {
                    try {
                        Thread.sleep(50);
                        activity.runOnUiThread(() -> view.animate().translationY(0f).alpha(1f).setDuration(500).start());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (listener != null) {
                activity.runOnUiThread(listener::onEnd);
            }
        });
        thread.start();
    }

    public static void fadeDown(ViewGroup container, List<Integer> exclude, Activity activity, PegaAnimationUtilsCallBack listener) {
        endCount = 1;
        if (listener != null) {
            listener.onStart();
        }
        Thread thread = new Thread(() -> {
            int childCount = container.getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                View view = container.getChildAt(i);
                if (!exclude.contains(view.getId())) {
                    try {
                        Thread.sleep(50);
                        activity.runOnUiThread(() -> view.animate().translationY(100f).alpha(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                endCount++;
                                if (endCount == childCount && listener != null) {
                                    listener.onEnd();
                                }
                            }
                        }).start());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (listener != null) {
                activity.runOnUiThread(listener::onEnd);
            }

        });
        thread.start();
    }


    /**
     * This function Slides the view from position 0 to to Left till its width
     */
    public static void slideViewToLeft(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "x", 0f, -view.getWidth());
        animator.setDuration(250); // Set the animation duration in milliseconds
        animator.start();
    }

    /**
     * This function Slides the view from position 0 to to right till its width
     */
    public static void slideViewToRight(View view, int duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "x", 0f, view.getWidth());
        animator.setDuration(duration); // Set the animation duration in milliseconds
        animator.start();
    }

    /**
     * This function Slides the view from position right side of its width to 0
     */
    public static void slideViewFromOutTo0f(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "x", view.getWidth(), 0f);
        animator.setDuration(250); // Set the animation duration in milliseconds
        animator.start();
    }

    /**
     * This function Slides the view from position left side of its width to 0
     */
    public static void slideViewFrom0fToOut(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "x", -view.getWidth(), 0f);
        animator.setDuration(250); // Set the animation duration in milliseconds
        animator.start();
    }


}
