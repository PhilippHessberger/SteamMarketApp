package com.example.steammarketapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class InventoryFileDetails extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventoryfile_details);

        String inventory = getIntent().getExtras().getString("inventory");

        InventoryHandler inventoryHandler = new InventoryHandler(InventoryFileDetails.this);
        ArrayList<ItemModel> itemModelArrayList = inventoryHandler.extractLastInventoryHistoryEntryFromJsonFile(inventory);

        RecyclerView inventoryDetailsRecyclerView = findViewById(R.id.inventoryDetailsRecyclerView);
        ItemAdapter itemAdapter = new ItemAdapter(InventoryFileDetails.this, itemModelArrayList);
        inventoryDetailsRecyclerView.setAdapter(itemAdapter);
        LinearLayoutManager offerLayoutManager = new LinearLayoutManager(InventoryFileDetails.this);
        offerLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        inventoryDetailsRecyclerView.setLayoutManager(offerLayoutManager);
        inventoryDetailsRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
