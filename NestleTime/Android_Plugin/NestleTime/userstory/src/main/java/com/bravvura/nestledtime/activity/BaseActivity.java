package com.bravvura.nestledtime.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.mediagallery.fragment.FacebookAlbumFragment;
import com.bravvura.nestledtime.mediagallery.fragment.FacebookAlbumPhotoFragment;
import com.bravvura.nestledtime.mediagallery.fragment.FacebookPhotoFragment;
import com.bravvura.nestledtime.mediagallery.fragment.LocalAlbumPhotoFragment;
import com.bravvura.nestledtime.mediagallery.fragment.LocalAlbumFragment;
import com.bravvura.nestledtime.mediagallery.fragment.LocalGalleryFragment;
import com.bravvura.nestledtime.mediagallery.fragment.LocalPhotoFragment;
import com.bravvura.nestledtime.mediagallery.model.MediaModel;
import com.bravvura.nestledtime.userstory.model.UserStoryMediaModel;
import com.bravvura.nestledtime.userstory.ui.fragment.UserStoryAudioRecorderFragment;
import com.bravvura.nestledtime.utils.Constants;
import com.bravvura.nestledtime.utils.FRAGMENTS;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Deepak Saini on 07-02-2018.
 */

public class BaseActivity extends AppCompatActivity {
    public Handler handler = new Handler();

    TextView toolbarTitle;
    public Toolbar toolbar;

    @Override
    public void setTitle(CharSequence title) {
        toolbarTitle.setText(title);
    }

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
            case FACEBOOK_ALBUM:
                fragment = new FacebookAlbumFragment();
                break;
            case FACEBOOK_ALBUM_PHOTO:
                fragment = new FacebookAlbumPhotoFragment();
                break;
            case FACEBOOK_PHOTO:
                fragment = new FacebookPhotoFragment();
                break;
            case LOCAL_GALLERY:
                fragment = new LocalGalleryFragment();
                break;
            case LOCAL_ALBUM_PHOTO:
                fragment = new LocalAlbumPhotoFragment();
                break;
            case LOCAL_ALBUM:
                fragment = new LocalAlbumFragment();
                break;
            case LOCAL_PHOTO:
                fragment = new LocalPhotoFragment();
                break;
            case AUDIO_RECORDER:
                fragment = new UserStoryAudioRecorderFragment();
                break;
        }
        return fragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        toolbarTitle = findViewById(R.id.toolbar_title);
    }

    public void finishWithResult(UserStoryMediaModel selectedMediaModel) {
        removeCacheFromLocal(selectedMediaModel.mediaModels);
        Bundle bundle = new Bundle();
//        JSONArray selectedMedias = getSelectedMediaPath(selectedMediaModel.mediaModels);
//        bundle.putString(Constants.BUNDLE_KEY.SELECTED_MEDIA_PATH, selectedMedias.toString());
        bundle.putParcelable(Constants.BUNDLE_KEY.SELECTED_MEDIA, selectedMediaModel);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void hideKeyBoard() {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
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

    private void removeCacheFromLocal(ArrayList<MediaModel> mediaModels) {
        for (MediaModel mediaModel : mediaModels) {
            mediaModel.removeTempFile();
        }
    }

    private JSONArray getSelectedMediaPath(ArrayList<MediaModel> mediaModels) {
        JSONArray jsonArray = new JSONArray();
        for (int index = 0; index < mediaModels.size(); index++) {
            if (mediaModels.get(index).isSelected())
                jsonArray.put(mediaModels.get(index).getPathFile());
        }
        return jsonArray;
    }

    Toast toast;

    public void showToast(String message) {
        if (toast == null)
            toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        else {
            toast.cancel();
            toast.setText(message);
        }
        toast.show();
    }
}
