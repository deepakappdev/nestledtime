package com.nestletime.mediagallery.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nestletime.mediagallery.fragment.AllAlbumFragment;
import com.nestletime.mediagallery.fragment.AllPhotoFragment;

/**
 * Created by Deepak Saini on 06-02-2018.
 */

public class MediaGalleryAdapter extends FragmentStatePagerAdapter {

    String[] tabs = new String[]{"Photos", "Albums"};

    public MediaGalleryAdapter(FragmentManager fm) {
        super(fm);
    }

    Fragment getAllPhotoFragment() {
        Fragment fragment = new AllPhotoFragment();
        return fragment;
    }

    Fragment getAllAlbumFragment() {
        Fragment fragment = new AllAlbumFragment();
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
