package com.example.steammarketapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.steammarketapp.adapters.PortfolioFileAdapter;
import com.example.steammarketapp.data_models.PortfolioFileModel;
import com.example.steammarketapp.R;

import java.io.File;
import java.util.ArrayList;

public class SelectPortfolioActivity extends AppCompatActivity {

    RecyclerView portfolioRecyclerView;
    PortfolioFileAdapter portfolioFileAdapter;
    LinearLayoutManager portfolioLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectportfolio_activity);

        loadPortfolios();

        Button buttonAddPortfolio = (Button) findViewById(R.id.buttonAddPortfolio);
        buttonAddPortfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectPortfolioActivity.this, DownloadPortfolioActivity.class);
                startActivity(intent);
            }
        });

        Button buttonDeletePortfolioList = (Button) findViewById(R.id.buttonDeletePortfolioList);
        buttonDeletePortfolioList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File[] files = SelectPortfolioActivity.this.getFilesDir().listFiles();
                if (files.length != 0) {
                    for (File file : files) {
                        Log.d("Deleted: ", file.getAbsolutePath());
                        file.delete();
                    }
                }
                loadPortfolios();
            }
        });

        Button buttonRefreshPortfolioList = (Button) findViewById(R.id.buttonRefreshPortfolioList);
        buttonRefreshPortfolioList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPortfolios();
            }
        });

    }

    private void loadPortfolios() {
        portfolioRecyclerView = findViewById(R.id.recyclerViewDownloadedPortfolios);
        portfolioFileAdapter = new PortfolioFileAdapter(SelectPortfolioActivity.this, getPortfolioFiles());
        portfolioRecyclerView.setAdapter(portfolioFileAdapter);
        portfolioLayoutManager = new LinearLayoutManager(SelectPortfolioActivity.this);
        portfolioLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        portfolioRecyclerView.setLayoutManager(portfolioLayoutManager);
        portfolioRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private ArrayList<PortfolioFileModel> getPortfolioFiles() {
        File[] files = this.getFilesDir().listFiles();
        ArrayList<PortfolioFileModel> fileNames = new ArrayList<>();
        if (files.length != 0) {
            for (File file : files) {
                fileNames.add(new PortfolioFileModel(file.getName(), file.getName()));
            }
        }

        return fileNames;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPortfolios();
    }
}
