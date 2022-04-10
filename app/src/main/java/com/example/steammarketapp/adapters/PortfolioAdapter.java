package com.example.steammarketapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.steammarketapp.R;
import com.example.steammarketapp.activities.DescriptionList;
import com.example.steammarketapp.data_models.PortfolioFileModel;

import java.io.File;
import java.util.ArrayList;

/*
Hallo ihr ganzen Spieltypen, spielt nicht soviel, sondern kümmert euch um eure Familien und ganz besonders um eure Mütter!!!
    -My Mom
 */

public class PortfolioAdapter extends RecyclerView.Adapter<PortfolioAdapter.InventoryViewHolder> {

    private final File[] files;
    private LayoutInflater layoutInflater;
    private Context context;

    public PortfolioAdapter(Context context, File[] files) {
        this.files = files;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public PortfolioAdapter.InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.cardview_portfolio, parent, false);
        return new InventoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PortfolioAdapter.InventoryViewHolder holder, int position) {
        File current = files[position];
        holder.setData(current, position);
        holder.setListeners();
    }

    @Override
    public int getItemCount() {
        return files.length;
    }

    public class InventoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textViewInventoryCard;

        ConstraintLayout constraintLayout;

        private int position;
        private File currentObject;

        public InventoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewInventoryCard   = (TextView) itemView.findViewById(R.id.textViewInventoryCard);
            constraintLayout        = (ConstraintLayout) itemView.findViewById(R.id.layoutInventoryCard);
        }

        public void setData(File currentObject, int position) {
            this.textViewInventoryCard.setText(currentObject.getName()
                    .replace("portfolio_", "")
                    .replace("id-", "")
                    .replace("profiles-", "")
                    .replace(".json", "")
            );

            this.position = position;
            this.currentObject = currentObject;
        }

        public void setListeners() {
            constraintLayout.setOnClickListener(PortfolioAdapter.InventoryViewHolder.this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, DescriptionList.class);
            intent.putExtra("inventory", currentObject.getName());
            context.startActivity(intent);
        }
    }
}
