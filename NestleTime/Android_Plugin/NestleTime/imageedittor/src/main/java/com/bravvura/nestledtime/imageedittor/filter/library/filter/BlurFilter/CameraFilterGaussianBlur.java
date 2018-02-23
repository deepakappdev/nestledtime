package com.bravvura.nestledtime.imageedittor.filter.library.filter.BlurFilter;

import android.content.Context;

import com.bravvura.nestledtime.imageedittor.filter.library.filter.CameraFilter;
import com.bravvura.nestledtime.imageedittor.filter.library.filter.FilterGroup;

public class CameraFilterGaussianBlur extends FilterGroup<CameraFilter> {

    public CameraFilterGaussianBlur(Context context, float blur) {
        super();
        addFilter(new CameraFilterGaussianSingleBlur(context, blur, false));
        addFilter(new CameraFilterGaussianSingleBlur(context, blur, true));
    }
}
