package com.bravvura.nestledtime.worlds.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.firebase.model.MemoryItem;

import java.util.List;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 24-03-2018.
 */

public class MemoryListAdapter extends RecyclerView.Adapter<MemoryListAdapter.MyViewHolder> {
    private final OnMemorySelectListener memorySelectListener;

    public interface OnMemorySelectListener {
        void onSelectMemory(MemoryItem memoryItem);
    }

    public MemoryListAdapter(OnMemorySelectListener memorySelectListener) {
        this.memorySelectListener = memorySelectListener;
    }

    List<MemoryItem> memoryItems;

    public void updateResults(List<MemoryItem> allMemories) {
        this.memoryItems = allMemories;
        notifyDataSetChanged();
    }

    @Override
    public MemoryListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MemoryListAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_text, parent, false));
    }

    @Override
    public void onBindViewHolder(MemoryListAdapter.MyViewHolder holder, int position) {
        holder.populateItem(memoryItems.get(position));

    }

    @Override
    public int getItemCount() {
        return memoryItems == null ? 0 : memoryItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;
        private MemoryItem memoryItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    memorySelectListener.onSelectMemory(memoryItem);
                }
            });
        }

        public void populateItem(MemoryItem memoryItem) {
            this.memoryItem = memoryItem;
            textView.setText(memoryItem.title + " ("+memoryItem.memoryId+")");
        }
    }
}
