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

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.firebase.OnValueEventListener;
import com.bravvura.nestledtime.firebase.manager.MyFirebaseManager;
import com.bravvura.nestledtime.firebase.model.WorldItem;
import com.bravvura.nestledtime.userstory.ui.fragment.BaseFragment;
import com.bravvura.nestledtime.utils.Constants;
import com.bravvura.nestledtime.worlds.adapter.WorldListAdapter;
import com.bravvura.nestledtime.worlds.ui.activity.WorldDetailActivity;

import java.util.ArrayList;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 24-03-2018.
 */

public class MyWorldsFragment extends BaseFragment {
    RecyclerView recyclerView;
    private WorldListAdapter adapter;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.myworlds_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initArguments();
        initComponent(view);
    }

    private void initArguments() {
        Bundle bundle = getArguments();
        userId = bundle.getString(Constants.BUNDLE_KEY.USER_ID);
    }

    private void initComponent(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter = new WorldListAdapter(new WorldListAdapter.OnWorldSelectListener() {
            @Override
            public void onSelectWorld(WorldItem worldItem) {
                Intent intent = new Intent(getContext(), WorldDetailActivity.class);
                intent.putExtra(Constants.BUNDLE_KEY.WORLD_ITEM, worldItem);
                intent.putExtra(Constants.BUNDLE_KEY.USER_ID, userId);
                startActivity(intent);
            }
        }));
        MyFirebaseManager.getWorldList(getContext(), new OnValueEventListener<ArrayList<WorldItem>>() {
            @Override
            public void onValueRecived(ArrayList<WorldItem> value) {
                adapter.updateResults(value);
            }

            @Override
            public void onCancelled(String errorMessage) {

            }
        });
    }
}
