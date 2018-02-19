package com.nestletime.mediagallery.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nestletime.R;
import com.nestletime.mediagallery.model.MediaModel;
import com.nestletime.ui.activity.BaseActivity;
import com.nestletime.utils.Constants;
import com.nestletime.utils.FRAGMENTS;

/**
 * Created by Deepak Saini on 08-02-2018.
 */

public class MediaAlbumActivity extends BaseActivity {
    private MediaModel albumModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_gallery);
        setupToolBar();
        Bundle bundle =  getIntent().getExtras();
        albumModel = bundle.getParcelable(Constants.BUNDLE_KEY.MEDIA_MODEL);
        setTitle(albumModel.getTitle());
        pushFragment(FRAGMENTS.ALBUM_PHOTO, bundle, false, false);
    }


}
