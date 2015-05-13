package com.mawsom.mawsom.mawsomnobl.data;

import org.json.JSONObject;

/**
 * Created by Waqas on 5/9/2015.
 */
public class Item implements JSONPopulator {
    private Condition condition;

    public Condition getCondition() {
        return condition;
    }

    @Override
    public void populate(JSONObject data) {
        condition = new Condition();
        condition.populate(data.optJSONObject("condition"));
    }
}