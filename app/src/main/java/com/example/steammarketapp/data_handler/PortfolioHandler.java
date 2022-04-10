package com.example.steammarketapp.data_handler;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.steammarketapp.data_models.DescriptionModel;
import com.example.steammarketapp.data_models.MetaDataModel;
import com.example.steammarketapp.data_models.PortfolioModel;
import com.example.steammarketapp.data_models.ItemModel;
import com.example.steammarketapp.data_models.SnapshotModel;

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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class PortfolioHandler {

    private final Context context;

    public PortfolioHandler(Context context) {
        this.context = context;
    }

    // Methods for downloading .json and creating new portfolio:
    public void createPortfolio(String steamID) {
        Log.d("DEBUG: ", "Starting downloadInventoryForSteamID now");
        // This function will create a new file with the contents of a steam inventory.

        // Check for existing files:
        if (isInventoryFilePresent(steamID) != null) {
            Toast.makeText(context, "There is already a Scanned Portfolio of this Inventory.", Toast.LENGTH_LONG).show();
        } else {
            // create new file and add new content:
            PortfolioModel portfolio = new PortfolioModel(steamID);
            getSteamInventoryFormURL(portfolio);
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

    private void getSteamInventoryFormURL(PortfolioModel portfolio) {
        // Sends a request for the inventory of the steamID to steam and hands on the response.
        Log.d("DEBUG: ", "Starting getSteamInventoryFormURL now");

        String inventoryURL = "https://steamcommunity.com/" + portfolio.getSteamID() + "/inventory/json/730/2";

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, inventoryURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Call method to extract data from json:
                        extractDataFromJson(response, queue, portfolio);
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

    private void extractDataFromJson(JSONObject response, RequestQueue queue, PortfolioModel portfolio) {
        // Input is the JSONObject of a Steam-Inventory. Creates Model-Lists from the Json.
        Log.d("DEBUG: ", "Starting extractDataFromJson now");

        ArrayList<ItemModel> items = new ArrayList<>();
        ArrayList<DescriptionModel> descriptions = new ArrayList<>();

        // Extracting Items and Descriptions from JSON into ModelLists:
        try {
            // Items:
            JSONObject inventory = response.getJSONObject("rgInventory");
            Iterator<String> inventoryKeys = inventory.keys();
            while (inventoryKeys.hasNext()) {
                JSONObject item = inventory.getJSONObject((String) inventoryKeys.next());
                items.add(new ItemModel(
                        BigInteger.valueOf(Long.parseLong(item.getString("classid"))),
                        BigInteger.valueOf(Long.parseLong(item.getString("id")))
                ));
            }

            // Descriptions:
            JSONObject JSONDescriptions = response.getJSONObject("rgDescriptions");
            Iterator<String> descriptionKeys = JSONDescriptions.keys();
            while (descriptionKeys.hasNext()) {
                JSONObject description = JSONDescriptions.getJSONObject((String) descriptionKeys.next());
                descriptions.add(new DescriptionModel(
                        BigInteger.valueOf(Long.parseLong(description.getString("classid"))),
                        description.getString("market_hash_name"),
                        description.getInt("marketable"),
                        description.getInt("tradable")
                ));
            }
        } catch (JSONException e) {
            Toast.makeText(context, "Something with the received data is wrong, retry or contact developer", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        // Counting amount of items per description:
        matchItemsAndDescriptions(descriptions, items);
        // Fetching Prices for Descriptions:
        fetchPricesForDescriptions(queue, descriptions, portfolio);
    }

    private void matchItemsAndDescriptions(ArrayList<DescriptionModel> descriptions, ArrayList<ItemModel> items) {
        // Adds 1 to Description.amount for each Item with the same Classid as the Description.
        for (DescriptionModel descriptionModel : descriptions) {
            for (ItemModel itemModel : items) {
                if (descriptionModel.getClassid().equals(itemModel.getClassid())) {
                    descriptionModel.addOne();
                    Log.d("DEBUG: ", String.valueOf(descriptionModel.getAmount()));
                }
            }
        }
    }

    private void fetchPricesForDescriptions(RequestQueue queue, ArrayList<DescriptionModel> descriptions, PortfolioModel portfolio) {
        Log.d("DEBUG: ", "Starting fetchPricesForDescriptions now");

        for (DescriptionModel descriptionModel : descriptions) {

            if (descriptionModel.isMarketable()) {
                fetchPriceForMarketHashName(
                        queue,
                        new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                try {
                                    descriptionModel.setLowestPrice(
                                            BigDecimal.valueOf(
                                                    Long.parseLong("+" + response.getString("lowest_price")
                                                            .replace(",", "")
                                                            .replace("€", "")
                                                            .replace("-", "0"))
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
                                            BigDecimal.valueOf(
                                                    Long.parseLong("+" + response.getString("median_price")
                                                            .replace(",", "")
                                                            .replace("€", "")
                                                            .replace("-", "0"))
                                            )
                                    );
                                } catch (JSONException e) {
                                    Toast.makeText(context, "Something with the data was wrong, retry or contact developer", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }

                                // TODO: Is not yet displayed (maybe because the change of context?)
                                Toast.makeText(context, ((descriptions.size() - descriptions.indexOf(descriptionModel)) * 4) + " secs estimated time left", Toast.LENGTH_SHORT).show();

                                if (descriptionModel == descriptions.get(descriptions.size() - 1)) {
                                    portfolio.addSnapshot(new SnapshotModel(descriptions));
                                    writeJsonToLocalFile(Objects.requireNonNull(createJsonFromData(portfolio)), portfolio);
                                }
                            }
                        },
                        descriptionModel.getItemName()
                );
            } else {
                // If it is not marketable, set lowest_price, volume and median_price to 0.
                descriptionModel.setLowestPrice(BigDecimal.ZERO);
                descriptionModel.setVolume(BigInteger.ZERO);
                descriptionModel.setMedianPrice(BigDecimal.ZERO);
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

    private void writeJsonToLocalFile(JSONObject jsonObject, PortfolioModel newPortfolio) {
        Log.d("DEBUG: ", "Starting writeJsonToLocalFile now");

        if (isInventoryFilePresent(newPortfolio.getSteamID()) != null) {
            Log.d("DEBUG", "Updating existing portfolio.");

            // Portfolio from method parameters is just the new snapshot, the old snapshots need to be loaded, added and then saved in a file.
            updatePortfolio(newPortfolio.getFilename());
            PortfolioModel oldPortfolio = loadPortfolio(newPortfolio.getFilename());
            for (SnapshotModel snapshot : newPortfolio.getSnapshots()) {
                oldPortfolio.addSnapshot(snapshot);
            }
        }

        try {
            FileOutputStream fileOutputStream = context.openFileOutput(newPortfolio.getFilename(), Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

            outputStreamWriter.write(jsonObject.toString());
            Toast.makeText(context, "Local file created", Toast.LENGTH_SHORT).show();

            outputStreamWriter.flush();
            outputStreamWriter.close();
        } catch (IOException e) {
            Toast.makeText(context, "Something went wrong while writing to local file, retry or contact developer", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        ((Activity) context).finish();
    }

    private JSONObject createJsonFromData(PortfolioModel portfolio) {
        Log.d("DEBUG: ", "Starting createJsonFromData now");

        // Creates a JSONObject with all descriptions and metadata.
        MetaDataModel metadata = new MetaDataModel(LocalDateTime.now());

        try {
            JSONObject JSONPortfolio = new JSONObject();

            for (SnapshotModel snapshot : portfolio.getSnapshots()) {
                JSONObject JSONDescriptions = new JSONObject();

                MetaDataModel metaData = new MetaDataModel(LocalDateTime.now());

                for (DescriptionModel description : snapshot.getDescriptions()) {
                    metaData.addSnapshotValue(description.getLowestPrice().multiply(new BigDecimal(description.getAmount())));

                    // Creating the JSONObject
                    JSONObject newDescription = new JSONObject()
                            .put("classid", String.valueOf(description.getClassid()))
                            .put("market_hash_name", description.getItemName())
                            .put("lowest_price", description.getLowestPrice())
                            .put("volume", description.getVolume())
                            .put("median_price", description.getMedianPrice())
                            .put("amount", description.getAmount());

                    if (description.isMarketable()) {
                        newDescription.put("marketable", 1);
                    } else {
                        newDescription.put("marketable", 0);
                    }

                    if (description.isTradable()) {
                        newDescription.put("tradable", 1);
                    } else {
                        newDescription.put("tradable", 0);
                    }

                    JSONDescriptions.put(String.valueOf(description.getClassid()), newDescription);
                }

                JSONObject JSONMetaData = new JSONObject()
                        .put("snapshotValue", metaData.getSnapshotValue())
                        .put("dateOfEntry", metaData.getDateOfEntry());

                JSONObject JSONSnapshot = new JSONObject()
                        .put("JSONDescriptions", JSONDescriptions)
                        .put("MetaData", JSONMetaData);

                JSONPortfolio.put(String.valueOf(portfolio.getSnapshots().indexOf(snapshot)), JSONSnapshot);
            }

            return JSONPortfolio;

            /*
            JSONObject JSONDescriptions = new JSONObject();

            // Calculating current portfolio value:
            for (DescriptionModel description : descriptions) {
                metadata.addSnapshotValue(description.getLowestPrice().multiply(new BigDecimal(description.getAmount())));

                // Creating the JSONObject
                JSONObject newDescription = new JSONObject()
                        .put("classid", String.valueOf(description.getClassid()))
                        .put("market_hash_name", description.getItemName())
                        .put("lowest_price", description.getLowestPrice())
                        .put("volume", description.getVolume())
                        .put("median_price", description.getMedianPrice())
                        .put("amount", description.getAmount());

                if (description.isMarketable()) {
                    newDescription.put("marketable", 1);
                } else {
                    newDescription.put("marketable", 0);
                }

                if (description.isTradable()) {
                    newDescription.put("tradable", 1);
                } else {
                    newDescription.put("tradable", 0);
                }

                JSONDescriptions.put(String.valueOf(description.getClassid()), newDescription);
            }

            JSONObject jsonMetadata = new JSONObject()
                    .put("snapshotValue", metadata.getSnapshotValue())
                    .put("dateOfEntry", metadata.getDateOfEntry());

            JSONObject snapshot = new JSONObject()
                    .put("metadata", jsonMetadata)
                    .put("JSONDescriptions", JSONDescriptions);

            return new JSONObject().put(String.valueOf(LocalDateTime.now()), snapshot);
             */

        } catch (JSONException e) {
            Toast.makeText(context, "Something went wrong while storing local data, retry or contact developer", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return null;
        }
    }

    // Methods for reading saved .json and parsing for adapter:
    public SnapshotModel loadLastSnapshot(String filename) {
        Log.d("DEBUG: ", "Starting extractLastInventoryHistoryEntryFromJsonFile now");

        // TODO: Don't return descriptions, but a snapshot instead.
        // Finding the correct file:
        try {
            File[] files = context.getFilesDir().listFiles();
            for (File file : files) {
                if (file.getName().equals(filename)) {

                    // Getting ready to read:
                    FileInputStream fileInputStream = context.openFileInput(file.getName());
                    int size = fileInputStream.available();
                    char[] inputBuffer = new char[size];
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                    inputStreamReader.read(inputBuffer);
                    String fileToString = new String(inputBuffer);

                    // Extracting the first entry from the file with the help of keys-Iterator:
                    JSONObject JSONPortfolio = new JSONObject(fileToString);
                    Iterator<String> snapshotKeys = JSONPortfolio.keys();
                    JSONObject JSONSnapshot = JSONPortfolio.getJSONObject((String) snapshotKeys.next());

                    // Iterating through the descriptions and creating representing model instances:
                    ArrayList<DescriptionModel> descriptions = new ArrayList<>();
                    JSONObject JSONDescriptions = JSONSnapshot.getJSONObject("JSONDescriptions");
                    Iterator<String> descriptionKeys = JSONDescriptions.keys();

                    while (descriptionKeys.hasNext()) {
                        JSONObject JSONDescription = JSONDescriptions.getJSONObject((String) descriptionKeys.next());
                        descriptions.add(new DescriptionModel(
                                BigInteger.valueOf(Long.parseLong(JSONDescription.getString("classid"))),
                                JSONDescription.getString("market_hash_name"),
                                JSONDescription.getInt("marketable"),
                                JSONDescription.getInt("tradable"),
                                BigDecimal.valueOf(Long.parseLong(String.valueOf(JSONDescription.get("lowest_price")))),
                                BigDecimal.valueOf(Long.parseLong(String.valueOf(JSONDescription.get("median_price")))),
                                BigInteger.valueOf(Long.parseLong(JSONDescription.getString("volume"))),
                                BigInteger.valueOf(Long.parseLong(JSONDescription.getString("amount")))
                        ));
                    }

                    // Extracting MetaData:
                    JSONObject JSONMetaData = JSONSnapshot.getJSONObject("MetaData");
                    MetaDataModel metaData = new MetaDataModel(LocalDateTime.parse(JSONMetaData.getString("dateOfEntry")));
                    metaData.setSnapshotValue(new BigDecimal(JSONMetaData.getString("snapshotValue")));
                    SnapshotModel snapshot = new SnapshotModel(filterNotMarketable(descriptions));
                    snapshot.setMetadata(metaData);

                    return snapshot;
                }
            }
        } catch (IOException | JSONException e) {
            Toast.makeText(context, "Something with the file is wrong, retry or contact developer.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        return null;
    }

    private ArrayList<DescriptionModel> filterNotMarketable(ArrayList<DescriptionModel> descriptions) {
        // Didn't write it like this, but it is supposed to delete all non-marketable descriptions:
        descriptions.removeIf(description -> !description.isMarketable());
        return descriptions;
    }

    // Methods for reading saved .json and updating it:
    public PortfolioModel loadPortfolio(String filename) {
        Log.d("DEBUG", "Starting loadPortfolio now");

        // Getting the file:
        try {
            File[] files = context.getFilesDir().listFiles();
            for (File file : files) {
                if (file.getName().equals(filename)) {

                    // Getting ready to read:
                    FileInputStream fileInputStream = context.openFileInput(file.getName());
                    int size = fileInputStream.available();
                    char[] inputBuffer = new char[size];
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                    inputStreamReader.read(inputBuffer);
                    String fileToString = new String(inputBuffer);

                    // Convert from String to JSONObjects to DataModels:
                    PortfolioModel portfolio = new PortfolioModel(filename);
                    JSONObject JSONPortfolio = new JSONObject(fileToString);
                    Iterator<String> snapshotKeys = JSONPortfolio.keys();

                    // Iterating over all snapshots:
                    while (snapshotKeys.hasNext()) {
                        JSONObject JSONSnapshot = JSONPortfolio.getJSONObject(snapshotKeys.next());
                        Iterator<String> descriptionKeys = JSONSnapshot.keys();

                        ArrayList<DescriptionModel> descriptions = new ArrayList<>();

                        // Iterating over all descriptions of current snapshot:
                        while (descriptionKeys.hasNext()) {
                            JSONObject JSONDescription = JSONSnapshot.getJSONObject(descriptionKeys.next());
                            descriptions.add(new DescriptionModel(
                                    BigInteger.valueOf(Long.parseLong(JSONDescription.getString("classid"))),
                                    JSONDescription.getString("market_hash_name"),
                                    JSONDescription.getInt("marketable"),
                                    JSONDescription.getInt("tradable"),
                                    BigDecimal.valueOf(Long.parseLong(String.valueOf(JSONDescription.get("lowest_price")))),
                                    BigDecimal.valueOf(Long.parseLong(String.valueOf(JSONDescription.get("median_price")))),
                                    BigInteger.valueOf(Long.parseLong(JSONDescription.getString("volume"))),
                                    BigInteger.valueOf(Long.parseLong(JSONDescription.getString("amount")))
                            ));
                        }
                        portfolio.addSnapshot(new SnapshotModel(descriptions));
                    }
                    return portfolio;
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Unused methods:
    public void updatePortfolio(String filename) {
        // TODO: implement comments:
        // Use filename to open local json and load all previous entries into PortfolioModel.
        PortfolioModel portfolio = loadPortfolio(filename);
        // Use the steamLink to get json for new entry.
        getSteamInventoryFormURL(portfolio);
        // Create new InventoryEntryModel from new json.
        // Add new InventoryEntryModel to existing InventoryModel.
        // Convert existing InventoryModel into json.
        // Write json to local file.
    }

    // I hate this method:
    public interface VolleyCallback {
        void onSuccess(JSONObject response);
    }
}
