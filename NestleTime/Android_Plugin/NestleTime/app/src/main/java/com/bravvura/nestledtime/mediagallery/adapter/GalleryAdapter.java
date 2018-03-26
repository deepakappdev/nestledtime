package com.bravvura.nestledtime.mediagallery.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bravvura.nestledtime.mediagallery.fragment.FacebookGalleryFragment;
import com.bravvura.nestledtime.mediagallery.fragment.InstagramGalleryFragment;
import com.bravvura.nestledtime.mediagallery.fragment.LocalGalleryFragment;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 15-03-2018.
 */

public class GalleryAdapter extends FragmentStatePagerAdapter {
    public LocalGalleryFragment localGalleryFragment;
    FacebookGalleryFragment facebookGalleryFragment;
    InstagramGalleryFragment instagramGalleryFragment;

    public GalleryAdapter(FragmentManager fm) {
        super(fm);
    }

    Fragment getLocalGalleryFragment() {
        if (localGalleryFragment == null)
            localGalleryFragment = new LocalGalleryFragment();
        return localGalleryFragment;
    }

    Fragment getFacebookGalleryFragment() {
        if (facebookGalleryFragment == null)
            facebookGalleryFragment = new FacebookGalleryFragment();
        return facebookGalleryFragment;
    }

    Fragment getInstagramGalleryFragment() {
        if (instagramGalleryFragment == null)
            instagramGalleryFragment = new InstagramGalleryFragment();
        return instagramGalleryFragment;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return getLocalGalleryFragment();
            case 1:
                return getFacebookGalleryFragment();
            case 2:
                return getInstagramGalleryFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
