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
import com.example.steammarketapp.data_models.DescriptionModel;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

public class DescriptionAdapter extends RecyclerView.Adapter<DescriptionAdapter.DescriptionViewHolder> {

    private ArrayList<DescriptionModel> descriptions;
    private LayoutInflater layoutInflater;
    private Context context;

    public DescriptionAdapter(Context context, ArrayList<DescriptionModel> descriptions) {
        layoutInflater = LayoutInflater.from(context);
        this.descriptions = descriptions;
        this.context = context;
    }

    @NonNull
    @Override
    public DescriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_cardview, parent, false);
        return new DescriptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DescriptionViewHolder holder, int position) {
        DescriptionModel current = descriptions.get(position);
        holder.setData(current, position);
    }

    @Override
    public int getItemCount() {
        return descriptions.size();
    }

    public class DescriptionViewHolder extends RecyclerView.ViewHolder {

        private TextView name, volume, price, priceMedian, amount, priceTotal;

        private int position;
        private DescriptionModel currentObject;

        public DescriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            name        = (TextView) itemView.findViewById(R.id.itemCardNameValue);
            price       = (TextView) itemView.findViewById(R.id.itemCardPriceValue);
            priceMedian = (TextView) itemView.findViewById(R.id.itemCardMedianValue);
            volume      = (TextView) itemView.findViewById(R.id.itemCardVolumeValue);
            amount      = (TextView) itemView.findViewById(R.id.itemCardAmountValue);
            priceTotal  = (TextView) itemView.findViewById(R.id.itemCardPriceTotalValue);
        }

        public void setData(DescriptionModel currentObject, int position) {
            this.name.setText(currentObject.getItemName());
            this.price.setText(formatPrice(currentObject.getLowestPrice()));
            this.priceMedian.setText(formatPrice(currentObject.getMedianPrice()));
            this.volume.setText(String.valueOf(currentObject.getVolume()));
            this.amount.setText(String.valueOf(currentObject.getAmount()));
            this.priceTotal.setText(calcTotalPrice(currentObject.getAmount(), currentObject.getLowestPrice()));

            this.position = position;
            this.currentObject = currentObject;
        }

        private String formatPrice(BigDecimal cents) {
            return String.valueOf(cents.movePointLeft(2));
        }

        private String calcTotalPrice(BigInteger amount, BigDecimal price) {
            // Multiplying AND formatting:
            return new BigDecimal(amount).multiply(price).movePointLeft(2).toString();
        }
    }
}
