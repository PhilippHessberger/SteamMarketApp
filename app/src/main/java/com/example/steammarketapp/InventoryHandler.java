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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

public class InventoryHandler {

    private final Context context;

    public InventoryHandler(Context context) {
        this.context = context;
    }

    public ArrayList<ItemModel> extractLastInventoryHistoryEntryFromJsonFile(String filename) {
        Log.d("DEBUG: ", "Starting extractLastInventoryHistoryEntryFromJsonFile now");
        // TODO: Technically it is the last entry we extract... Fix it.
        // Opening the local inventory history file:
        try {
            File[] files = context.getFilesDir().listFiles();
            // Search for the correct file:
            for (File file : files) {
                if (file.getName().equals(filename)) {
                    File myFile = file;

                    FileInputStream fileInputStream = context.openFileInput(file.getName());
                    int size = fileInputStream.available();
                    char[] inputBuffer = new char[size];
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                    inputStreamReader.read(inputBuffer);
                    String fileToString = new String(inputBuffer);

                    // Extracting data from the local file:
                    JSONObject inventoryHistory = new JSONObject(fileToString);
                    Iterator<String> inventoryEntries = inventoryHistory.keys();
                    JSONObject inventoryEntry = inventoryHistory.getJSONObject((String) inventoryEntries.next());

                    ArrayList<ItemModel> itemModelArrayList = new ArrayList<>();
                    ArrayList<DescriptionModel> descriptionModelArrayList = new ArrayList<>();


                    //  TODO: remove after testing:
                    Log.d("First entry in local file: ", inventoryEntry.toString(4));

                    // Going through all items:
                    JSONObject inventory = inventoryEntry.getJSONObject("rgInventory");
                    Iterator<String> inventoryKeys = inventory.keys();

                    //  TODO: remove after testing:
                    int counter1 = 0;
                    while (inventoryKeys.hasNext()) {
                        //  TODO: remove after testing:
                        counter1++;
                        JSONObject item = inventory.getJSONObject((String) inventoryKeys.next());
                        itemModelArrayList.add(new ItemModel(
                                item.getInt("classid"),
                                item.getInt("id")
                        ));
                    }

                    //  TODO: remove after testing:
                    Log.d("Counter Items", String.valueOf(counter1));

                    // Going through all descriptions:
                    JSONObject descriptions = inventoryEntry.getJSONObject("rgDescriptions");
                    Iterator<String> descriptionKeys = descriptions.keys();

                    //  TODO: remove after testing:
                    int counter2 = 0;
                    while (descriptionKeys.hasNext()) {
                        //  TODO: remove after testing:
                        counter2++;
                        JSONObject description = descriptions.getJSONObject((String) descriptionKeys.next());
                        descriptionModelArrayList.add(new DescriptionModel(
                                description.getInt("classid"),
                                description.getString("market_hash_name"),
                                description.getInt("marketable"),
                                description.getInt("tradable"),
                                Float.parseFloat(String.valueOf(description.getLong("lowest_price"))),
                                Float.parseFloat(String.valueOf(description.getLong("median_price"))),
                                description.getInt("volume")
                        ));
                    }

                    //  TODO: remove after testing:
                    Log.d("Counter Descriptions", String.valueOf(counter2));

                    // Matching items with their description:
                    for (DescriptionModel descriptionModel : descriptionModelArrayList) {
                        for (ItemModel itemModel : itemModelArrayList) {
                            if (descriptionModel.getClassid() == itemModel.getClassid()) {
                                itemModel.setDescriptionModel(descriptionModel);
                            }
                        }
                    }

                    return itemModelArrayList;
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void downloadInventoryForSteamID(String steamID) {
        Log.d("DEBUG: ", "Starting downloadInventoryForSteamID now");
        // This function will create a new file with the interesting contents of a steam inventory.
        // TODO: Evaluate if this is really the best and efficient course of action:
        // TODO: Check if there is already a file for this steamID
        // TODO: If there is one, open it, and add another entry

        // Check for existing files:
        if (isInventoryFilePresent(steamID)) {
            // Read existing file and add new content:
            Toast.makeText(context, "Going to update the existing File.", Toast.LENGTH_LONG).show();
        } else {
            // create new file and add new content:
            getSteamInventoryFormURL(steamID);
        }
    }

    private String getSteamInventoryFormURL(String steamID) {
        // TODO: Everything in here works, check extractDataFromJson
        Log.d("DEBUG: ", "Starting getSteamInventoryFormURL now");
        String inventoryURL = "https://steamcommunity.com/" + steamID + "/inventory/json/730/2";
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, inventoryURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Call method to extract data from json:
                        extractDataFromJson(response, queue, steamID);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        queue.add(jsonObjectRequest);

        return null;
    }

    private void extractDataFromJson(JSONObject response, RequestQueue queue, String steamID) {
        Log.d("DEBUG: ", "Starting extractDataFromJson now");
        ArrayList<ItemModel> itemModelArrayList = new ArrayList<>();
        ArrayList<DescriptionModel> descriptionModelArrayList = new ArrayList<>();

        // Extracting Items and Descriptions from JSON into ModelLists:
        try {
            // Items:
            JSONObject inventory = response.getJSONObject("rgInventory");
            Iterator<String> inventoryKeys = inventory.keys();
            while (inventoryKeys.hasNext()) {
                JSONObject item = inventory.getJSONObject((String) inventoryKeys.next());
                itemModelArrayList.add(new ItemModel(
                        item.getInt("classid"),
                        item.getInt("id")
                ));
                Log.d("New Item:", "classid: " + String.valueOf(item.getInt("classid"))
                        + " id: " + String.valueOf(item.getInt("id"))
                );
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
                Log.d("New Description:", "classid: " + String.valueOf(description.getInt("classid"))
                        + " market_hash_name: " + String.valueOf(description.getString("market_hash_name"))
                        + " marketable: " + String.valueOf(description.getInt("marketable"))
                        + " tradable: " + String.valueOf(description.getInt("marketable"))
                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Fetching Prices for Descriptions and matching ItemModels with their DescriptionModels:
        fetchPricesForDescriptions(queue,
                new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response, DescriptionModel descriptionModel) {
                        try {
                            descriptionModel.setLowestPrice(
                                    Float.parseFloat(
                                            response.getString("lowest_price")
                                            .replace("€", "")
                                            .replace(",", ".")
                                            .replace("-", "0")
                                    )
                            );
                            descriptionModel.setVolume(
                                    Integer.parseInt(
                                            response.getString  ("volume")
                                            .replace(",", "")
                                    )
                            );
                            descriptionModel.setMedianPrice(
                                    Float.parseFloat(
                                            response.getString("median_price")
                                            .replace("€", "")
                                            .replace(",", ".")
                                            .replace("-", "0")
                                    )
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                descriptionModelArrayList,
                itemModelArrayList,
                steamID
        );

        for (DescriptionModel descriptionModel : descriptionModelArrayList) {
            Log.d("des. : ", String.valueOf(descriptionModel.getLowestPrice()));
        }

        // Currently calling it here because we don't know how Callback really works
        createJsonFromData(descriptionModelArrayList, itemModelArrayList, steamID);
    }

    private void fetchPricesForDescriptions(RequestQueue queue, VolleyCallback volleyCallback, ArrayList<DescriptionModel> descriptionModelArrayList, ArrayList<ItemModel> itemModelArrayList, String steamID) {
        Log.d("DEBUG: ", "Starting fetchPricesForDescriptions now");
        String baseURL = "https://steamcommunity.com/market/priceoverview/?appid=730&currency=3&market_hash_name=";
        int descriptionCounter = 0;
        String output = "";
        for (DescriptionModel descriptionModel : descriptionModelArrayList) {
            if (descriptionModel.isMarketable()) {
                // TODO: remove counter and URL debug messages:
                Log.d("Counter ", String.valueOf(descriptionCounter++));
                Log.d("Counter ", "URL of description: " + baseURL + descriptionModel.getItemName().replace(" ", "%20"));
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        baseURL + descriptionModel.getItemName().replace(" ", "%20"),
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                volleyCallback.onSuccess(response, descriptionModel);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error in getting prices", error.toString());
                            }
                        }
                );
                queue.add(jsonObjectRequest);
            }
            try {
                Thread.sleep(3500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Log.d("DEBUG: ", "Description-Counter: " + String.valueOf(descriptionCounter));
        Log.d("DEBUG: ", "List of all descriptions: ");
        // Only sends 2 prices, both 0.0 ?!
        for (DescriptionModel descriptionModel : descriptionModelArrayList) {
            Log.d("price", String.valueOf(descriptionModel.getLowestPrice()));
        }
    }

    private void createJsonFromData(ArrayList<DescriptionModel> descriptionModelArrayList, ArrayList<ItemModel> itemModelArrayList, String steamID) {
        Log.d("DEBUG: ", "Starting createJsonFromData now");
        try {
            JSONObject rgDescriptions = new JSONObject();
            JSONObject rgInventory = new JSONObject();

            for (DescriptionModel model : descriptionModelArrayList) {
                JSONObject newDescriptionEntry = new JSONObject()
                        .put("classid", String.valueOf(model.getClassid()))
                        .put("market_hash_name", model.getItemName())
                        .put("lowest_price", model.getLowestPrice())
                        .put("volume", model.getVolume())
                        .put("median_price", model.getMedianPrice());

                if (model.isMarketable()) {
                    newDescriptionEntry.put("marketable", 1);
                } else {
                    newDescriptionEntry.put("marketable", 0);
                }

                if (model.isTradable()) {
                    newDescriptionEntry.put("tradable", 1);
                } else {
                    newDescriptionEntry.put("tradable", 0);
                }
                rgDescriptions.put(String.valueOf(model.getClassid()), newDescriptionEntry);
                Log.d("New description: ", newDescriptionEntry.toString());
            }

            for (ItemModel model : itemModelArrayList) {
                JSONObject newItemEntry = new JSONObject()
                        .put("classid", model.getClassid())
                        .put("id", model.getId());
                rgInventory.put(String.valueOf(model.getId()), newItemEntry);
                Log.d("New item: ", newItemEntry.toString());
            }

            JSONObject newInventoryHistoryEntry = new JSONObject()
                    .put("rgInventory", rgInventory)
                    .put("rgDescriptions", rgDescriptions);

            JSONObject newEntry = new JSONObject().put(String.valueOf(LocalDateTime.now()), newInventoryHistoryEntry);

            writeJsonToLocalFile(newEntry, steamID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void writeJsonToLocalFile(JSONObject jsonObject, String steamID) {
        Log.d("DEBUG: ", "Starting writeJsonToLocalFile now");
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(("inv_" + steamID + ".json").replace("/", "_"), context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

            outputStreamWriter.write(jsonObject.toString());
            Log.d("Writing json to local file:", jsonObject.toString());
            Toast.makeText(context, "Local file created", Toast.LENGTH_SHORT).show();

            outputStreamWriter.flush();
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isInventoryFilePresent(String steamID) {
        // Checking for steamID-File
        File[] files = context.getFilesDir().listFiles();
        if (files.length != 0) {
            for (File file : files) {
                if (file.getName().contains(steamID)) {
                    return true;
                }
            }
        }
        return false;
    }
}
