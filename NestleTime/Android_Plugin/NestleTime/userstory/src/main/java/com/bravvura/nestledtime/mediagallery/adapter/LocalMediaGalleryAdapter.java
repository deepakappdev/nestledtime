package com.bravvura.nestledtime.mediagallery.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bravvura.nestledtime.mediagallery.fragment.LocalAlbumFragment;
import com.bravvura.nestledtime.mediagallery.fragment.LocalGalleryFragment;
import com.bravvura.nestledtime.mediagallery.fragment.LocalPhotoFragment;

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

    Fragment getAllPhotoFragment() {
        LocalPhotoFragment fragment = new LocalPhotoFragment();
        fragment.parentFragment = localGalleryFragment;
        return fragment;
    }

    Fragment getAllAlbumFragment() {
        LocalAlbumFragment fragment = new LocalAlbumFragment();
        fragment.parentFragment = localGalleryFragment;
        return fragment;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) return getAllPhotoFragment();
        else return getAllAlbumFragment();
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