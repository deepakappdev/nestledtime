package com.bravvura.nestledtime.worlds.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bravvura.nestledtime.MemoryDetailActivity;
import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.firebase.OnValueEventListener;
import com.bravvura.nestledtime.firebase.manager.MyFirebaseManager;
import com.bravvura.nestledtime.firebase.model.MemoryItem;
import com.bravvura.nestledtime.firebase.model.WorldItem;
import com.bravvura.nestledtime.userstory.ui.fragment.BaseFragment;
import com.bravvura.nestledtime.utils.Constants;
import com.bravvura.nestledtime.worlds.adapter.MemoryListAdapter;
import com.bravvura.nestledtime.worlds.ui.activity.WorldDetailActivity;

import java.util.ArrayList;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 24-03-2018.
 */

public class WorldDetailFragment extends BaseFragment {
    RecyclerView recyclerView;
    private MemoryListAdapter adapter;
    private WorldItem worldItem;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.world_detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initArguments();
        initComponent(view);
    }

    private void initArguments() {
        Bundle bundle = getArguments();
        worldItem = bundle.getParcelable(Constants.BUNDLE_KEY.WORLD_ITEM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case Constants.REQUEST_CODE.MEMORY_DETAIL:

                break;
        }
    }

    private void initComponent(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter = new MemoryListAdapter(new MemoryListAdapter.OnMemorySelectListener() {
            @Override
            public void onSelectMemory(MemoryItem memoryItem) {
                Intent intent = new Intent(getContext(), MemoryDetailActivity.class);
                intent.putExtra(Constants.BUNDLE_KEY.MEMORY_ITEM, memoryItem);
                startActivityForResult(intent, Constants.REQUEST_CODE.MEMORY_DETAIL);

            }
        }));
        MyFirebaseManager.getMemoryList(getContext(), worldItem.worldId,
                new OnValueEventListener<ArrayList<MemoryItem>>() {
                    @Override
                    public void onValueRecived(ArrayList<MemoryItem> value) {
                        adapter.updateResults(value);
                    }

                    @Override
                    public void onCancelled(String errorMessage) {

                    }
                });
    }
}
