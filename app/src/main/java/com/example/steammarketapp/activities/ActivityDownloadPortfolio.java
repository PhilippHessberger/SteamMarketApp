package com.example.steammarketapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.steammarketapp.InventoryHandler;
import com.example.steammarketapp.R;
import com.google.android.material.textfield.TextInputLayout;

public class ActivityDownloadPortfolio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.downloadportfolio_activity);

        TextInputLayout textInputLayoutEnterSteamID = (TextInputLayout) findViewById(R.id.textInputLayoutEnterSteamID);

        Button buttonGetPortfolioData = (Button) findViewById(R.id.buttonGetPortfolioData);
        buttonGetPortfolioData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textInputLayoutEnterSteamID.getEditText().getText() != null) {
                    // TODO: Now the data needs to be downloaded
                    Log.d("DEBUG: ", "Starting buttonGetPortfolioData onClick now");
                    InventoryHandler inventoryHandler = new InventoryHandler(ActivityDownloadPortfolio.this);
                    inventoryHandler.downloadInventoryForSteamID(String.valueOf(textInputLayoutEnterSteamID.getEditText().getText()));
                    finish();
                } else {
                    Toast.makeText(ActivityDownloadPortfolio.this, "Please enter a SteamID", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}