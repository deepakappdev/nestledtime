package com.bravvura.nestledtime.utils;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.util.DisplayMetrics;

/**
 * Created by Deepak Saini on 13-02-2018.
 */

public class MyLinearSmoothScroller extends LinearSmoothScroller {
    private final LinearLayoutManager layoutManager;

    public MyLinearSmoothScroller(Context context, LinearLayoutManager layoutManager) {
        super(context);
        this.layoutManager = layoutManager;
    }
    private final float MILLISECONDS_PER_INCH = 5f;

    //This controls the direction in which smoothScroll looks
    //for your view
    @Override
    public PointF computeScrollVectorForPosition
    (int targetPosition) {
        return layoutManager.computeScrollVectorForPosition(targetPosition);
    }

    //This returns the milliseconds it takes to
    //scroll one pixel.
    @Override
    protected float calculateSpeedPerPixel
    (DisplayMetrics displayMetrics) {
        return MILLISECONDS_PER_INCH/displayMetrics.densityDpi;
    }
}
