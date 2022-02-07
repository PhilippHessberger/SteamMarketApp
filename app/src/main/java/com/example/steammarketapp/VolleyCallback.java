package com.example.steammarketapp;

import org.json.JSONObject;

import java.util.ArrayList;

public interface VolleyCallback {
    void onSuccess(JSONObject response, DescriptionModel descriptionModel);
}
