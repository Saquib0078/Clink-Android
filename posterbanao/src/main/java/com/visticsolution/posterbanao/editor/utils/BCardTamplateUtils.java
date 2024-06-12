package com.visticsolution.posterbanao.editor.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.classes.Variables;
import com.visticsolution.posterbanao.editor.EditorActivity;
import com.visticsolution.posterbanao.editor.model.Sticker_info;
import com.visticsolution.posterbanao.editor.model.textInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BCardTamplateUtils {

    public String backgroundImage = "";
    public String realX = "";
    public String realY = "";
    public String calcWidth = "";
    public String calcHeight = "";
    public String template_w_h_ratio = "";
    public ArrayList<Sticker_info> stickerInfoArrayList = new ArrayList<>();
    public ArrayList<textInfo> textInfoArrayList = new ArrayList<>();
    static int templateRealWidth = 0;
    static int templateRealHeight = 0;

    Context context;

    public BCardTamplateUtils(Context context) {
        this.context = context;
    }

    public void openEditorActivity(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArrayLayers = jsonObject.getJSONArray("layers");
            for (int i = 0; i < jsonArrayLayers.length(); i++) {

                JSONObject jsonObject1 = jsonArrayLayers.getJSONObject(i);
                processJson(i, jsonObject1,context);

            }

            gotoEditorActivity(context);
        } catch (Exception e) {
        }
    }

    public void processJson(int i, JSONObject jsonObject1, Context context) throws Exception {

        String type = jsonObject1.getString("type");
        String name = jsonObject1.getString("name");

        String width = jsonObject1.getString("width");
        String height = jsonObject1.getString("height");


        String x = jsonObject1.getString("x");
        String y = jsonObject1.getString("y");

        realX = x;
        realY = y;


        calcWidth = "";
        calcHeight = "";

        if (i == 0) {

            template_w_h_ratio = width+":"+height;
            templateRealWidth = Integer.parseInt(width);
            templateRealHeight = Integer.parseInt(height);

            calcWidth = width;
            calcHeight = height;

        } else {

            realX = String.valueOf(((int) Math.round(Float.parseFloat(x)) * 100) / templateRealWidth);
            realY = String.valueOf((int) Math.round(Float.parseFloat(y) * 100) / templateRealHeight);


            calcWidth = String.valueOf(Integer.parseInt(width) * 100 / templateRealWidth + Integer.parseInt(realX));
            calcHeight = String.valueOf(Integer.parseInt(height) * 100 / templateRealHeight + Integer.parseInt(realY));

        }


        if (type != null) {

            if (type.contains("image")) {

                if (i == 0) {

                    backgroundImage = jsonObject1.getString("src");

                } else {

                    Sticker_info info = new Sticker_info();

                    info.setSticker_id(String.valueOf(i));

                    if (name.equals("logo") && !Functions.getSharedPreference(context).getString(Variables.BUSSINESS_LOGO,"").equals("")){
                        info.setSt_image(Functions.getItemBaseUrl(Functions.getSharedPreference(context).getString(Variables.BUSSINESS_LOGO,"https://png.pngtree.com/png-vector/20190307/ourlarge/pngtree-political-logo-and-icon-design-png-image_782175.jpg")));
                    }else{
                        info.setSt_image(Functions.getItemBaseUrl(jsonObject1.getString("src")));
                    }

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

                TamplateUtils.LoadFonts loadFonts = new TamplateUtils.LoadFonts(context,Functions.getItemBaseUrl(font));
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

                    String calSizeHeight = String.valueOf(Integer.parseInt(size) - Integer.parseInt(height)).replace("-", "");

                    String calRealY = String.valueOf(Integer.parseInt(y) - Integer.parseInt(calSizeHeight));

                    realY = String.valueOf((int) Math.round(Float.parseFloat(calRealY) * 100) / templateRealHeight);


                    calcHeight = String.valueOf(Integer.parseInt(size) * 100 / templateRealHeight + Integer.parseInt(realY));

                }

                String rotation = "0";
                if (jsonObject1.has("rotation")) {
                    rotation = jsonObject1.getString("rotation");
                }

                textInfo textInfo = new textInfo();

                textInfo.setText_id(String.valueOf(i));
                textInfo.setText(text);
                if (!Functions.getSharedPreference(context).getString(Variables.BUSSINESS_ID,"").equals("")){
                    if (name.equals("designation")){
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.BUSINESS_DESIGNATION,""));
                    }else if (name.equals("email")){
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.BUSSINESS_EMAIL,""));
                    }else if (name.equals("about")){
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.BUSSINESS_ABOUT,""));
                    }else if (name.equals("address")){
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.BUSSINESS_ADDRESS,""));
                    }else if (name.equals("website")){
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.WEBSITE,""));
                    }else if (name.equals("company")){
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.BUSSINESS_NAME,""));
                    }else if (name.equals("name")){
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.BUSSINESS_OWNER,""));
                    }else if (name.equals("number")){
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.BUSSINESS_NUMBER,""));
                    }else if (name.equals("whatsapp")){
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.WHATSAPP,""));
                    }else if (name.equals("facebook")){
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.FACEBOOK,""));
                    }else if (name.equals("twitter")){
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.TWITTER,""));
                    }else if (name.equals("youtube")){
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.YOUTUBE,""));
                    }else if (name.equals("Instagram")){
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.INSTAGRAM,""));
                    }
                }

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

    private void gotoEditorActivity(Context context) {
        Log.d("gotoEditorActivity___",backgroundImage);
        Log.d("gotoEditorActivity___",""+textInfoArrayList.size());
        Log.d("gotoEditorActivity___",""+stickerInfoArrayList.size());
        Log.d("gotoEditorActivity___",""+template_w_h_ratio);

        Intent intent = new Intent(context, EditorActivity.class);
        intent.putParcelableArrayListExtra("text", textInfoArrayList);
        intent.putParcelableArrayListExtra("sticker", stickerInfoArrayList);
        intent.putExtra("backgroundImage", Functions.getItemBaseUrl(backgroundImage));
        intent.putExtra("isTamplate", 1);
        intent.putExtra("ration", template_w_h_ratio);
        context.startActivity(intent);
    }
}
