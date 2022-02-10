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

    public ArrayList<ItemModel> extractLastInventoryHistoryEntryFromJsonFile(String filename) {
        Log.d("DEBUG: ", "Starting extractLastInventoryHistoryEntryFromJsonFile now");

        // TODO: Technically it is the newest entry we extract... Fix it. Maybe...
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

                    // Going through all items:
                    JSONObject inventory = inventoryEntry.getJSONObject("rgInventory");
                    Iterator<String> inventoryKeys = inventory.keys();

                    while (inventoryKeys.hasNext()) {
                        JSONObject item = inventory.getJSONObject((String) inventoryKeys.next());
                        itemModelArrayList.add(new ItemModel(
                                BigInteger.valueOf(Long.parseLong(item.getString("classid"))),
                                BigInteger.valueOf(Long.parseLong(item.getString("id")))
                        ));
                    }

                    // Going through all descriptions:
                    JSONObject descriptions = inventoryEntry.getJSONObject("rgDescriptions");
                    Iterator<String> descriptionKeys = descriptions.keys();

                    while (descriptionKeys.hasNext()) {
                        JSONObject description = descriptions.getJSONObject((String) descriptionKeys.next());
                        descriptionModelArrayList.add(new DescriptionModel(
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
                    for (DescriptionModel descriptionModel : descriptionModelArrayList) {
                        for (ItemModel itemModel : itemModelArrayList) {
                            if (descriptionModel.getClassid().equals(itemModel.getClassid())) {
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

    public ArrayList<ItemModel>[] extractInventoryHistoryFromJsonFile(String filename) {
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
                    Iterator<String> inventoryEntries = inventoryHistory.keys();
                    JSONObject inventoryEntry = inventoryHistory.getJSONObject((String) inventoryEntries.next());

                    ArrayList<ItemModel> itemModelArrayList = new ArrayList<>();
                    ArrayList<DescriptionModel> descriptionModelArrayList = new ArrayList<>();

                    // Going through all items:
                    JSONObject inventory = inventoryEntry.getJSONObject("rgInventory");
                    Iterator<String> inventoryKeys = inventory.keys();

                    while (inventoryKeys.hasNext()) {
                        JSONObject item = inventory.getJSONObject((String) inventoryKeys.next());
                        itemModelArrayList.add(new ItemModel(
                                BigInteger.valueOf(Long.parseLong(item.getString("classid"))),
                                BigInteger.valueOf(Long.parseLong(item.getString("id")))
                        ));
                    }

                    // Going through all descriptions:
                    JSONObject descriptions = inventoryEntry.getJSONObject("rgDescriptions");
                    Iterator<String> descriptionKeys = descriptions.keys();

                    while (descriptionKeys.hasNext()) {
                        JSONObject description = descriptions.getJSONObject((String) descriptionKeys.next());
                        descriptionModelArrayList.add(new DescriptionModel(
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
                    for (DescriptionModel descriptionModel : descriptionModelArrayList) {
                        for (ItemModel itemModel : itemModelArrayList) {
                            if (descriptionModel.getClassid().equals(itemModel.getClassid())) {
                                itemModel.setDescriptionModel(descriptionModel);
                            }
                        }
                    }

                    return null;
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
                        error.printStackTrace();
                    }
                }
        );
        queue.add(jsonObjectRequest);
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
                descriptionModelArrayList.add(new DescriptionModel(
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
            e.printStackTrace();
        }

        // Fetching Prices for Descriptions and matching ItemModels with their DescriptionModels:
        fetchPricesForDescriptions(queue, descriptionModelArrayList, itemModelArrayList, steamID);
    }

    private void fetchPricesForDescriptions(RequestQueue queue, ArrayList<DescriptionModel> descriptionModelArrayList, ArrayList<ItemModel> itemModelArrayList, String steamID) {
        Log.d("DEBUG: ", "Starting fetchPricesForDescriptions now");

        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        Toast.makeText(context, "This can take some time...", Toast.LENGTH_LONG).show();

        for (DescriptionModel descriptionModel : descriptionModelArrayList) {

            if (descriptionModel.isMarketable()) {
                fetchPriceForMarketHashName(
                        queue,
                        new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                Log.d("Now getting response to :", "");
                                try {
                                    descriptionModel.setLowestPrice(
                                            roundPrice(
                                                    BigDecimal.valueOf(
                                                            Long.parseLong("+" + response.getString("lowest_price")
                                                                    .replace(",", "")
                                                                    .replace("€", "")
                                                                    .replace("-", "0"))
                                                    )
                                            )

                                    );
                                    descriptionModel.setVolume(
                                            BigInteger.valueOf(
                                                    Long.parseLong(
                                                            response.getString("volume")
                                                                    .replace(",", "")
                                                    )
                                            )
                                    );
                                    descriptionModel.setMedianPrice(
                                            roundPrice(
                                                    BigDecimal.valueOf(
                                                            Long.parseLong("+" + response.getString("median_price")
                                                                    .replace(",", "")
                                                                    .replace("€", "")
                                                                    .replace("-", "0"))
                                                    )
                                            )

                                    );
                                    Log.d("lowest_price", String.valueOf(descriptionModel.getLowestPrice()));
                                    Log.d("volume", String.valueOf(descriptionModel.getVolume()));
                                    Log.d("median_price", String.valueOf(descriptionModel.getMedianPrice()));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (descriptionModel == descriptionModelArrayList.get(descriptionModelArrayList.size() - 1)) {
                                    createJsonFromData(descriptionModelArrayList, itemModelArrayList, steamID);
                                }
                            }
                        },
                        descriptionModel.getItemName()
                );
            } else {
                // If it is not marketable, set lowest_price, volume and median_price to 0.
                Log.d("Not marketable description: ", descriptionModel.getItemName());
                descriptionModel.setLowestPrice(BigDecimal.ZERO);
                descriptionModel.setVolume(BigInteger.ZERO);
                descriptionModel.setMedianPrice(BigDecimal.ZERO);
            }

            try {
                Thread.sleep(3500);
            } catch (InterruptedException e) {
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
                        Log.d("Error in getting prices", error.toString());
                    }
                }
        );
        queue.add(jsonObjectRequest);
    }

    private void createJsonFromData(ArrayList<DescriptionModel> descriptionModelArrayList, ArrayList<ItemModel> itemModelArrayList, String steamID) {
        Log.d("DEBUG: ", "Starting createJsonFromData now");

        // Creating the metadata object for the current inventory entry:
        InvMetaDataModel metadata = new InvMetaDataModel(LocalDateTime.now());

        try {
            JSONObject rgDescriptions = new JSONObject();
            JSONObject rgInventory = new JSONObject();

            for (DescriptionModel descriptionModel : descriptionModelArrayList) {
                // Calculating the current inventory value and creating JSONObjects for items:
                for (ItemModel itemModel : itemModelArrayList) {
                    JSONObject newItemEntry = new JSONObject()
                            .put("classid", itemModel.getClassid())
                            .put("id", itemModel.getId());
                    rgInventory.put(String.valueOf(itemModelArrayList.indexOf(itemModel)), newItemEntry);
                    Log.d("New item: ", newItemEntry.toString());
                    // Calculating the current inventory value:
                    if (descriptionModel.getClassid().equals(itemModel.getClassid())) {
                        metadata.addPortfolioValue(descriptionModel.getLowestPrice());
                        // TODO: remove after testing:
                        Log.d("price ", String.valueOf(descriptionModel.getLowestPrice()));
                    }
                }

                // Creating the JSONObject
                JSONObject newDescriptionEntry = new JSONObject()
                        .put("classid", String.valueOf(descriptionModel.getClassid()))
                        .put("market_hash_name", descriptionModel.getItemName())
                        .put("lowest_price", roundPrice(descriptionModel.getLowestPrice()))
                        .put("volume", descriptionModel.getVolume())
                        .put("median_price", roundPrice(descriptionModel.getMedianPrice()));

                if (descriptionModel.isMarketable()) {
                    newDescriptionEntry.put("marketable", 1);
                } else {
                    newDescriptionEntry.put("marketable", 0);
                }

                if (descriptionModel.isTradable()) {
                    newDescriptionEntry.put("tradable", 1);
                } else {
                    newDescriptionEntry.put("tradable", 0);
                }
                rgDescriptions.put(String.valueOf(descriptionModel.getClassid()), newDescriptionEntry);
                Log.d("New description: ", newDescriptionEntry.toString());
            }

            JSONObject jsonMetadata = new JSONObject()
                    .put("inventoryValue", metadata.getPortfolioValue())
                    .put("dateOfEntry", metadata.getDateOfEntry());

            // TODO: remove after confirming functionality of metadata:
            Log.d("Metadata ", jsonMetadata.toString(4));

            JSONObject newInventoryHistoryEntry = new JSONObject()
                    .put("metadata", jsonMetadata)
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
        // TODO: Check: The last fifth of the string to check if it contains all descriptions:

        try {
            Log.d("last part of jsonObject", jsonObject.toString(4).substring(jsonObject.toString().length() /2));
            FileOutputStream fileOutputStream = context.openFileOutput(("inv_" + steamID + ".json").replace("/", "_"), context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

            outputStreamWriter.write(jsonObject.toString());
            Toast.makeText(context, "Local file created", Toast.LENGTH_SHORT).show();

            outputStreamWriter.flush();
            outputStreamWriter.close();
        } catch (IOException | JSONException e) {
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

    public static BigDecimal roundPrice(BigDecimal d) {
        // TODO: Check if this actually works this way too:
        BigDecimal bd;
        bd = d.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd;
    }
}
