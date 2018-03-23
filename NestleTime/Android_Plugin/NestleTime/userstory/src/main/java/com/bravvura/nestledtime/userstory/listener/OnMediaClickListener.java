package com.bravvura.nestledtime.userstory.listener;

import android.support.v7.widget.RecyclerView;

import com.bravvura.nestledtime.userstory.model.UserStoryElement;

import java.util.ArrayList;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 05-03-2018.
 */

public interface OnMediaClickListener {
    void onClick(RecyclerView.ViewHolder viewHolder, UserStoryElement userStoryElement, int index);

    void onEditClick(UserStoryElement userStoryElement, int index);

    void onRemoveClick(UserStoryElement userStoryElement, int index);
}
