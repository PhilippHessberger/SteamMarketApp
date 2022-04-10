package com.example.steammarketapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.steammarketapp.data_handler.PortfolioHandler;
import com.example.steammarketapp.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

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
                if (Objects.requireNonNull(textInputLayoutEnterSteamID.getEditText()).getText() != null) {
                    PortfolioHandler portfolioHandler = new PortfolioHandler(DownloadPortfolioActivity.this);
                    portfolioHandler.createPortfolio(String.valueOf(textInputLayoutEnterSteamID.getEditText().getText()));
                    //finish();
                } else {
                    Toast.makeText(DownloadPortfolioActivity.this, "Please enter a SteamID", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}