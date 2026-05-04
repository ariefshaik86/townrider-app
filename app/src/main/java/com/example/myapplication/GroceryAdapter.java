package com.example.myapplication;

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

public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.ViewHolder> {

    private List<GroceryItem> list;

    private static final String BASE_URL =
            "http://10.177.237.34/townride/images/";

    public GroceryAdapter(List<GroceryItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grocery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        GroceryItem item = list.get(position);

        holder.title.setText(item.getName());
        holder.price.setText("₹ " + item.getPrice());

        String imageName = item.getImage();

        if (imageName != null && !imageName.isEmpty()) {

            Glide.with(holder.itemView.getContext())
                    .load(BASE_URL + imageName)
                    .into(holder.image);

        } else {
            holder.image.setImageResource(R.drawable.img);
        }

        holder.btnAdd.setOnClickListener(v -> {
            Toast.makeText(v.getContext(),
                    item.getName() + " added to cart",
                    Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, price;
        Button btnAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imgGrocery);
            title = itemView.findViewById(R.id.tvGroceryName);
            price = itemView.findViewById(R.id.tvGroceryPrice);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }
    }
}