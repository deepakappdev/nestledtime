package com.bravvura.nestledtime.worlds.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.firebase.model.WorldItem;

import java.util.List;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 24-03-2018.
 */

public class WorldListAdapter extends RecyclerView.Adapter<WorldListAdapter.MyViewHolder> {
    private final OnWorldSelectListener worldSelectListener;

    public interface OnWorldSelectListener{
        void onSelectWorld(WorldItem worldItem);
    }

    public WorldListAdapter(OnWorldSelectListener worldSelectListener) {
        this.worldSelectListener = worldSelectListener;
    }
    List<WorldItem> allWorlds;

    public void updateResults(List<WorldItem> allWorlds) {
        this.allWorlds = allWorlds;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_text, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.populateItem(allWorlds.get(position));

    }

    @Override
    public int getItemCount() {
        return allWorlds == null ? 0 : allWorlds.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;
        private WorldItem worldItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    worldSelectListener.onSelectWorld(worldItem);
                }
            });
        }

        public void populateItem(WorldItem worldItem) {
            this.worldItem = worldItem;
            textView.setText(worldItem.ownerName + " ("+worldItem.worldId+")");
        }
    }
}
