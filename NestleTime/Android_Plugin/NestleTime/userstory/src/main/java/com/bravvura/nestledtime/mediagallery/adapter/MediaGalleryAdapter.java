package com.bravvura.nestledtime.mediagallery.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bravvura.nestledtime.mediagallery.fragment.LocalAlbumFragment;
import com.bravvura.nestledtime.mediagallery.fragment.LocalPhotoFragment;

/**
 * Created by Deepak Saini on 06-02-2018.
 */

public class MediaGalleryAdapter extends FragmentStatePagerAdapter {

    String[] tabs = new String[]{"Photos", "Albums"};

    public MediaGalleryAdapter(FragmentManager fm) {
        super(fm);
    }

    Fragment getAllPhotoFragment() {
        Fragment fragment = new LocalPhotoFragment();
        return fragment;
    }

    Fragment getAllAlbumFragment() {
        Fragment fragment = new LocalAlbumFragment();
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
