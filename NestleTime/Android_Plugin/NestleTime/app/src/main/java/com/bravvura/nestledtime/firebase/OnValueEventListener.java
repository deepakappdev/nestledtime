package com.bravvura.nestledtime.firebase;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 24-03-2018.
 */

public interface OnValueEventListener<T> {
    void onValueRecived(T value);

    void onCancelled(String errorMessage);
}
