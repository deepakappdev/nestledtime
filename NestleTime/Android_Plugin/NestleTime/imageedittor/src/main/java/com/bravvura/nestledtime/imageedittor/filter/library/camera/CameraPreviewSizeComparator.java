package com.bravvura.nestledtime.imageedittor.filter.library.camera;

import android.hardware.Camera;

import java.util.Comparator;

public class CameraPreviewSizeComparator implements Comparator<Camera.Size> {

    public int compare(Camera.Size size1, Camera.Size size2) {
        return size1.width - size2.width;
    }
}
