package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class GroceryCategoryAdapter extends RecyclerView.Adapter<GroceryCategoryAdapter.ViewHolder> {

    private List<GroceryCategory> list;
    private OnCategoryClick listener;

    private static final String BASE_URL =
            "http://10.177.237.34/townride/images/";

    public interface OnCategoryClick {
        void onClick(int categoryId);
    }

    public GroceryCategoryAdapter(List<GroceryCategory> list,
                                  OnCategoryClick listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grocery_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        GroceryCategory category = list.get(position);

        holder.title.setText(category.getName());

        String imageUrl = BASE_URL + category.getImage();

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .into(holder.image);

        holder.itemView.setOnClickListener(v ->
                listener.onClick(category.getId()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imgGroceryCategory);
            title = itemView.findViewById(R.id.tvGroceryCategory);
        }
    }
}