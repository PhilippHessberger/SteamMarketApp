package com.example.steammarketapp.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.steammarketapp.R;
import com.example.steammarketapp.adapters.DescriptionAdapter;
import com.example.steammarketapp.data_models.DescriptionModel;
import com.example.steammarketapp.data_handler.PortfolioHandler;

import java.util.ArrayList;

public class PortfolioFileDetails extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventoryfile_details);

        String inventory = getIntent().getExtras().getString("inventory");

        PortfolioHandler portfolioHandler = new PortfolioHandler(PortfolioFileDetails.this);
        ArrayList<DescriptionModel> descriptions = portfolioHandler.extractLastSnapshot(inventory);

        RecyclerView inventoryDetailsRecyclerView = findViewById(R.id.inventoryDetailsRecyclerView);
        DescriptionAdapter descriptionAdapter = new DescriptionAdapter(PortfolioFileDetails.this, descriptions);
        inventoryDetailsRecyclerView.setAdapter(descriptionAdapter);
        LinearLayoutManager offerLayoutManager = new LinearLayoutManager(PortfolioFileDetails.this);
        offerLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        inventoryDetailsRecyclerView.setLayoutManager(offerLayoutManager);
        inventoryDetailsRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
