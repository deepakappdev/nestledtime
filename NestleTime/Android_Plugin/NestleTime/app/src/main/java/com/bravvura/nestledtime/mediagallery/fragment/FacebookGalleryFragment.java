package com.bravvura.nestledtime.mediagallery.fragment;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.eventbusmodel.MessageFacebookIndexEvent;
import com.bravvura.nestledtime.eventbusmodel.MessageLocalIndexEvent;
import com.bravvura.nestledtime.eventbusmodel.MessagePhotoFound;
import com.bravvura.nestledtime.mediagallery.adapter.FaceBookMediaGalleryAdapter;
import com.bravvura.nestledtime.mediagallery.adapter.LocalMediaGalleryAdapter;
import com.bravvura.nestledtime.userstory.ui.fragment.BaseFragment;
import com.bravvura.nestledtime.utils.Constants;

import org.greenrobot.eventbus.EventBus;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 15-03-2018.
 */

public class FacebookGalleryFragment extends BaseFragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FaceBookMediaGalleryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_media_facebook_pager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
    }

    private void initComponent(View view) {
        adapter = new FaceBookMediaGalleryAdapter(getChildFragmentManager(), this);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    MessageFacebookIndexEvent facebookIndexEvent = new MessageFacebookIndexEvent();
                    facebookIndexEvent.selectedIndex = position;
                    EventBus.getDefault().post(facebookIndexEvent);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);

        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        TextView textOne = view1.findViewById(R.id.tab);
        textOne.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tab_photo_selector, 0, 0, 0);
        textOne.setText("Photos");
        View view2 = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        TextView textTwo = view2.findViewById(R.id.tab);
        textTwo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tab_album_selector, 0, 0, 0);
        textTwo.setText("Albums");
        tabLayout.getTabAt(0).setCustomView(view1);
        tabLayout.getTabAt(1).setCustomView(view2);
    }

}
