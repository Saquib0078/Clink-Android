package com.nirmiteepublic.clink.functions.listeners.option;

import org.json.JSONObject;

public abstract class OptionsCallBackListener {
    public void onContinue(int index, JSONObject data) {

    }

    public void onOptionsRequest(OptionRequestType type, String title, String preData, OptionsRequestCallBackListener optionsRequestCallBackListener) {

    }

}
