package com.example.steammarketapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class SelectPortfolioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectportfolio_activity);

        ListView listViewDownloadedPortfolios = (ListView) findViewById(R.id.listViewDownloadedPortfolios);
        TextView textViewInventoryCard = (TextView) findViewById(R.id.textViewInventoryCard);

        ArrayAdapter<String> fileNamesAdapter = new ArrayAdapter<String>(this, R.layout.inventory_cardview, R.id.textViewInventoryCard, getFileArrayNames());
        listViewDownloadedPortfolios.setAdapter(fileNamesAdapter);

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
                createAFile("test");
            }
        });
    }

    private String[] getFileArrayNames() throws NullPointerException {
        File[] files = this.getFilesDir().listFiles();
        String[] fileNames = new String[0];
        if (files != null) {
            fileNames = new String[files.length];
        }
        // TODO: Error-Handling maybe...
        if (files.length != 0) {
            for (int i = 0; i < files.length; i++) {
                fileNames[i] = files[i].getName();
            }
        }

        return fileNames;
    }

    // TODO: remove this later
    // for creating a few test files to check if the listview arrayadapter works:
    private void createAFile(String filename) {
        try {
            FileOutputStream fileOutputStream = openFileOutput(filename + ".json", MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

            outputStreamWriter.write("test");

            outputStreamWriter.flush();
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
