package com.example.steammarketapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.steammarketapp.R;
import com.example.steammarketapp.data_models.ModelItem;

import java.util.ArrayList;

public class AdapterItem extends RecyclerView.Adapter<AdapterItem.ItemViewHolder> {

    private ArrayList<ModelItem> modelItemArrayList;
    private LayoutInflater layoutInflater;
    private Context context;

    public AdapterItem(Context context, ArrayList<ModelItem> modelItemArrayList) {
        layoutInflater = LayoutInflater.from(context);
        this.modelItemArrayList = modelItemArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_cardview, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ModelItem current = modelItemArrayList.get(position);
        holder.setData(current, position);
    }

    @Override
    public int getItemCount() {
        return modelItemArrayList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView name, volume, price, priceMedian;

        private int position;
        private ModelItem currentObject;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name        = (TextView) itemView.findViewById(R.id.itemCardViewTextViewItemName);
            price       = (TextView) itemView.findViewById(R.id.itemCardViewTextViewItemPrice);
            priceMedian = (TextView) itemView.findViewById(R.id.itemCardViewTextViewItemPriceMedian);
            volume      = (TextView) itemView.findViewById(R.id.itemCardViewTextViewItemVolume);
        }

        public void setData(ModelItem currentObject, int position) {
            Log.d("currentObject" + currentObject.getId(), currentObject.getDescriptionModel().getItemName());
            this.name.setText(currentObject.getDescriptionModel().getItemName());
            this.price.setText(String.valueOf(currentObject.getDescriptionModel().getLowestPrice()));
            this.priceMedian.setText(String.valueOf(currentObject.getDescriptionModel().getMedianPrice()));
            this.volume.setText(String.valueOf(currentObject.getDescriptionModel().getVolume()));

            this.position = position;
            this.currentObject = currentObject;
        }
    }
}
