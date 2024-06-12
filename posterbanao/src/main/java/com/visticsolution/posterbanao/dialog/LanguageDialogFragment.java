package com.visticsolution.posterbanao.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.visticsolution.posterbanao.adapter.LanguageAdapter;
import com.visticsolution.posterbanao.classes.Callback;
import com.visticsolution.posterbanao.classes.Constants;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.classes.Variables;
import com.visticsolution.posterbanao.interfaces.AdapterClickListener;
import com.visticsolution.posterbanao.model.LanguageModel;
import com.visticsolution.posterbanao.R;

import java.util.ArrayList;
import java.util.List;

public class LanguageDialogFragment extends DialogFragment {

    Callback callback;

    public LanguageDialogFragment(Callback callback) {
        this.callback = callback;
    }

    RecyclerView recyclerview;
    LanguageAdapter adapter;
    LanguageModel selectedLanguage = new LanguageModel();
    Activity context;
    ArrayList<LanguageModel> languageList=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_language_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setAttributes(getLayoutParams(getDialog()));
        context = getActivity();

        recyclerview = view.findViewById(R.id.recyclerview);
        languageList = Constants.languageList;

        String jsonText = Functions.getSharedPreference(context).getString(Variables.SELCT_LANGUAGE,"");
        Gson gson = new Gson();
        List<String> list = gson.fromJson(jsonText, List.class);

        if (list != null && list.size() > 0){
            for (int i = 0; i < languageList.size(); i++){
                languageList.get(i).setSelected(false);
                for (int i2 = 0; i2 < list.size(); i2++){
                    if (languageList.get(i).getLanguage_code().equals(list.get(i2))){
                        languageList.get(i).setSelected(true);
                    }
                }
            }
        }

        adapter=new LanguageAdapter(getContext(),languageList, new AdapterClickListener() {
            @Override
            public void onItemClick(View view, int pos, Object object) {
                selectedLanguage = (LanguageModel) object;
                SharedPreferences.Editor editor2 = Functions.getSharedPreference(getActivity()).edit();
                editor2.putString(Variables.TEST_APP_LANGUAGE_CODE, selectedLanguage.language_code);
                editor2.apply();
                adapter.notifyDataSetChanged();
            }
        });
        recyclerview.setAdapter(adapter);

        view.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        view.findViewById(R.id.save_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> languages = new ArrayList<>();
                for (int i = 0; i < languageList.size(); i++){
                    if (languageList.get(i).isSelected()){
                        languages.add(languageList.get(i).language_code);
                    }
                }

                if (languages.size() > 0){
                    String jsonText = gson.toJson(languages);
                    Functions.getSharedPreference(context).edit().putString(Variables.SELCT_LANGUAGE,jsonText).apply();
                    callback.Responce("done");
                    dismiss();
                }else {
                    Functions.showToast(context,getString(R.string.please_select_min_one_language));
                }
            }
        });
    }
    private WindowManager.LayoutParams getLayoutParams(@NonNull Dialog dialog) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (dialog.getWindow() != null) {
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
        }
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        return layoutParams;
    }
}