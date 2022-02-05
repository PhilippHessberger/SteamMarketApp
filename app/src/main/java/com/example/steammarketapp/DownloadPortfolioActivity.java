package com.example.steammarketapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class DownloadPortfolioActivity extends AppCompatActivity {

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

                    finish();
                } else {
                    Toast.makeText(DownloadPortfolioActivity.this, "Please enter a SteamID", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}