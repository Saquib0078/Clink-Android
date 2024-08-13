package com.visticsolution.posterbanao.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.editor.View.StickerView;
import com.visticsolution.posterbanao.editor.View.ViewIdGenerator;
import com.visticsolution.posterbanao.editor.View.text.AutofitTextRel;
import com.visticsolution.posterbanao.editor.View.text.TextInfo;
import com.visticsolution.posterbanao.editor.adapter.FramePagerAdapter;
import com.visticsolution.posterbanao.editor.model.ElementInfo;
import com.visticsolution.posterbanao.editor.model.Sticker_info;
import com.visticsolution.posterbanao.editor.model.textInfo;
import com.visticsolution.posterbanao.editor.utility.ImageUtils;
import com.visticsolution.posterbanao.editor.utils.FrameUtils;
import com.visticsolution.posterbanao.model.FrameModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class LoadFrameFragment extends Fragment {


    Activity context;
    FrameModel model;
    FramePagerAdapter.OnFrameSelect onOverlaySelected;
    ArrayList<Sticker_info> stickerInfoArrayList = new ArrayList<>();
    ArrayList<textInfo> textInfoArrayList = new ArrayList<>();
    RelativeLayout relativeLayout;
    HashMap<Integer, Object> txtShapeList;
    float screenWidth;
    float screenHeight;
    float wr = 1.0f;
    float hr = 1.0f;
    String fontName;

    public LoadFrameFragment(FrameModel iArr, float wr, float hr, FramePagerAdapter.OnFrameSelect onOverlaySelected) {
        this.model = iArr;
        this.onOverlaySelected = onOverlaySelected;
        this.wr = wr;
        this.hr = hr;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_load_frame, container, false);

        relativeLayout = view.findViewById(R.id.main_lay);
        context = getActivity();

        if (model.getJson() == null){
            Glide.with(context)
                    .asBitmap()
                    .load(Functions.getItemBaseUrl(model.getThumbnail()))
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            BitmapDrawable background = new BitmapDrawable(resource);
                            relativeLayout.setBackgroundDrawable(background);
                        }
                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
            return view;
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.screenWidth = (float) displayMetrics.widthPixels;
        this.screenHeight = (float) (displayMetrics.heightPixels - ImageUtils.dpToPx(context, 105.0f));

//        stickerInfoArrayList = getStickerArrayList("frameStickerArray" + model.getId());
//        textInfoArrayList = getTextArrayList("frameTextArray" + model.getId());

//        if (stickerInfoArrayList != null &&
//                textInfoArrayList != null) {
//
//            LoadFrameAsync loadFrameAsync = new LoadFrameAsync();
//            loadFrameAsync.execute();
//
//        } else {
        FrameUtils frameUtils = new FrameUtils(context);
        frameUtils.proccessFrame(context, model, new FrameUtils.OnFrameStatus() {
            @Override
            public void onFrameLoaded(ArrayList<Sticker_info> stickerInfos, ArrayList<textInfo> textInfos) {
                Functions.showLoader(context);

                stickerInfoArrayList = stickerInfos;
                textInfoArrayList = textInfos;

//                    saveStickerArrayList(stickerInfoArrayList, "frameStickerArray" + model.getId());
//                    saveTextArrayList(textInfoArrayList, "frameTextArray" + model.getId());

                try {
                    LoadFrameAsync loadFrameAsync = new LoadFrameAsync();
                    loadFrameAsync.execute();
                }catch (Exception e){}

            }
        });
//        }

        return view;
    }

    public void saveStickerArrayList(ArrayList<Sticker_info> list, String key) {
        SharedPreferences.Editor editor = Functions.getSharedPreference(context).edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);

        editor.putString(key, json);
        editor.apply();
    }

    public ArrayList<Sticker_info> getStickerArrayList(String key) {
        SharedPreferences prefs = Functions.getSharedPreference(context);
        String json = prefs.getString(key, null);

        Type type = new TypeToken<ArrayList<Sticker_info>>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    public void saveTextArrayList(ArrayList<textInfo> list, String key) {
        SharedPreferences.Editor editor = Functions.getSharedPreference(context).edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);

        editor.putString(key, json);
        editor.apply();
    }

    public ArrayList<textInfo> getTextArrayList(String key) {
        SharedPreferences prefs = Functions.getSharedPreference(context);
        String json = prefs.getString(key, null);

        Type type = new TypeToken<ArrayList<textInfo>>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    private class LoadFrameAsync extends AsyncTask<String, String, Boolean> {

        private LoadFrameAsync() {

        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }


        public Boolean doInBackground(String... strArr) {

            ArrayList<ElementInfo> stickerArrayList;
            ArrayList<com.visticsolution.posterbanao.editor.View.text.TextInfo> textInfoArrayList2;
            String str;

            textInfoArrayList2 = new ArrayList<>();
            stickerArrayList = new ArrayList<>();

            for (int i = 0; i < stickerInfoArrayList.size(); i++) {

                int newWidht = getNewWidht(Float.valueOf(stickerInfoArrayList.get(i).getSt_x_pos()).floatValue(),
                        Float.valueOf(stickerInfoArrayList.get(i).getSt_width()).floatValue());
                int newHeight = getNewHeight(Float.valueOf(stickerInfoArrayList.get(i).getSt_y_pos()).floatValue(), Float.valueOf(stickerInfoArrayList.get(i).getSt_height()).floatValue());


                int i2 = newWidht;//< 10 ? 20 : (newWidht <= 10 || newWidht > 20) ? newWidht : 35;
                int i3 = newHeight;// < 10 ? 20 : (newHeight <= 10 || newHeight > 20) ? newHeight : 35;

                if (stickerInfoArrayList.get(i).getSt_field2() != null) {
                    str = stickerInfoArrayList.get(i).getSt_field2();
                } else {
                    str = "";
                }
                float parseInt = (stickerInfoArrayList.get(i).getSt_rotation() == null ||
                        stickerInfoArrayList.get(i).getSt_rotation().equals("")) ? 0.0f :
                        (float) Integer.parseInt(stickerInfoArrayList.get(i).getSt_rotation());

                float xpos = getXpos(Float.valueOf(stickerInfoArrayList.get(i).getSt_x_pos()).floatValue());
                float ypos = getYpos(Float.valueOf(stickerInfoArrayList.get(i).getSt_y_pos()).floatValue());

                stickerArrayList.add(new ElementInfo(stickerInfoArrayList.get(i).getName(), xpos, ypos, i2, i3, parseInt, 0.0f, "", "STICKER", Integer.parseInt(stickerInfoArrayList.get(i).getSt_order()), 0, 255, 0, 0, 0, 0, stickerInfoArrayList.get(i).getSt_image(), "colored", 1, 0, str, "", "", null, null));
            }
            for (int i5 = 0; i5 < textInfoArrayList.size(); i5++) {

                String text = textInfoArrayList.get(i5).getText();
                String font_family = textInfoArrayList.get(i5).getFont_family();
                String justification = textInfoArrayList.get(i5).getTxt_justification();
                String weight = textInfoArrayList.get(i5).getTxt_weight();

                int parseColor = Color.parseColor(textInfoArrayList.get(i5).getTxt_color());
                float xpos2 = getXpos(Float.valueOf(textInfoArrayList.get(i5).getTxt_x_pos()).floatValue());
                float ypos = getYpos(Float.valueOf(textInfoArrayList.get(i5).getTxt_y_pos()).floatValue());
                int newWidht2 = getNewWidht(Float.valueOf(textInfoArrayList.get(i5).getTxt_x_pos()).floatValue(), Float.valueOf(textInfoArrayList.get(i5).getTxt_width()).floatValue());
                int newHeit2 = getNewHeightText(Float.valueOf(textInfoArrayList.get(i5).getTxt_y_pos()).floatValue(),
                        Float.valueOf(textInfoArrayList.get(i5).getTxt_height()).floatValue());
                textInfoArrayList2.add(new TextInfo(text, font_family, parseColor, 100, ViewCompat.MEASURED_STATE_MASK, 0, "0", ViewCompat.MEASURED_STATE_MASK, 0, xpos2, ypos, newWidht2, newHeit2, Float.parseFloat(textInfoArrayList.get(i5).getTxt_rotation()), "TEXT", Integer.parseInt(textInfoArrayList.get(i5).getTxt_order()), 0, 0, 0, 0, 0, "", "", "", 0.0f, 0.0f, 0, 0, weight, justification));
            }

            txtShapeList = new HashMap<>();
            Iterator<TextInfo> it = textInfoArrayList2.iterator();
            while (it.hasNext()) {
                com.visticsolution.posterbanao.editor.View.text.TextInfo next = it.next();
                txtShapeList.put(Integer.valueOf(next.getORDER()), next);
            }
            Iterator<ElementInfo> it2 = stickerArrayList.iterator();
            while (it2.hasNext()) {
                ElementInfo next2 = it2.next();
                txtShapeList.put(Integer.valueOf(next2.getORDER()), next2);
            }
            return true;
        }

        @Override
        public void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            Functions.cancelLoader();

            relativeLayout.removeAllViews();
            ArrayList arrayList = new ArrayList(txtShapeList.keySet());
            Collections.sort(arrayList);
            int size = arrayList.size();

            for (int i = 0; i < size; i++) {
                Object obj = txtShapeList.get(arrayList.get(i));
//                Log.d("listSize__", model.getId() + "T _ " + textInfoArrayList2.size());

                if (obj instanceof ElementInfo) {
                    ElementInfo elementInfo = (ElementInfo) obj;

                    StickerView stickerView = new StickerView(context);
                    relativeLayout.addView(stickerView);
                    stickerView.optimizeScreen(screenWidth, screenHeight);
                    stickerView.setViewWH((float) relativeLayout.getWidth(), (float) relativeLayout.getHeight());
                    stickerView.setComponentInfo(elementInfo);
                    stickerView.setFrameItem(true);
                    stickerView.setId(ViewIdGenerator.generateViewId());
                    stickerView.optimize(wr, hr);
                    stickerView.setDefaultTouchListener(false);
                    stickerView.setBorderVisibility(false);
                } else {
                    AutofitTextRel autofitTextRel = new AutofitTextRel(context);
                    relativeLayout.addView(autofitTextRel);
                    com.visticsolution.posterbanao.editor.View.text.TextInfo textInfo = (com.visticsolution.posterbanao.editor.View.text.TextInfo) obj;

                    autofitTextRel.setTextInfo(textInfo, false);
                    autofitTextRel.setFrameItem(true);
                    autofitTextRel.setId(ViewIdGenerator.generateViewId());
                    autofitTextRel.optimize(wr, hr);
                    autofitTextRel.setBorderVisibility(false);
                    autofitTextRel.setDefaultTouchListener(false);
                    try {
//                        if (textInfo.getFONT_WEIGHT().equals("bold")){
//                            autofitTextRel.setBoldFont();
//                        }
                        if (textInfo.getFONT_JUSTIFY().equals("left")) {
                            autofitTextRel.setLeftAlignMent();
                        }
                        if (textInfo.getFONT_JUSTIFY().equals("center")) {
                            autofitTextRel.setCenterAlignMent();
                        }
                        if (textInfo.getFONT_JUSTIFY().equals("right")) {
                            autofitTextRel.setRightAlignMent();
                        }
//                        Log.d("onFrameLoaded__","EDITOR _> "+textInfo.getFONT_WEIGHT());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("onFrameLoaded__", "E => " + e.getMessage());
                    }

                    if (textInfo.getFONT_NAME() != null) {
                        setTextFonts(textInfo.getFONT_NAME());
                    }

                    fontName = textInfo.getFONT_NAME();
                }
            }

        }
    }

    public void setTextFonts(String str) {
        this.fontName = str;
        int childCount = relativeLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = relativeLayout.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setTextFont(str);
                }
            }
        }
    }

    public float getXpos(float f) {
        return (((float) this.relativeLayout.getWidth()) * f) / 100.0f;
    }

    public float getYpos(float f) {
        return (((float) this.relativeLayout.getHeight()) * f) / 100.0f;
    }

    public int getNewWidht(float f, float f2) {
        return (int) ((((float) this.relativeLayout.getWidth()) * (f2 - f)) / 100.0f);
    }

    public int getNewHeight(float f, float f2) {
        return (int) ((((float) this.relativeLayout.getHeight()) * (f2 - f)) / 100.0f);
    }

    public int getNewHeightText(float f, float f2) {
        float height = (((float) this.relativeLayout.getHeight()) * (f2 - f)) / 100.0f;
        return (int) (((float) ((int) height)) + (height / 2.0f));
    }

}