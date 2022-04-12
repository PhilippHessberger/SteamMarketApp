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
import com.example.steammarketapp.data_models.PortfolioModel;
import com.example.steammarketapp.data_models.SnapshotModel;

public class SnapshotAdapter extends RecyclerView.Adapter<SnapshotAdapter.SnapshotViewHolder> {

    private LayoutInflater layoutInflater;
    private PortfolioModel portfolio;
    private Context context;

    public SnapshotAdapter(Context context, PortfolioModel portfolio) {
        this.layoutInflater = LayoutInflater.from(context);
        this.portfolio = portfolio;
        this.context = context;
    }

    @NonNull
    @Override
    public SnapshotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.cardview_snapshot, parent, false);
        return new SnapshotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SnapshotViewHolder holder, int position) {
        SnapshotModel current = portfolio.getSnapshots().get(position);
        holder.setData(current, position);
        holder.setListener();
    }

    @Override
    public int getItemCount() {
        return portfolio.getSnapshots().size();
    }

    public class SnapshotViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textViewSnapshotIndexValue;
        private final TextView textViewSnapshotValueValue;

        ConstraintLayout constraintLayout;

        private int position;
        private SnapshotModel currentObject;

        public SnapshotViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout            = (ConstraintLayout) itemView.findViewById(R.id.layoutSnapshotCardview);
            textViewSnapshotIndexValue  = (TextView) itemView.findViewById(R.id.textViewSnapshotIndexValue);
            textViewSnapshotValueValue  = (TextView) itemView.findViewById(R.id.textViewSnapshotValueValue);
        }

        public void setData(SnapshotModel currentObject, int position) {
            // TODO: Write index to MetaData in PortfolioHandler!
            this.textViewSnapshotIndexValue.setText(String.valueOf(currentObject.getMetadata().getSnapshotIndex()));
            this.textViewSnapshotValueValue.setText(String.valueOf(currentObject.getMetadata().getSnapshotValue()));

            this.position = position;
            this.currentObject = currentObject;
        }

        public void setListener() {
            constraintLayout.setOnClickListener(SnapshotAdapter.SnapshotViewHolder.this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, DescriptionList.class);
            intent.putExtra("snapshot_index", currentObject.getMetadata().getSnapshotIndex());
            intent.putExtra("portfolio_filename", portfolio.getFilename());
            context.startActivity(intent);
        }
    }
}
