package com.bravvura.nestledtime.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Deepak Saini on 16-02-2018.
 */

public class FlingRecyclerView extends RecyclerView {
    private static final double FLING_SPEED_FACTOR = 0.6;

    public FlingRecyclerView(Context context) {
        super(context);
    }

    public FlingRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FlingRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean fling(int velocityX, int velocityY)
    {

        // if FLING_SPEED_FACTOR between [0, 1[ => slowdown 
        velocityY *= FLING_SPEED_FACTOR;

        return super.fling(velocityX, velocityY);
    }
}
