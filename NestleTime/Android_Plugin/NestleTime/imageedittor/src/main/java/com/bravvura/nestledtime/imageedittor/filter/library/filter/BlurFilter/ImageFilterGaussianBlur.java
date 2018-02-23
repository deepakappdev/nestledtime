package com.bravvura.nestledtime.imageedittor.filter.library.filter.BlurFilter;

import android.content.Context;

import com.bravvura.nestledtime.imageedittor.filter.library.filter.CameraFilter;
import com.bravvura.nestledtime.imageedittor.filter.library.filter.FilterGroup;

public class ImageFilterGaussianBlur extends FilterGroup<CameraFilter> {

    public ImageFilterGaussianBlur(Context context, float blur) {
        super();
        addFilter(new ImageFilterGaussianSingleBlur(context, blur, false));
        addFilter(new ImageFilterGaussianSingleBlur(context, blur, true));
    }
}
