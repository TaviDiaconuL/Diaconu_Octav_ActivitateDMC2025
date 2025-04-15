package com.example.lab04;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ConducatorRecyclerAdapter extends RecyclerView.Adapter<ConducatorRecyclerAdapter.ConducatorViewHolder> {
    private ArrayList<Conducator> conducatori;

    public ConducatorRecyclerAdapter(ArrayList<Conducator> conducatori) {
        this.conducatori = conducatori;
    }

    @NonNull
    @Override
    public ConducatorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ConducatorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConducatorViewHolder holder, int position) {
        Conducator conducator = conducatori.get(position);
        holder.textView.setText(conducator.toString());
    }

    @Override
    public int getItemCount() {
        return conducatori.size();
    }

    public static class ConducatorViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ConducatorViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}