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

import com.example.steammarketapp.adapters.AdapterInventoryFile;
import com.example.steammarketapp.data_models.ModelInventoryFile;
import com.example.steammarketapp.R;

import java.io.File;
import java.util.ArrayList;

public class ActivitySelectPortfolio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectportfolio_activity);

        loadInventories();

        Button buttonGoToDownloadPortfolioActivity = (Button) findViewById(R.id.buttonGoToDownloadPortfolioActivity);
        buttonGoToDownloadPortfolioActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivitySelectPortfolio.this, ActivityDownloadPortfolio.class);
                startActivity(intent);
            }
        });

        Button button = (Button) findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: clean up testing code:
                File[] files = ActivitySelectPortfolio.this.getFilesDir().listFiles();
                if (files.length != 0) {
                    for (File file : files) {
                        Log.d("Deleted: ", file.getAbsolutePath());
                        file.delete();
                    }
                }
            }
        });

    }

    private void loadInventories() {
        RecyclerView recyclerViewDownloadedPortfolios = findViewById(R.id.recyclerViewDownloadedPortfolios);
        AdapterInventoryFile adapterInventoryFile = new AdapterInventoryFile(ActivitySelectPortfolio.this, getInventroyModels());
        recyclerViewDownloadedPortfolios.setAdapter(adapterInventoryFile);
        LinearLayoutManager offerLayoutManager = new LinearLayoutManager(ActivitySelectPortfolio.this);
        offerLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewDownloadedPortfolios.setLayoutManager(offerLayoutManager);
        recyclerViewDownloadedPortfolios.setItemAnimator(new DefaultItemAnimator());
    }

    private ArrayList<ModelInventoryFile> getInventroyModels() {
        File[] files = this.getFilesDir().listFiles();
        ArrayList<ModelInventoryFile> fileNames = new ArrayList<>();
        if (files.length != 0) {
            for (File file : files) {
                fileNames.add(new ModelInventoryFile(file.getName(), file.getName()));
            }
        }

        return fileNames;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadInventories();
    }
}
