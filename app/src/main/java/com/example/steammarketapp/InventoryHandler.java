package com.example.steammarketapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;

public class InventoryHandler {

    private final Context context;

    public InventoryHandler(Context context) {
        this.context = context;
    }

    public ArrayList<ItemModel> extractInventoryHistoryFromJsonFile(File file) {
        return null;
    }

    public void downloadInventoryForSteamID(String steamID) {

        // TODO: Evaluate if this is really the best and efficient course of action:
        // TODO: Check if there is already a file for this steamID
        // TODO: If there is one, open it, and add another entry
        // TODO: If there is none, create a new one and make the first entry
        String inventoryURL = "https://steamcommunity.com/id/" + steamID + "/inventory/json/730/2";
        RequestQueue queue = Volley.newRequestQueue(context);
        ArrayList<ItemModel> itemModelArrayList = new ArrayList<>();
        ArrayList<DescriptionModel> descriptionModelArrayList = new ArrayList<>();

        // TODO: Still only one price request because I accidentally spammed the valve servers... Remove restrictions when further testing is needed.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, inventoryURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Inventory fetched", Toast.LENGTH_SHORT).show();

                        // Extracting Items and Descriptions from JSON into ModelLists:
                        try {
                            // Items:
                            JSONObject inventory = response.getJSONObject("rgInventory");
                            Iterator<String> inventoryKeys = inventory.keys();
                            while (inventoryKeys.hasNext()) {
                                JSONObject item = inventory.getJSONObject((String) inventoryKeys.next());
                                itemModelArrayList.add(new ItemModel(
                                        item.getInt("classid"),
                                        item.getInt("amount")
                                ));
                            }

                            // Descriptions:
                            JSONObject descriptions = response.getJSONObject("rgDescriptions");
                            Iterator<String> descriptionKeys = descriptions.keys();
                            while (descriptionKeys.hasNext()) {
                                JSONObject description = descriptions.getJSONObject((String) descriptionKeys.next());
                                descriptionModelArrayList.add(new DescriptionModel(
                                        description.getInt("classid"),
                                        description.getString("market_hash_name"),
                                        description.getInt("marketable"),
                                        description.getInt("tradable")
                                ));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(context, "Data extracted", Toast.LENGTH_SHORT).show();

                        // Fetching Prices for Descriptions and matching ItemModels with their DescriptionModels:
                        String baseURL = "https://steamcommunity.com/market/priceoverview/?appid=730&currency=3&market_hash_name=";
                        int counter = 0;
                        for (DescriptionModel descriptionModel : descriptionModelArrayList) {
                            if (counter < 1 && descriptionModel.isMarketable()) {
                                counter++;
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, baseURL + descriptionModel.getItemName().replace(" ", "%20"), null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    descriptionModel.setPrice(Float.parseFloat(response.getString("lowest_price").replace("€", "").replace(",", ".")));
                                                    descriptionModel.setVolume(Integer.parseInt(response.getString("volume").replace(",", "")));
                                                    descriptionModel.setMedianPrice(Float.parseFloat(response.getString("median_price").replace("€", "").replace(",", ".")));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                /* Unnecessary to match, since we do that later on while reading the <steamID>.json.
                                                // Matching Items with their Descriptions:
                                                for (ItemModel itemModel : itemModelArrayList) {
                                                    if (descriptionModel.getClassid() == itemModel.getClassid()) {
                                                        itemModel.setDescriptionModel(descriptionModel);
                                                    }
                                                }
                                                 */
                                                // Writing the ItemList into a .json-File:
                                                JSONObject newInventoryHistoryEntry = new JSONObject();
                                                try {
                                                    // TODO: check if the LocalDateTime.now() format should be changed:
                                                    newInventoryHistoryEntry.put("date", String.valueOf(LocalDateTime.now()));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d("Error in getting prices", error.toString());
                                    }
                                });
                                queue.add(jsonObjectRequest);
                                Log.d("Request Counter: ", String.valueOf(counter));
                            }
                        }
                        Log.d("Description Counter: ", String.valueOf(descriptionModelArrayList.size()));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("While Fetching Inv.", error.toString());
                error.printStackTrace();
            }
        });

        queue.add(jsonObjectRequest);
    }
}
