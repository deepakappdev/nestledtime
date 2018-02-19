package com.nestletime.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.nestletime.R;
import com.nestletime.mediagallery.fragment.AlbumPhotoFragment;
import com.nestletime.mediagallery.fragment.AllAlbumFragment;
import com.nestletime.mediagallery.fragment.AllPhotoFragment;
import com.nestletime.mediagallery.model.MediaModel;
import com.nestletime.utils.Constants;
import com.nestletime.utils.FRAGMENTS;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Deepak Saini on 07-02-2018.
 */

public class BaseActivity extends AppCompatActivity {


    protected boolean isPermissionGranted(String permission) {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, permission);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

//    void pushFragment(FRAGMENTS fragmentId, Bundle bundle) {
//        pushFragment(fragmentId, bundle, true, true);
//    }

    Fragment getFragment(FRAGMENTS fragmentId) {
        Fragment fragment = null;
        switch (fragmentId) {
            case ALBUM_PHOTO:
                fragment = new AlbumPhotoFragment();
                break;
            case ALL_ALBUM:
                fragment = new AllAlbumFragment();
                break;
            case ALL_PHOTO:
                fragment = new AllPhotoFragment();
                break;
        }
        return fragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void pushFragment(FRAGMENTS fragmentId, Bundle bundle, boolean addToBackStack, boolean replace) {
        Fragment fragment = getFragment(fragmentId);
        if (bundle != null)
            fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (replace)
            fragmentTransaction.replace(R.id.frame_layout, fragment, fragmentId.name()).commit();
        else
            fragmentTransaction.add(R.id.frame_layout, fragment, fragmentId.name()).commit();
        if (addToBackStack)
            fragmentTransaction.addToBackStack(fragmentId.name());
    }


    public void setupToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void finishWithResult(ArrayList<MediaModel> selectedMediaModels) {
        Bundle bundle = new Bundle();
        JSONArray selectedMedias = getSelectedMediaPath(selectedMediaModels);
        bundle.putString(Constants.BUNDLE_KEY.SELECTED_MEDIA_PATH, selectedMedias.toString());
        bundle.putParcelableArrayList(Constants.BUNDLE_KEY.SELECTED_MEDIA, selectedMediaModels);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private JSONArray getSelectedMediaPath(ArrayList<MediaModel> mediaModels) {
        JSONArray jsonArray = new JSONArray();
        for (int index = 0; index < mediaModels.size(); index++) {
            if (mediaModels.get(index).isSelected())
                jsonArray.put(mediaModels.get(index).getPathFile());
        }
        return jsonArray;
    }
}
