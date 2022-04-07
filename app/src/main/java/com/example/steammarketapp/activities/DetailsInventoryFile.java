package com.example.steammarketapp.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.steammarketapp.R;
import com.example.steammarketapp.adapters.AdapterItem;
import com.example.steammarketapp.data_models.ModelItem;
import com.example.steammarketapp.json_handler.InventoryHandler;

import java.util.ArrayList;

public class DetailsInventoryFile extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventoryfile_details);

        String inventory = getIntent().getExtras().getString("inventory");

        InventoryHandler inventoryHandler = new InventoryHandler(DetailsInventoryFile.this);
        ArrayList<ModelItem> modelItemArrayList = inventoryHandler.extractLastInventoryHistoryEntryFromJsonFile(inventory);

        RecyclerView inventoryDetailsRecyclerView = findViewById(R.id.inventoryDetailsRecyclerView);
        AdapterItem adapterItem = new AdapterItem(DetailsInventoryFile.this, modelItemArrayList);
        inventoryDetailsRecyclerView.setAdapter(adapterItem);
        LinearLayoutManager offerLayoutManager = new LinearLayoutManager(DetailsInventoryFile.this);
        offerLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        inventoryDetailsRecyclerView.setLayoutManager(offerLayoutManager);
        inventoryDetailsRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
