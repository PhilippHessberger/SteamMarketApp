package com.example.steammarketapp;

import org.json.JSONObject;

public interface VolleyCallback {
    void onSuccess(JSONObject response, DescriptionModel descriptionModel);
}
