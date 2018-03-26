package com.bravvura.nestledtime.userstory.adapter;

import android.location.Address;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.model.googleaddress.GooglePlaceItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 28-02-2018.
 */

public class BaseTextAdapter extends RecyclerView.Adapter<BaseTextAdapter.TextViewHolder> {
    ArrayList<GooglePlaceItem> results = new ArrayList<>();

    public void setResults(List<GooglePlaceItem> results) {
        this.results.clear();
        this.results.addAll(results);
    }

    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_text, parent, false));
    }

    @Override
    public void onBindViewHolder(TextViewHolder holder, int position) {
        holder.textView.setText(results.get(position).getFormatted_address());
    }


    @Override
    public int getItemCount() {
        return this.results.size();
    }

    class TextViewHolder extends RecyclerView.ViewHolder {

        public final TextView textView;

        public TextViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
        }
    }
}
