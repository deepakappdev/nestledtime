package com.bravvura.nestledtime.mediagallery.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bravvura.nestledtime.mediagallery.fragment.FacebookAlbumFragment;
import com.bravvura.nestledtime.mediagallery.fragment.FacebookGalleryFragment;
import com.bravvura.nestledtime.mediagallery.fragment.FacebookPhotoFragment;
import com.bravvura.nestledtime.mediagallery.fragment.InstagramGalleryFragment;
import com.bravvura.nestledtime.mediagallery.fragment.InstagramPhotoFragment;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 15-03-2018.
 */

public class InstagramMediaGalleryAdapter extends FragmentStatePagerAdapter{
    private final InstagramGalleryFragment instagramGalleryFragment;
    String[] tabs = new String[]{"Photos", "Albums"};

    public InstagramMediaGalleryAdapter(FragmentManager fm, InstagramGalleryFragment instagramGalleryFragment) {
        super(fm);
        this.instagramGalleryFragment = instagramGalleryFragment;
    }

    Fragment getAllPhotoFragment() {
        InstagramPhotoFragment fragment = new InstagramPhotoFragment();
        fragment.parentFragment = instagramGalleryFragment;
        return fragment;
    }

//    Fragment getAllAlbumFragment() {
//        FacebookAlbumFragment fragment = new FacebookAlbumFragment();
//        fragment.parentFragment = facebookGalleryFragment;
//        return fragment;
//    }

    @Override
    public Fragment getItem(int position) {
        return getAllPhotoFragment();
//        if (position == 0) return getAllPhotoFragment();
//        else return getAllAlbumFragment();
    }

    @Override
    public int getCount() {
        return 0;/*tabs.length;*/
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
