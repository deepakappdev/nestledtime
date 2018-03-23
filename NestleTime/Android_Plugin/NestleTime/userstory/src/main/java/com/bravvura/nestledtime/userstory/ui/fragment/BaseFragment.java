package com.bravvura.nestledtime.userstory.ui.fragment;

import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.bravvura.nestledtime.activity.BaseActivity;
import com.bravvura.nestledtime.mediagallery.model.MediaModel;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 07-03-2018.
 */

public class BaseFragment extends Fragment {
    private Toast toast;


    public MediaModel getDateHeader(long time) {
        MediaModel mediaModel = new MediaModel();
        mediaModel.setLastModified(time);
        mediaModel.setType(-1);
        return mediaModel;
    }

    public void hideDialog() {
        if(getActivity()!=null)
            ((BaseActivity)getActivity()).hideProgressDialog();
    }

    public void showToast(String message) {
        if (toast == null)
            toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        else
            toast.setText(message);
        toast.show();
    }

    public void showDialog() {
        if(getActivity()!=null)
            ((BaseActivity)getActivity()).showProgressDialog();
    }

    protected boolean isPermissionGranted(String permission) {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(getContext(), permission);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }
}
