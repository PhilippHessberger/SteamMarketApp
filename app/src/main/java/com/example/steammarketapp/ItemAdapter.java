package com.example.steammarketapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private ArrayList<ItemModel> itemModelArrayList;
    private LayoutInflater layoutInflater;
    private Context context;

    public ItemAdapter(Context context, ArrayList<ItemModel> itemModelArrayList) {
        this.itemModelArrayList = itemModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_cardview, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ItemModel current = itemModelArrayList.get(position);
        holder.setData(current, position);
    }

    @Override
    public int getItemCount() {
        return itemModelArrayList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView name, volume, price, priceMedian;

        private int position;
        private ItemModel currentObject;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name        = (TextView) itemView.findViewById(R.id.itemCardViewTextViewItemName);
            price       = (TextView) itemView.findViewById(R.id.itemCardViewTextViewItemPrice);
            priceMedian = (TextView) itemView.findViewById(R.id.itemCardViewTextViewItemPriceMedian);
            volume      = (TextView) itemView.findViewById(R.id.itemCardViewTextViewItemVolume);
        }

        public void setData(ItemModel currentObject, int position) {
            this.name.setText(currentObject.getDescriptionModel().getItemName());
            this.price.setText(String.valueOf(currentObject.getDescriptionModel().getPrice()));
            this.priceMedian.setText(String.valueOf(currentObject.getDescriptionModel().getMedianPrice()));
            this.volume.setText(currentObject.getDescriptionModel().getVolume());

            this.position = position;
            this.currentObject = currentObject;
        }
    }
}
