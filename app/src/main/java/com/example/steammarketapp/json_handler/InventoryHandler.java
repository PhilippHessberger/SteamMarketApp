package com.example.steammarketapp.json_handler;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.steammarketapp.data_models.ModelDescription;
import com.example.steammarketapp.data_models.ModelMetaData;
import com.example.steammarketapp.data_models.ModelInventory;
import com.example.steammarketapp.data_models.ModelItem;
import com.example.steammarketapp.data_models.ModelSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

public class InventoryHandler {

    private final Context context;

    public InventoryHandler(Context context) {
        this.context = context;
    }

    public ArrayList<ModelItem> extractLastInventoryHistoryEntryFromJsonFile(String filename) {
        Log.d("DEBUG: ", "Starting extractLastInventoryHistoryEntryFromJsonFile now");

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

                    ArrayList<ModelItem> modelItemArrayList = new ArrayList<>();
                    ArrayList<ModelDescription> modelDescriptionArrayList = new ArrayList<>();

                    // Going through all items:
                    JSONObject inventory = inventoryEntry.getJSONObject("rgInventory");
                    Iterator<String> inventoryKeys = inventory.keys();

                    while (inventoryKeys.hasNext()) {
                        JSONObject item = inventory.getJSONObject((String) inventoryKeys.next());
                        modelItemArrayList.add(new ModelItem(
                                BigInteger.valueOf(Long.parseLong(item.getString("classid"))),
                                BigInteger.valueOf(Long.parseLong(item.getString("id")))
                        ));
                    }

                    // Going through all descriptions:
                    JSONObject descriptions = inventoryEntry.getJSONObject("rgDescriptions");
                    Iterator<String> descriptionKeys = descriptions.keys();

                    while (descriptionKeys.hasNext()) {
                        JSONObject description = descriptions.getJSONObject((String) descriptionKeys.next());
                        modelDescriptionArrayList.add(new ModelDescription(
                                BigInteger.valueOf(Long.parseLong(description.getString("classid"))),
                                description.getString("market_hash_name"),
                                description.getInt("marketable"),
                                description.getInt("tradable"),
                                BigDecimal.valueOf(Long.parseLong(String.valueOf(description.get("lowest_price")))),
                                BigDecimal.valueOf(Long.parseLong(String.valueOf(description.get("median_price")))),
                                BigInteger.valueOf(Long.parseLong(description.getString("volume")))
                        ));
                    }

                    // Matching items with their description:
                    for (ModelDescription modelDescription : modelDescriptionArrayList) {
                        for (ModelItem modelItem : modelItemArrayList) {
                            if (modelDescription.getClassid().equals(modelItem.getClassid())) {
                                modelItem.setDescriptionModel(modelDescription);
                            }
                        }
                    }

                    return modelItemArrayList;
                }
            }
        } catch (IOException | JSONException e) {
            Toast.makeText(context, "Something with the file is wrong, retry or contact developer.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        return null;
    }

    public ModelInventory extractInventoryHistoryFromJsonFile(String filename) {

        // TODO: implement error handling

        Log.d("DEBUG: ", "Starting extractInventoryHistoryFromJsonFile now");

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
                    Iterator<String> inventoryEntryKeys = inventoryHistory.keys();

                    ModelInventory newInventory = new ModelInventory();
                    while (inventoryEntryKeys.hasNext()) {
                        JSONObject inventoryEntryJson = inventoryHistory.getJSONObject( (String) inventoryEntryKeys.next());

                        ArrayList<ModelItem> modelItems = new ArrayList<>();
                        ArrayList<ModelDescription> modelDescriptions = new ArrayList<>();

                        // Getting all items:
                        JSONObject rgInventory = inventoryEntryJson.getJSONObject("rgInventory");
                        Iterator<String> rgInventoryKeys = rgInventory.keys();

                        // Going through all items:
                        while (rgInventoryKeys.hasNext()) {
                            JSONObject newItem = rgInventory.getJSONObject((String) rgInventoryKeys.next());
                            modelItems.add(new ModelItem(
                                    BigInteger.valueOf(Long.parseLong(newItem.getString("classid"))),
                                    BigInteger.valueOf(Long.parseLong(newItem.getString("id")))
                            ));
                        }

                        // Getting all descriptions:
                        JSONObject rgDescriptions = inventoryEntryJson.getJSONObject("rgDescriptions");
                        Iterator<String> rgDescriptionsKeys = rgDescriptions.keys();

                        while (rgDescriptionsKeys.hasNext()) {
                            JSONObject newDescription = rgDescriptions.getJSONObject((String) rgDescriptionsKeys.next());
                            modelDescriptions.add(new ModelDescription(
                                    BigInteger.valueOf(Long.parseLong(newDescription.getString("classid"))),
                                    newDescription.getString("market_hash_name"),
                                    newDescription.getInt("marketable"),
                                    newDescription.getInt("tradable"),
                                    BigDecimal.valueOf(Long.parseLong(String.valueOf(newDescription.get("lowest_price")))),
                                    BigDecimal.valueOf(Long.parseLong(String.valueOf(newDescription.get("median_price")))),
                                    BigInteger.valueOf(Long.parseLong(newDescription.getString("volume")))
                            ));
                        }

                        // Matching items with their description:
                        for (ModelDescription modelDescription : modelDescriptions) {
                            for (ModelItem modelItem : modelItems) {
                                if (modelDescription.getClassid().equals(modelItem.getClassid())) {
                                    modelItem.setDescriptionModel(modelDescription);
                                }
                            }
                        }

                        ModelSnapshot newEntry = new ModelSnapshot(modelItems);
                        newInventory.addEntry(newEntry);
                    }

                    return newInventory;
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateInventory(String filename) {
        // TODO: Test the implementation:
        // TODO: Implement the following steps:
        // Use filename to open local json and load all previous entries into InventoryModel.
        ModelInventory modelInventory = extractInventoryHistoryFromJsonFile(filename);
        // Use the steamLink to get json for new entry.
        // TODO: Refactor the way we fetch data from steam.
        JSONObject newEntry;
        // Create new InventoryEntryModel from new json.
        // TODO: Refactor the way we create JSONObjects.
        ModelSnapshot snapshotModel = null;
        // Add new InventoryEntryModel to existing InventoryModel.
        modelInventory.addEntry(snapshotModel);
        // Convert existing InventoryModel into json.
        // TODO: Refactor the way we create JSONObjects.
        // Write json to local file.
    }

    public void downloadInventoryForSteamID(String steamID) {
        Log.d("DEBUG: ", "Starting downloadInventoryForSteamID now");
        // This function will create a new file with the interesting contents of a steam inventory.

        // Check for existing files:
        if (isInventoryFilePresent(steamID) != null) {
            // Read existing file and add new content:
            extractInventoryHistoryFromJsonFile(isInventoryFilePresent(steamID));
        } else {
            // create new file and add new content:
            getSteamInventoryFormURL(steamID);
        }
    }

    private void getSteamInventoryFormURL(String steamID) {
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
                        Toast.makeText(context, "HTTP-ErrorCode: " + error.networkResponse.statusCode, Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        );
        queue.add(jsonObjectRequest);
    }

    private void extractDataFromJson(JSONObject response, RequestQueue queue, String steamID) {
        Log.d("DEBUG: ", "Starting extractDataFromJson now");

        ArrayList<ModelItem> modelItemArrayList = new ArrayList<>();
        ArrayList<ModelDescription> modelDescriptionArrayList = new ArrayList<>();

        // Extracting Items and Descriptions from JSON into ModelLists:
        try {
            // Items:
            JSONObject inventory = response.getJSONObject("rgInventory");
            Iterator<String> inventoryKeys = inventory.keys();
            while (inventoryKeys.hasNext()) {
                JSONObject item = inventory.getJSONObject((String) inventoryKeys.next());
                modelItemArrayList.add(new ModelItem(
                        BigInteger.valueOf(Long.parseLong(item.getString("classid"))),
                        BigInteger.valueOf(Long.parseLong(item.getString("id")))
                ));
                Log.d("New Item:", "classid: " + String.valueOf(item.get("classid"))
                        + " id: " + String.valueOf(item.get("id"))
                );
            }

            // Descriptions:
            JSONObject descriptions = response.getJSONObject("rgDescriptions");
            Iterator<String> descriptionKeys = descriptions.keys();
            while (descriptionKeys.hasNext()) {
                JSONObject description = descriptions.getJSONObject((String) descriptionKeys.next());
                modelDescriptionArrayList.add(new ModelDescription(
                        BigInteger.valueOf(Long.parseLong(description.getString("classid"))),
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
            Toast.makeText(context, "Something with the received data is wrong, retry or contact developer", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        // Fetching Prices for Descriptions and matching ItemModels with their DescriptionModels:
        fetchPricesForDescriptions(queue, modelDescriptionArrayList, modelItemArrayList, steamID);
    }

    private void fetchPricesForDescriptions(RequestQueue queue, ArrayList<ModelDescription> modelDescriptionArrayList, ArrayList<ModelItem> modelItemArrayList, String steamID) {
        Log.d("DEBUG: ", "Starting fetchPricesForDescriptions now");

        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        for (ModelDescription modelDescription : modelDescriptionArrayList) {

            if (modelDescription.isMarketable()) {
                fetchPriceForMarketHashName(
                        queue,
                        new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                Log.d("Now getting response to :", "");
                                try {
                                    modelDescription.setLowestPrice(
                                            BigDecimal.valueOf(
                                                    Long.parseLong("+" + response.getString("lowest_price")
                                                            .replace(",", "")
                                                            .replace("€", "")
                                                            .replace("-", "0"))
                                            )

                                    );
                                    modelDescription.setVolume(
                                            BigInteger.valueOf(
                                                    Long.parseLong(
                                                            response.getString("volume")
                                                                    .replace(",", "")
                                                    )
                                            )
                                    );
                                    modelDescription.setMedianPrice(
                                            BigDecimal.valueOf(
                                                    Long.parseLong("+" + response.getString("median_price")
                                                            .replace(",", "")
                                                            .replace("€", "")
                                                            .replace("-", "0"))
                                            )

                                    );
                                    Log.d("lowest_price", String.valueOf(modelDescription.getLowestPrice()));
                                    Log.d("volume", String.valueOf(modelDescription.getVolume()));
                                    Log.d("median_price", String.valueOf(modelDescription.getMedianPrice()));
                                } catch (JSONException e) {
                                    Toast.makeText(context, "Something with the data was wrong, retry or contact developer", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }

                                if (modelDescription == modelDescriptionArrayList.get(modelDescriptionArrayList.size() - 1)) {
                                    createJsonFromData(modelDescriptionArrayList, modelItemArrayList, steamID);
                                }
                            }
                        },
                        modelDescription.getItemName()
                );
            } else {
                // If it is not marketable, set lowest_price, volume and median_price to 0.
                Log.d("Not marketable description: ", modelDescription.getItemName());
                modelDescription.setLowestPrice(BigDecimal.ZERO);
                modelDescription.setVolume(BigInteger.ZERO);
                modelDescription.setMedianPrice(BigDecimal.ZERO);
            }

            try {
                Thread.sleep(3500);
            } catch (InterruptedException e) {
                Toast.makeText(context, "Something highly unlikely happened, just retry.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    private void fetchPriceForMarketHashName(RequestQueue queue, VolleyCallback volleyCallback, String marketHashName) {

        String baseURL = "https://steamcommunity.com/market/priceoverview/?appid=730&currency=3&market_hash_name=";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                baseURL + marketHashName.replace(" ", "%20"),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        volleyCallback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error while fetching prices, please retry or contact developer", Toast.LENGTH_LONG).show();
                        Log.d("Error in getting prices", error.toString());
                    }
                }
        );
        queue.add(jsonObjectRequest);
    }

    private void createJsonFromData(ArrayList<ModelDescription> modelDescriptionArrayList, ArrayList<ModelItem> modelItemArrayList, String steamID) {
        Log.d("DEBUG: ", "Starting createJsonFromData now");

        // Creating the metadata object for the current inventory entry:
        ModelMetaData metadata = new ModelMetaData(LocalDateTime.now());

        try {
            JSONObject rgDescriptions = new JSONObject();
            JSONObject rgInventory = new JSONObject();

            for (ModelDescription modelDescription : modelDescriptionArrayList) {
                // Calculating the current inventory value and creating JSONObjects for items:
                for (ModelItem modelItem : modelItemArrayList) {
                    JSONObject newItemEntry = new JSONObject()
                            .put("classid", modelItem.getClassid())
                            .put("id", modelItem.getId());
                    rgInventory.put(String.valueOf(modelItemArrayList.indexOf(modelItem)), newItemEntry);
                    Log.d("New item: ", newItemEntry.toString());
                    // Calculating the current inventory value:
                    if (modelDescription.getClassid().equals(modelItem.getClassid())) {
                        metadata.addPortfolioValue(modelDescription.getLowestPrice());
                    }
                }

                // Creating the JSONObject
                JSONObject newDescriptionEntry = new JSONObject()
                        .put("classid", String.valueOf(modelDescription.getClassid()))
                        .put("market_hash_name", modelDescription.getItemName())
                        .put("lowest_price", modelDescription.getLowestPrice())
                        .put("volume", modelDescription.getVolume())
                        .put("median_price", modelDescription.getMedianPrice());

                if (modelDescription.isMarketable()) {
                    newDescriptionEntry.put("marketable", 1);
                } else {
                    newDescriptionEntry.put("marketable", 0);
                }

                if (modelDescription.isTradable()) {
                    newDescriptionEntry.put("tradable", 1);
                } else {
                    newDescriptionEntry.put("tradable", 0);
                }
                rgDescriptions.put(String.valueOf(modelDescription.getClassid()), newDescriptionEntry);
                Log.d("New description: ", newDescriptionEntry.toString());
            }

            JSONObject jsonMetadata = new JSONObject()
                    .put("inventoryValue", metadata.getPortfolioValue())
                    .put("dateOfEntry", metadata.getDateOfEntry());

            JSONObject newInventoryHistoryEntry = new JSONObject()
                    .put("metadata", jsonMetadata)
                    .put("rgInventory", rgInventory)
                    .put("rgDescriptions", rgDescriptions);

            JSONObject newEntry = new JSONObject().put(String.valueOf(LocalDateTime.now()), newInventoryHistoryEntry);

            writeJsonToLocalFile(newEntry, ("inv_" + steamID + ".json").replace("/", "_"));
        } catch (JSONException e) {
            Toast.makeText(context, "Something went wrong while storing local data, retry or contact developer", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void writeJsonToLocalFile(JSONObject jsonObject, String filename) {
        Log.d("DEBUG: ", "Starting writeJsonToLocalFile now");

        try {
            FileOutputStream fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

            outputStreamWriter.write(jsonObject.toString());
            Toast.makeText(context, "Local file created", Toast.LENGTH_SHORT).show();

            outputStreamWriter.flush();
            outputStreamWriter.close();
        } catch (IOException e) {
            Toast.makeText(context, "Something went wrong while writing to local file, retry or contact developer", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private String isInventoryFilePresent(String steamID) {
        // Checking for steamID-File
        File[] files = context.getFilesDir().listFiles();
        if (files.length != 0) {
            for (File file : files) {
                if (file.getName().contains(steamID)) {
                    return file.getName();
                }
            }
        }
        return null;
    }

    public interface VolleyCallback {
        void onSuccess(JSONObject response);
    }
}
