package com.example.steammarketapp;

import com.example.steammarketapp.data_models.DescriptionModel;

import org.json.JSONObject;

public interface VolleyCallback {
    void onSuccess(JSONObject response, DescriptionModel descriptionModel);
}
