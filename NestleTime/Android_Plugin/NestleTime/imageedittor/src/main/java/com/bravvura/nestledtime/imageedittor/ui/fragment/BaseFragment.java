package com.bravvura.nestledtime.imageedittor.ui.fragment;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bravvura.nestledtime.imageedittor.R;
import com.bravvura.nestledtime.imageedittor.main.activity.EditPhotoActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 22-02-2018.
 */

public class BaseFragment extends Fragment {

    public void back() {
        getActivity().onBackPressed();
    }

    public void showImage(String path, ImageView imageView) {
        Glide.with(getContext()).load(new File(path))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_done, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean isLoading() {
        return false;
    }

    ;

    public void saveImage() {
    }

    public void setTitle(String title) {
        ((EditPhotoActivity) getActivity()).setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_done) {
            if (!isLoading()) {
                saveImage();
            }
        } else if (item.getItemId() == android.R.id.home) {
            back();
        }
        return super.onOptionsItemSelected(item);
    }
}
