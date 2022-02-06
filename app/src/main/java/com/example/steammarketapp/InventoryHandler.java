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

    public ArrayList<ItemModel> extractInventoryHistoryFromJsonFile(String filename) {
        // Opening the local inventory history file:
        try {
            File[] files = context.getFilesDir().listFiles();
            for (File file : files) {
                if (file.getName().equals(filename)) {
                    File myFile = file;

                    FileInputStream fileInputStream = context.openFileInput(file.getName());
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

                    char[] inputBuffer = new char[(int) myFile.length()];

                    inputStreamReader.read(inputBuffer);

                    String fileToString = new String(inputBuffer);

                    // Extracting data from the local file:

                    // Searching for different snapshots of the inventory:
                    JSONObject inventoryHistory = new JSONObject(fileToString);
                    Iterator<String> inventoryEntries = inventoryHistory.keys();
                    // TODO: Make it return the history and not the current snapshot of the inventory
                    while (inventoryEntries.hasNext()) {
                        ArrayList<ItemModel> itemModelArrayList = new ArrayList<>();
                        ArrayList<DescriptionModel> descriptionModelArrayList = new ArrayList<>();
                        JSONObject inventoryEntry = inventoryHistory.getJSONObject((String) inventoryEntries.next());

                        //  TODO: remove after testing:
                        Log.d("Reading local json file:", inventoryEntry.toString());

                        // Items:
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

                        // Descriptions:
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
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void downloadInventoryForSteamID(String steamID) {
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
        String inventoryURL = "https://steamcommunity.com/" + steamID + "/inventory/json/730/2";
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, inventoryURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Call method to extract data from json:
                        extractDataFromJson(response, queue, steamID);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Remove stuff added for testing
                Log.d("While Fetching Inv.", error.toString());
                error.printStackTrace();
            }
        });

        queue.add(jsonObjectRequest);

        ArrayList<ItemModel> itemModelArrayList = new ArrayList<>();
        ArrayList<DescriptionModel> descriptionModelArrayList = new ArrayList<>();

        // TODO: Still only one price request because I accidentally spammed the valve servers... Remove restrictions when further testing is needed.

        return null;
    }

    private void extractDataFromJson(JSONObject response, RequestQueue queue, String steamID) {
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

        // Fetching Prices for Descriptions and matching ItemModels with their DescriptionModels:
        fetchPricesForDescriptions(queue, descriptionModelArrayList, itemModelArrayList, steamID);
    }

    private void fetchPricesForDescriptions(RequestQueue queue, ArrayList<DescriptionModel> descriptionModelArrayList, ArrayList<ItemModel> itemModelArrayList, String steamID) {
        String baseURL = "https://steamcommunity.com/market/priceoverview/?appid=730&currency=3&market_hash_name=";
        int counter = 0;
        for (DescriptionModel descriptionModel : descriptionModelArrayList) {
            if (descriptionModel.isMarketable()) {
                Log.d("Counter ", String.valueOf(counter++));
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        baseURL + descriptionModel.getItemName().replace(" ", "%20"),
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
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
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Findet hier immer noch keine preise. behindert der sleep das vllt?
        while (descriptionModelArrayList.get(descriptionModelArrayList.size() - 1).getLowestPrice() < 0) {
            try {
                Thread.sleep(1000);
                Log.d("Debug ", "Waiting on prices");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // TODO: Why is this called twice?
        Log.d("Hope", "pls let prices be here...");

        // Only sends 2 prices, both 0.0
        for (DescriptionModel descriptionModel : descriptionModelArrayList) {
            Log.d("price", String.valueOf(descriptionModel.getLowestPrice()));
        }

        // is 52...?!
        Log.d("Final Counter: ", String.valueOf(counter));

        // Writing the ItemList into a .json-File:
        // TODO: Currently just hoping everything goes well with price fetching...
        createJsonFromData(descriptionModelArrayList, itemModelArrayList, steamID);
    }

    private void createJsonFromData(ArrayList<DescriptionModel> descriptionModelArrayList, ArrayList<ItemModel> itemModelArrayList, String steamID) {
        try {
            // TODO: check if the LocalDateTime.now() format should be changed:
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
            }

            for (ItemModel model : itemModelArrayList) {
                JSONObject newItemEntry = new JSONObject()
                        .put("classid", model.getClassid())
                        .put("id", model.getId());
                rgInventory.put(String.valueOf(model.getId()), newItemEntry);
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
