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
import com.example.steammarketapp.activities.DetailsInventoryFile;
import com.example.steammarketapp.data_models.ModelInventoryFile;

import java.util.ArrayList;

/*
Hallo ihr ganzen Spieltypen, spielt nicht soviel, sondern kümmert euch um eure Familien und ganz besonders um eure Mütter!!!
 */

public class AdapterInventoryFile extends RecyclerView.Adapter<AdapterInventoryFile.InventoryViewHolder> {

    private ArrayList<ModelInventoryFile> modelInventoryFileArrayList;
    private LayoutInflater layoutInflater;
    private Context context;

    public AdapterInventoryFile(Context context, ArrayList<ModelInventoryFile> modelInventoryFileArrayList) {
        this.modelInventoryFileArrayList = modelInventoryFileArrayList;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterInventoryFile.InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.inventoryfile_cardview, parent, false);
        return new InventoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterInventoryFile.InventoryViewHolder holder, int position) {
        ModelInventoryFile current = modelInventoryFileArrayList.get(position);
        holder.setData(current, position);
        holder.setListeners();
    }

    @Override
    public int getItemCount() {
        return modelInventoryFileArrayList.size();
    }

    public class InventoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textViewInventoryCard;

        ConstraintLayout constraintLayout;

        private int position;
        private ModelInventoryFile currentObject;

        public InventoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewInventoryCard   = (TextView) itemView.findViewById(R.id.textViewInventoryCard);
            constraintLayout        = (ConstraintLayout) itemView.findViewById(R.id.layoutInventoryCard);
        }

        public void setData(ModelInventoryFile currentObject, int position) {
            this.textViewInventoryCard.setText(currentObject.getSteamID()
                    .replace("inv_", "")
                    .replace(".json", "")
                    .replace("id_", "")
                    .replace("profiles_", "")
            );

            this.position = position;
            this.currentObject = currentObject;
        }

        public void setListeners() {
            constraintLayout.setOnClickListener(AdapterInventoryFile.InventoryViewHolder.this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, DetailsInventoryFile.class);
            intent.putExtra("inventory", currentObject.getFilename());
            context.startActivity(intent);
        }
    }
}
