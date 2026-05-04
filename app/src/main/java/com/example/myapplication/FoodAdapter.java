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

import java.util.List;

import com.bumptech.glide.Glide;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private List<FoodItem> list;

    private static final String BASE_URL =
            "http://10.177.237.34/townride/images/";

    public FoodAdapter(List<FoodItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        FoodItem item = list.get(position);

        holder.name.setText(item.getName());
        holder.price.setText("₹ " + item.getPrice());

        String imageName = item.getImage();

        if (imageName != null && !imageName.isEmpty()) {

            String imageUrl = BASE_URL + imageName;

            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
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
        TextView name, price;
        Button btnAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imgFood);
            name = itemView.findViewById(R.id.tvFoodName);
            price = itemView.findViewById(R.id.tvFoodPrice);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }
    }
}