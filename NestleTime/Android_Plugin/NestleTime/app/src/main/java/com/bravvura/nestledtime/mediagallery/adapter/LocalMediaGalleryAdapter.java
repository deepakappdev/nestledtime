package com.bravvura.nestledtime.mediagallery.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bravvura.nestledtime.mediagallery.fragment.LocalAlbumFragment;
import com.bravvura.nestledtime.mediagallery.fragment.LocalGalleryFragment;
import com.bravvura.nestledtime.mediagallery.fragment.LocalPhotoFragment;
import com.bravvura.nestledtime.utils.Constants;

/**
 * Created by Deepak Saini on 06-02-2018.
 */

public class LocalMediaGalleryAdapter extends FragmentStatePagerAdapter {

    private final LocalGalleryFragment localGalleryFragment;
    String[] tabs = new String[]{"Photos", "Albums"};

    public LocalMediaGalleryAdapter(FragmentManager fm, LocalGalleryFragment localGalleryFragment) {
        super(fm);
        this.localGalleryFragment = localGalleryFragment;
    }

    Fragment getAllPhotoFragment(int index) {
        LocalPhotoFragment fragment = new LocalPhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_KEY.INDEX, index);
        fragment.setArguments(bundle);
        fragment.parentFragment = localGalleryFragment;
        return fragment;
    }

    Fragment getAllAlbumFragment(int index) {
        LocalAlbumFragment fragment = new LocalAlbumFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_KEY.INDEX, index);
        fragment.setArguments(bundle);
        fragment.parentFragment = localGalleryFragment;
        return fragment;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) return getAllPhotoFragment(0);
        else return getAllAlbumFragment(1);
    }

    @Override
    public int getCount() {
        return tabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}