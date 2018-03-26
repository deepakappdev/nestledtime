package com.bravvura.nestledtime.mediagallery.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bravvura.nestledtime.mediagallery.fragment.FacebookAlbumFragment;
import com.bravvura.nestledtime.mediagallery.fragment.FacebookGalleryFragment;
import com.bravvura.nestledtime.mediagallery.fragment.FacebookPhotoFragment;
import com.bravvura.nestledtime.mediagallery.fragment.LocalAlbumFragment;
import com.bravvura.nestledtime.mediagallery.fragment.LocalGalleryFragment;
import com.bravvura.nestledtime.mediagallery.fragment.LocalPhotoFragment;
import com.bravvura.nestledtime.utils.Constants;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 15-03-2018.
 */

public class FaceBookMediaGalleryAdapter extends FragmentStatePagerAdapter{
    private final FacebookGalleryFragment facebookGalleryFragment;
    String[] tabs = new String[]{"Photos", "Albums"};

    public FaceBookMediaGalleryAdapter(FragmentManager fm, FacebookGalleryFragment facebookGalleryFragment) {
        super(fm);
        this.facebookGalleryFragment = facebookGalleryFragment;
    }

    Fragment getAllPhotoFragment(int index) {
        FacebookPhotoFragment fragment = new FacebookPhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_KEY.INDEX, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    Fragment getAllAlbumFragment(int index) {
        FacebookAlbumFragment fragment = new FacebookAlbumFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_KEY.INDEX, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) return getAllPhotoFragment(position);
        else return getAllAlbumFragment(position);
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
