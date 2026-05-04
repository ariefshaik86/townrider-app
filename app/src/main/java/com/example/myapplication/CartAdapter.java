package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<FoodItem> list;
    private CartUpdateListener listener;
    private Context context;

    public interface CartUpdateListener {
        void onCartUpdated();
    }

    public CartAdapter(List<FoodItem> list, Context context, CartUpdateListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        FoodItem item = list.get(position);

        holder.name.setText(item.getName());
        holder.price.setText("₹ " + item.getPrice());
        holder.quantity.setText(String.valueOf(item.getQuantity()));

        // PLUS BUTTON
        holder.btnPlus.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                list.get(currentPosition).increaseQuantity();
                notifyItemChanged(currentPosition);
                listener.onCartUpdated();
            }
        });

        // MINUS BUTTON
        holder.btnMinus.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {

                FoodItem currentItem = list.get(currentPosition);

                if (currentItem.getQuantity() > 1) {
                    currentItem.decreaseQuantity();
                    notifyItemChanged(currentPosition);
                } else {
                    list.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                    notifyItemRangeChanged(currentPosition, list.size());
                }

                listener.onCartUpdated();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, price, quantity;
        Button btnPlus, btnMinus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tvCartName);
            price = itemView.findViewById(R.id.tvCartPrice);
            quantity = itemView.findViewById(R.id.tvQuantity);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
        }
    }
}