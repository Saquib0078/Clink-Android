package com.visticsolution.posterbanao.editor.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.editor.EditorActivity;
import com.visticsolution.posterbanao.editor.model.Sticker_info;
import com.visticsolution.posterbanao.editor.model.textInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class TamplateUtils {

    public String backgroundImage = "";
    public String realX = "";
    public String realY = "";
    public String calcWidth = "";
    public String calcHeight = "";
    public String template_w_h_ratio = "";
    public ArrayList<Sticker_info> stickerInfoArrayList = new ArrayList<>();
    public ArrayList<textInfo> textInfoArrayList = new ArrayList<>();
    static float templateRealWidth = 0;
    static float templateRealHeight = 0;

    Activity context;

    public TamplateUtils(Activity context) {
        this.context = context;
    }

    public void openEditorActivity(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArrayLayers = jsonObject.getJSONArray("layers");
            for (int i = 0; i < jsonArrayLayers.length(); i++) {

                JSONObject jsonObject1 = jsonArrayLayers.getJSONObject(i);
                processJson(i, jsonObject1, context);

            }

            new Handler(Looper.getMainLooper()).postDelayed(() -> gotoEditorActivity(context), 1000);
        } catch (Exception e) {
            Log.d("JSONException___",e.getMessage());
        }
    }

    public void processJson(int i, JSONObject jsonObject1, Activity context) throws Exception {

        String type = jsonObject1.getString("type");

        String width = jsonObject1.getString("width");
        String height = jsonObject1.getString("height");

        String x = jsonObject1.getString("x");
        String y = jsonObject1.getString("y");

        realX = x;
        realY = y;


        calcWidth = "";
        calcHeight = "";



        if (i == 0) {
            templateRealWidth = Float.parseFloat(width);
            templateRealHeight = Float.parseFloat(height);

            template_w_h_ratio = width + ":" + height;

            calcWidth = width;
            calcHeight = height;
        } else {

            realX = String.valueOf((Float.parseFloat(x) * 100) / templateRealWidth);
            realY = String.valueOf((Float.parseFloat(y) * 100) / templateRealHeight);

            calcWidth = String.valueOf(Float.parseFloat(width) * 100 / templateRealWidth + Float.parseFloat(realX));
            calcHeight = String.valueOf(Float.parseFloat(height) * 100 / templateRealHeight + Float.parseFloat(realY));

            Log.d("JSONException___",jsonObject1.getString("name")+" - "+calcWidth + " " + calcHeight);
        }


        if (type != null) {

            if (type.contains("image")) {
//                String stickerUrl = "upload/" + templateName + "" + jsonObject1.getString("src").replace("..", "");

                // Bg
                if (i == 0) {

                    backgroundImage = Functions.getItemBaseUrl(jsonObject1.getString("src"));

                } else {

                    Sticker_info info = new Sticker_info();

                    info.setSticker_id(String.valueOf(i));
                    info.setSt_image(Functions.getItemBaseUrl(jsonObject1.getString("src")));
                    info.setSt_order(String.valueOf(i));
                    info.setSt_height(calcHeight);
                    info.setSt_width(calcWidth);
                    info.setSt_x_pos(realX);
                    info.setSt_y_pos(realY);
                    info.setSt_rotation("0");
                    stickerInfoArrayList.add(info);

                }
            } else if (type.contains("text")) {

                String color = jsonObject1.getString("color");

                String font = jsonObject1.getString("font");

                LoadFonts loadFonts = new LoadFonts(context, Functions.getItemBaseUrl(font));
                loadFonts.execute();

                font = font.substring(font.lastIndexOf('/') + 1);

                String text = jsonObject1.getString("text");
                String size = jsonObject1.getString("size");

                String weight = "";
                if (jsonObject1.has("weight")){
                    weight = jsonObject1.getString("weight");
                }
                String justification = jsonObject1.getString("justification");

                if (!jsonObject1.has("rotation")) {

                    jsonObject1.put("size", Integer.parseInt(size) + 15);
                    jsonObject1.put("y", Integer.parseInt(y) + 5);
                    y = jsonObject1.getString("y");

                    size = jsonObject1.getString("size");

                    String calSizeHeight = String.valueOf(Float.parseFloat(size) - Float.parseFloat(height)).replace("-", "");

                    String calRealY = String.valueOf(Float.parseFloat(y) - Float.parseFloat(calSizeHeight));

                    realY = String.valueOf((Float.parseFloat(calRealY) * 100) / templateRealHeight);

                    calcHeight = String.valueOf(Float.parseFloat(size) * 100 / templateRealHeight + Float.parseFloat(realY));
                }

                String rotation = "0";
                if (jsonObject1.has("rotation")) {
                    rotation = jsonObject1.getString("rotation");
                }

                textInfo textInfo = new textInfo();
                textInfo.setText_id(String.valueOf(i));

                textInfo.setText(text);
                textInfo.setTxt_height(calcHeight);
                textInfo.setTxt_width(calcWidth);
                textInfo.setTxt_x_pos(realX);
                textInfo.setTxt_y_pos(realY);
                textInfo.setTxt_rotation(rotation);
                textInfo.setTxt_color(color.replace("0x", "#"));
                textInfo.setTxt_order("" + i);
                textInfo.setFont_family(font);
                textInfo.setTxt_weight(weight);
                textInfo.setTxt_justification(justification);

                textInfoArrayList.add(textInfo);

            }
        }
    }

    static class LoadFonts extends AsyncTask<String, String, String> {

        private String fontUrl;
        InputStream inputStream;
        Context context;

        public LoadFonts(Context context, String fontUrl) {
            this.fontUrl = fontUrl;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            String fileName = fontUrl.substring(fontUrl.lastIndexOf('/') + 1);
            String fontPath = Functions.getAppFolder(context) + "font/";

            if (!new File(fontPath).exists()){
                new File(fontPath).mkdirs();
                new File(fontPath).mkdir();
            }
            if (!new File(fontPath, fileName).exists()) {
                try {
                    inputStream = new java.net.URL(fontUrl).openStream();
                    FileOutputStream fileOutputStream = new FileOutputStream(fontPath + "/" + fileName );
                    int length;
                    byte[] buffer = new byte[1024];
                    while ((length = inputStream.read(buffer)) > -1) {
                        fileOutputStream.write(buffer, 0, length);
                    }
                    fileOutputStream.close();
                    inputStream.close();
                } catch (IOException e) {
                }
            }
            return "0";
        }

        @SuppressLint("WrongThread")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    private void gotoEditorActivity(Activity context) {
        Intent intent = new Intent(context, EditorActivity.class);
        intent.putParcelableArrayListExtra("text", textInfoArrayList);
        intent.putParcelableArrayListExtra("sticker", stickerInfoArrayList);
        intent.putExtra("backgroundImage", backgroundImage);
        intent.putExtra("isTamplate", 1);
        intent.putExtra("ration", template_w_h_ratio);
        context.startActivity(intent);
    }
}
