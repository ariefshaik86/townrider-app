package com.example.myapplication;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class SpecialOfferAdapter extends RecyclerView.Adapter<SpecialOfferAdapter.ViewHolder> {

    private List<SpecialOffer> list;
    private static final String IMAGE_BASE =
            "http://10.177.237.34/townride/images/";

    public SpecialOfferAdapter(List<SpecialOffer> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_special_offer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SpecialOffer offer = list.get(position);

        holder.name.setText(offer.getName());

        holder.originalPrice.setText("₹ " + offer.getOriginalPrice());
        holder.appPrice.setText("₹ " + offer.getAppPrice());

        // ✅ STRIKE ORIGINAL PRICE
        holder.originalPrice.setPaintFlags(
                holder.originalPrice.getPaintFlags()
                        | Paint.STRIKE_THRU_TEXT_FLAG
        );

        Glide.with(holder.itemView.getContext())
                .load(IMAGE_BASE + offer.getImage())
                .into(holder.image);

        // ✅ ADD BUTTON CLICK
        holder.btnAdd.setOnClickListener(v -> {
            Toast.makeText(holder.itemView.getContext(),
                    offer.getName() + " added to cart",
                    Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, originalPrice, appPrice;
        Button btnAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imgOffer);
            name = itemView.findViewById(R.id.tvOfferName);
            originalPrice = itemView.findViewById(R.id.tvOriginalPrice);
            appPrice = itemView.findViewById(R.id.tvAppPrice);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }
    }
}