package com.example.steammarketapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class InventoryDetails extends AppCompatActivity {

    private String inventory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_details);
        this.inventory = getIntent().getExtras().getString("inventory");

        InventoryHandler inventoryHandler = new InventoryHandler(InventoryDetails.this);
        ArrayList<ItemModel> itemModelArrayList = inventoryHandler.extractInventoryHistoryFromJsonFile(inventory);

        RecyclerView inventoryDetailsRecyclerView = findViewById(R.id.inventoryDetailsRecyclerView);
        ItemAdapter itemAdapter = new ItemAdapter(InventoryDetails.this, itemModelArrayList);
        inventoryDetailsRecyclerView.setAdapter(itemAdapter);
        LinearLayoutManager offerLayoutManager = new LinearLayoutManager(InventoryDetails.this);
        offerLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        inventoryDetailsRecyclerView.setLayoutManager(offerLayoutManager);
        inventoryDetailsRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
