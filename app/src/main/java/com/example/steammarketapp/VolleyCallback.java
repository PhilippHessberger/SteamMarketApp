package com.example.steammarketapp;

import com.example.steammarketapp.data_models.ModelDescription;

import org.json.JSONObject;

public interface VolleyCallback {
    void onSuccess(JSONObject response, ModelDescription modelDescription);
}
