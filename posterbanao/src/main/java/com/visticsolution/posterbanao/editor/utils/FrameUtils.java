package com.visticsolution.posterbanao.editor.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.classes.Variables;
import com.visticsolution.posterbanao.editor.model.Sticker_info;
import com.visticsolution.posterbanao.editor.model.textInfo;
import com.visticsolution.posterbanao.model.FrameModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FrameUtils {

    public String realX = "";
    public String realY = "";
    public String calcWidth = "";
    public String calcHeight = "";
    public ArrayList<Sticker_info> stickerInfoArrayList = new ArrayList<>();
    public ArrayList<textInfo> textInfoArrayList = new ArrayList<>();
    float templateRealWidth = 0;
    float templateRealHeight = 0;
    public OnFrameStatus listner;

    Context context;

    public FrameUtils(Context context) {
        this.context = context;
    }

    public void proccessFrame(Context context, FrameModel model, OnFrameStatus onFrameStatus) {
        stickerInfoArrayList.clear();
        textInfoArrayList.clear();
        listner = onFrameStatus;
        try {
            JSONObject jsonObject = new JSONObject(model.getJson());
            JSONArray jsonArrayLayers = jsonObject.getJSONArray("layers");

            for (int i = 0; i < jsonArrayLayers.length(); i++) {

                JSONObject jsonObject1 = jsonArrayLayers.getJSONObject(i);
                processJson(context,model,i, jsonObject1);

            }

            new Handler(Looper.getMainLooper()).postDelayed(() -> listner.onFrameLoaded(stickerInfoArrayList,textInfoArrayList), 1000);

        } catch (Exception e) {
            Toast.makeText(context, "proccessFrame -> "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("onFrameLoaded__",e.getMessage());
        }
    }

    public void processJson(Context context,FrameModel model,int i, JSONObject jsonObject1) throws Exception {

        String type = jsonObject1.getString("type");
        String name = jsonObject1.getString("name");

        String width = String.valueOf(jsonObject1.getInt("width"));
        String height = String.valueOf(jsonObject1.getInt("height"));

        String x = jsonObject1.getString("x");
        String y = jsonObject1.getString("y");

        realX = x;
        realY = y;

        if (i == 0) {
            templateRealWidth = Float.parseFloat(width);
            templateRealHeight = Float.parseFloat(height);

            calcWidth = width;
            calcHeight = height;
        }else{
            realX = String.valueOf((Float.parseFloat(x) * 100) / templateRealWidth);
            realY = String.valueOf((Float.parseFloat(y) * 100) / templateRealHeight);

            calcWidth = String.valueOf(Float.parseFloat(width) * 100 / templateRealWidth + Float.parseFloat(realX));
            calcHeight = String.valueOf(Float.parseFloat(height) * 100 / templateRealHeight + Float.parseFloat(realY));
        }

        if (type != null) {

            if (type.contains("image")) {

                Sticker_info info = new Sticker_info();
                info.setSticker_id(String.valueOf(i));
                info.setName(name);
                if (name.equals("logo")){
                    if (model.getType().equals("political")){
                        info.setSt_image(Functions.getItemBaseUrl(Functions.getSharedPreference(context).getString(Variables.POLITICAL_LOGO,"https://png.pngtree.com/png-vector/20190307/ourlarge/pngtree-political-logo-and-icon-design-png-image_782175.jpg")));
                    }else if (model.getType().equals("personal")){
                        info.setSt_image(Functions.getItemBaseUrl(Functions.getSharedPreference(context).getString(Variables.P_PIC,"https://png.pngtree.com/png-vector/20190307/ourlarge/pngtree-political-logo-and-icon-design-png-image_782175.jpg")));
                    }else{
                        info.setSt_image(Functions.getItemBaseUrl(Functions.getSharedPreference(context).getString(Variables.BUSSINESS_LOGO,"https://png.pngtree.com/png-vector/20190307/ourlarge/pngtree-political-logo-and-icon-design-png-image_782175.jpg")));
                    }
                }else if (name.equals("partylogo")){
                    info.setSt_image(Functions.getItemBaseUrl(Functions.getSharedPreference(context).getString(Variables.POLITICAL_CATEGORY_LOGO,"https://png.pngtree.com/png-vector/20190307/ourlarge/pngtree-political-logo-and-icon-design-png-image_782175.jpg")));
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

            } else if (type.contains("text")) {

                String color = jsonObject1.getString("color");

                String font = jsonObject1.getString("font");

                TamplateUtils.LoadFonts loadFonts = new TamplateUtils.LoadFonts(context, Functions.getItemBaseUrl(font));
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
                    jsonObject1.put("y", Integer.parseInt(y) - 5);
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

                if (name.equals("designation")){
                    textInfo.setText(Functions.getSharedPreference(context).getString(Variables.POLITICAL_DESIGNATION,""));
                }else if (name.equals("email")){
                    if (model.getType().equals("personal")){
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.U_EMAIL,""));
                    }else {
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.BUSSINESS_EMAIL,""));
                    }
                }else if (name.equals("about")){
                    textInfo.setText(Functions.getSharedPreference(context).getString(Variables.BUSSINESS_ABOUT,""));
                }else if (name.equals("address")){
                    textInfo.setText(Functions.getSharedPreference(context).getString(Variables.BUSSINESS_ADDRESS,""));
                }else if (name.equals("website")){
                    textInfo.setText(Functions.getSharedPreference(context).getString(Variables.WEBSITE,""));
                }else if (name.equals("company")){
                    textInfo.setText(Functions.getSharedPreference(context).getString(Variables.BUSSINESS_NAME,""));
                }else if (name.equals("name")){
                    if (model.getType().equals("political")){
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.POLITICAL_NAME,""));
                    }else if (model.getType().equals("personal")){
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.NAME,""));
                    }else{
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.BUSSINESS_OWNER,""));
                    }
                }else if (name.equals("number")){
                    if (model.getType().equals("political")){
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.POLITICAL_NUMBER,""));
                    }else if (model.getType().equals("personal")){
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.NUMBER,""));
                    }else{
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.BUSSINESS_NUMBER,""));
                    }
                }else if (name.equals("whatsapp")){
                    if (model.getType().equals("political")){
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.POLITICAL_WHATSAPP,""));
                    }else{
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.WHATSAPP,""));
                    }
                }else if (name.equals("facebook")){
                    if (model.getType().equals("political")){
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.POLITICAL_FACEBOOK,""));
                    }else{
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.FACEBOOK,""));
                    }
                }else if (name.equals("twitter")){
                    if (model.getType().equals("political")){
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.POLITICAL_TWITTER,"Twitter"));
                    }else{
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.TWITTER,""));
                    }
                }else if (name.equals("youtube")){
                    if (model.getType().equals("political")){
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.POLITICAL_YOUTUBE,"Yotube"));
                    }else{
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.YOUTUBE,""));
                    }
                }else if (name.equals("instagram")){
                    if (model.getType().equals("political")){
                        textInfo.setText(Functions.getSharedPreference(context).getString(Variables.POLITICAL_INSTAGRAM,"Instagram"));
                    }else{
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

    public interface OnFrameStatus{
        void onFrameLoaded(ArrayList<Sticker_info> stickerInfos,ArrayList<textInfo> textInfos);
    }
}
