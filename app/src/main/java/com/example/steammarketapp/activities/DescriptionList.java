package com.example.steammarketapp.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.steammarketapp.R;
import com.example.steammarketapp.adapters.DescriptionAdapter;
import com.example.steammarketapp.data_handler.PortfolioHandler;
import com.example.steammarketapp.data_models.SnapshotModel;

public class DescriptionList extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_list);

        String filename = getIntent().getExtras().getString("portfolio_filename");
        int index = getIntent().getExtras().getInt("snapshot_index");

        PortfolioHandler portfolioHandler = new PortfolioHandler(DescriptionList.this);
        Log.d("DEBUG", "snapshot index:" + index);
        Log.d("DEBUG", "portfolio filename:" + filename);
        Log.d("Snapshots", portfolioHandler.loadPortfolio(filename).toString());
        SnapshotModel snapshot = portfolioHandler.loadPortfolio(filename).getSnapshots().get(index);

        RecyclerView inventoryDetailsRecyclerView = findViewById(R.id.inventoryDetailsRecyclerView);
        DescriptionAdapter descriptionAdapter = new DescriptionAdapter(DescriptionList.this, snapshot.getDescriptions());
        inventoryDetailsRecyclerView.setAdapter(descriptionAdapter);
        LinearLayoutManager offerLayoutManager = new LinearLayoutManager(DescriptionList.this);
        offerLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        inventoryDetailsRecyclerView.setLayoutManager(offerLayoutManager);
        inventoryDetailsRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
