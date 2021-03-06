package com.bravvura.nestledtime.mediagallery.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.activity.BaseActivity;
import com.bravvura.nestledtime.mediagallery.model.MediaModel;
import com.bravvura.nestledtime.mediagallery.model.facebook.FacebookAlbumData;
import com.bravvura.nestledtime.utils.Constants;
import com.bravvura.nestledtime.utils.FRAGMENTS;

/**
 * Created by Deepak Saini on 08-02-2018.
 */

public class FacebookMediaAlbumActivity extends BaseActivity {
    private FacebookAlbumData albumModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_gallery);
        setupToolBar();
        Bundle bundle =  getIntent().getExtras();
        albumModel = bundle.getParcelable(Constants.BUNDLE_KEY.MEDIA_MODEL);
        setTitle(albumModel.name);
        pushFragment(FRAGMENTS.FACEBOOK_ALBUM_PHOTO, bundle, false, false);
    }


}
