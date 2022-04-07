package com.example.steammarketapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.steammarketapp.json_handler.InventoryHandler;
import com.example.steammarketapp.R;

public class ActivityItemList extends AppCompatActivity {

    // TODO: Check if this app conforms to Steam TOS at some point!!!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemlist_activity);

        InventoryHandler inventoryHandler = new InventoryHandler(this);
        String inventoryURL = "https://steamcommunity.com/id/" + getIntent().getExtras().getString("steamID") + "/inventory/json/730/2";

        Button itemlistButtonRefresh = (Button) findViewById(R.id.itemlistButtonRefresh);
        itemlistButtonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: reload the list here
            }
        });

        // TODO: Get Data from InventoryHandler and display it properly
        /*
        RecyclerView itemlistRecycleViewItemList = findViewById(R.id.itemlistRecycleViewItemList);
        ItemAdapter itemAdapter = new ItemAdapter(ItemListActivity.this, itemModelArrayList);
        itemlistRecycleViewItemList.setAdapter(itemAdapter);
        LinearLayoutManager eventLayoutManager = new LinearLayoutManager(ItemListActivity.this);
        eventLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        itemlistRecycleViewItemList.setLayoutManager(eventLayoutManager);
        itemlistRecycleViewItemList.setItemAnimator(new DefaultItemAnimator());
         */
    }
}
