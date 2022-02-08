package com.example.steammarketapp;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class InventoryDetails extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_details);

        String inventory = getIntent().getExtras().getString("inventory");

        InventoryHandler inventoryHandler = new InventoryHandler(InventoryDetails.this);
        ArrayList<ItemModel> itemModelArrayList = inventoryHandler.extractLastInventoryHistoryEntryFromJsonFile(inventory);

        RecyclerView inventoryDetailsRecyclerView = findViewById(R.id.inventoryDetailsRecyclerView);
        ItemAdapter itemAdapter = new ItemAdapter(InventoryDetails.this, itemModelArrayList);
        inventoryDetailsRecyclerView.setAdapter(itemAdapter);
        LinearLayoutManager offerLayoutManager = new LinearLayoutManager(InventoryDetails.this);
        offerLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        inventoryDetailsRecyclerView.setLayoutManager(offerLayoutManager);
        inventoryDetailsRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}