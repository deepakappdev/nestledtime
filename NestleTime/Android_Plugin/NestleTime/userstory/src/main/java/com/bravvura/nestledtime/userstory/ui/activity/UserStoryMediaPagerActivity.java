package com.bravvura.nestledtime.userstory.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.activity.BaseActivity;
import com.bravvura.nestledtime.userstory.adapter.UserStoryMediaPagerAdapter;
import com.bravvura.nestledtime.userstory.model.UserStoryElement;
import com.bravvura.nestledtime.userstory.model.UserStoryMediaModel;
import com.bravvura.nestledtime.utils.Constants;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 07-03-2018.
 */

public class UserStoryMediaPagerActivity extends BaseActivity {
    private ViewPager viewPager;
    UserStoryMediaModel userStoryMediaModel;
    private int index;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_story_pager);
        setupToolBar();
        initArguments();
        initComponent();
    }

    private void initArguments() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(Constants.BUNDLE_KEY.USER_STORY_MEDIA_MODEL)) {
            userStoryMediaModel = bundle.getParcelable(Constants.BUNDLE_KEY.USER_STORY_MEDIA_MODEL);
            index = bundle.getInt(Constants.BUNDLE_KEY.INDEX, 0);
        }
    }

    private void initComponent() {
        if(toolbar!=null)
            toolbar.setBackgroundResource(R.color.semi_transparent_grey);

        viewPager = findViewById(R.id.view_pager);
        PagerAdapter adapter = new UserStoryMediaPagerAdapter(getSupportFragmentManager(), userStoryMediaModel);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(index);
    }
}
