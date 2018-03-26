package com.bravvura.nestledtime.worlds.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.activity.BaseActivity;
import com.bravvura.nestledtime.utils.FRAGMENTS;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 24-03-2018.
 */

public class WorldDetailActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_worlds);
        pushFragment(FRAGMENTS.WORLD_DETAIL, getIntent().getExtras(), false, false);
    }
}
