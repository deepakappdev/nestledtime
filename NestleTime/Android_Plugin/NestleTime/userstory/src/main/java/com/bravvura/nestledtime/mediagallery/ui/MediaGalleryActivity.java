package com.bravvura.nestledtime.mediagallery.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.activity.BaseActivity;
import com.bravvura.nestledtime.mediagallery.adapter.GalleryAdapter;
import com.bravvura.nestledtime.utils.FRAGMENTS;


/**
 * Created by Deepak Saini on 06-02-2018.
 */

public class MediaGalleryActivity extends BaseActivity {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    public GalleryAdapter adapter;
    private TabLayout.OnTabSelectedListener tabSelectListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            viewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_gallery);
        setupToolBar();
        initComponent();
    }

    private void initComponent() {
        tabLayout = findViewById(R.id.tab_layout);
        TabLayout.Tab tab = tabLayout.newTab();
        tab.setIcon(R.drawable.gallery_tab_local_selector);
        tabLayout.addTab(tab);
        tab = tabLayout.newTab();
        tab.setIcon(R.drawable.gallery_tab_facebook_selector);
        tabLayout.addTab(tab);
        tab = tabLayout.newTab();
        tab.setIcon(R.drawable.gallery_tab_instagram_selector);
        tabLayout.addTab(tab);
        tabLayout.addOnTabSelectedListener(tabSelectListener);
        viewPager = findViewById(R.id.view_pager);
        adapter = new GalleryAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}