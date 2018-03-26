package com.bravvura.nestledtime.userstory.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.bravvura.nestledtime.mediagallery.model.MEDIA_CELL_TYPE;
import com.bravvura.nestledtime.mediagallery.model.MediaModel;
import com.bravvura.nestledtime.userstory.model.UserStoryMediaModel;
import com.bravvura.nestledtime.userstory.ui.fragment.UserStoryImageFragment;
import com.bravvura.nestledtime.userstory.ui.fragment.UserStoryVideoFragment;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 07-03-2018.
 */

public class UserStoryMediaPagerAdapter extends FragmentStatePagerAdapter {
    private final UserStoryMediaModel userStoryMediaModel;

    public UserStoryMediaPagerAdapter(FragmentManager fm, UserStoryMediaModel userStoryMediaModel) {
        super(fm);
        this.userStoryMediaModel = userStoryMediaModel;
    }

    @Override
    public int getCount() {
        return userStoryMediaModel.mediaModels.size();
    }

    UserStoryImageFragment getImageFragment(MediaModel mediaModel, int index) {
        return UserStoryImageFragment.create(mediaModel, index);
    }

    UserStoryVideoFragment getVideoFragment(MediaModel mediaModel, int index) {
        return UserStoryVideoFragment.create(mediaModel, index);
    }

    @Override
    public Fragment getItem(int position) {
        MediaModel mediaModel = userStoryMediaModel.mediaModels.get(position);
        if (mediaModel.mediaCellType == MEDIA_CELL_TYPE.TYPE_VIDEO)
            return getVideoFragment(mediaModel, position);
        else return getImageFragment(mediaModel, position);
    }

}
