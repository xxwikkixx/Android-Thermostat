package com.mawsom.mawsom.mawsomnobl.data;

import org.json.JSONObject;

/**
 * Created by Waqas on 5/9/2015.
 */
public class Units implements JSONPopulator {
    private String temperature;
    public String getTemperature() {
        return temperature;
    }
    @Override
    public void populate(JSONObject data) {
        temperature = data.optString("temperature");
    }
}