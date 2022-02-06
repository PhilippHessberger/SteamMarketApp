package com.example.steammarketapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class SelectPortfolioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectportfolio_activity);

        loadInventories();

        Button buttonGoToDownloadPortfolioActivity = (Button) findViewById(R.id.buttonGoToDownloadPortfolioActivity);
        buttonGoToDownloadPortfolioActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectPortfolioActivity.this, DownloadPortfolioActivity.class);
                startActivity(intent);
            }
        });

        Button button = (Button) findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: clean up testing code:
                File[] files = SelectPortfolioActivity.this.getFilesDir().listFiles();
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
        InventoryAdapter inventoryAdapter = new InventoryAdapter(SelectPortfolioActivity.this, getInventroyModels());
        recyclerViewDownloadedPortfolios.setAdapter(inventoryAdapter);
        LinearLayoutManager offerLayoutManager = new LinearLayoutManager(SelectPortfolioActivity.this);
        offerLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewDownloadedPortfolios.setLayoutManager(offerLayoutManager);
        recyclerViewDownloadedPortfolios.setItemAnimator(new DefaultItemAnimator());
    }

    private ArrayList<InventoryModel> getInventroyModels() {
        File[] files = this.getFilesDir().listFiles();
        ArrayList<InventoryModel> fileNames = new ArrayList<>();
        if (files.length != 0) {
            for (File file : files) {
                fileNames.add(new InventoryModel(file.getName(), file.getName()));
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
