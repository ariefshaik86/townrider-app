package com.example.myapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Category> list;

    public CategoryAdapter(List<Category> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Category category = list.get(position);

        holder.title.setText(category.getName());
        String imageUrl = "http://10.177.237.34/townride/images/" + category.getImage();

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.image);

        holder.itemView.setOnClickListener(v -> {

            Intent intent = null;

            switch (category.getName().toLowerCase()) {

                case "food":
                    intent = new Intent(v.getContext(), FoodActivity.class);
                    break;

                case "grocery":
                    intent = new Intent(v.getContext(), GroceriesActivity.class);
                    break;

                case "chicken":
                    intent = new Intent(v.getContext(), ComingSoon.class);
                    break;

                case "parcel":
                    intent = new Intent(v.getContext(), ComingSoon.class);
                    break;

                case "pharmacy":
                    intent = new Intent(v.getContext(), ComingSoon.class);
                    break;

                case "services":
                    intent = new Intent(v.getContext(), ComingSoon.class);
                    break;

                case "rental":
                    intent = new Intent(v.getContext(), ComingSoon.class);
                    break;

                case "emergency":
                    intent = new Intent(v.getContext(), ComingSoon.class);
                    break;
            }

            if (intent != null) {
                intent.putExtra("category_id", category.getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imgIcon);
            title = itemView.findViewById(R.id.tvName);
        }
    }
}