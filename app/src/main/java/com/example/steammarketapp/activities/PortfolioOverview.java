package com.example.steammarketapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.steammarketapp.R;
import com.example.steammarketapp.adapters.SnapshotAdapter;
import com.example.steammarketapp.data_handler.PortfolioHandler;
import com.example.steammarketapp.data_models.PortfolioModel;

public class PortfolioOverview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_overview);
        TextView textViewPortfolioNameValue = (TextView) findViewById(R.id.textViewPortfolioNameValue);
        TextView textViewPortfolioValueValue = (TextView) findViewById(R.id.textViewPortfolioValueValue);

        // Loading the Portfolio:
        String portfolioFilename = getIntent().getExtras().getString("portfolio_filename");
        PortfolioHandler handler = new PortfolioHandler(PortfolioOverview.this);
        PortfolioModel portfolio = handler.loadPortfolio(portfolioFilename);

        // Setting TextViews:
        textViewPortfolioValueValue.setText(
                String.valueOf(
                        portfolio.getSnapshots()
                                .get(portfolio.getSnapshots().size() - 1)
                                .getMetadata()
                                .getSnapshotValue()
                )
        );

        textViewPortfolioNameValue.setText(
                String.valueOf(
                        portfolio.getSteamID()
                )
        );

        Button buttonUpdatePortfolio = (Button) findViewById(R.id.buttonUpdatePortfolio);
        buttonUpdatePortfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.getSteamInventoryFormURL(portfolio);
            }
        });

        // Creating Snapshot-List with loaded Portfolio:
        RecyclerView recyclerView = findViewById(R.id.portfolioOverviewRecyclerView);
        SnapshotAdapter adapter = new SnapshotAdapter(PortfolioOverview.this, portfolio);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(PortfolioOverview.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}