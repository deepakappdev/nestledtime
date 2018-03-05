package com.bravvura.nestledtime.userstory.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bravvura.nestledtime.userstory.model.UserStoryElement;
import com.bravvura.nestledtime.utils.Utils;

import java.util.ArrayList;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 26-02-2018.
 */

public class UserStoryElementListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TITLE = 1, VIEW_TEXT = 2, VIEW_MEDIA = 3, VIEW_LOCATION = 4, VIEW_AUDIO = 5, VIEW_DATE = 6, VIEW_BLANK = 7;

    ArrayList<UserStoryElement> userStoryElements = new ArrayList<>();

    public void setResult(ArrayList<UserStoryElement> userStoryElements) {
        this.userStoryElements = userStoryElements;
    }

    public void appendResult(ArrayList<UserStoryElement> userStoryElements) {
        if (this.userStoryElements == null)
            this.userStoryElements = new ArrayList<>();
        this.userStoryElements.addAll(userStoryElements);
    }

    public void appendResult(UserStoryElement userStoryElement) {
        if (this.userStoryElements == null)
            this.userStoryElements = new ArrayList<>();
        this.userStoryElements.add(userStoryElement);
    }

    public void removeIndex(int index) {
        if(!Utils.isEmpty(userStoryElements) && userStoryElements.size()>index) {
            userStoryElements.get(index).isdeleted = true;
        }
    }


    @Override
    public int getItemViewType(int position) {
        if(userStoryElements.get(position).isdeleted)
            return VIEW_BLANK;
        switch (userStoryElements.get(position).elementType) {
            case ELEMENT_TYPE_AUDIO:
                return VIEW_AUDIO;
            case ELEMENT_TYPE_DATE:
                return VIEW_DATE;
            case ELEMENT_TYPE_LOCATION:
                return VIEW_LOCATION;
            case ELEMENT_TYPE_MEDIA:
                return VIEW_MEDIA;
            case ELEMENT_TYPE_TITLE:
                return VIEW_TITLE;
            case ELEMENT_TYPE_TEXT:
                return VIEW_TEXT;
            default:
                return VIEW_TEXT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
if(holder instanceof TextViewHolder) {

}
    }

    @Override
    public int getItemCount() {
        return userStoryElements.size();
    }
    class TextViewHolder extends RecyclerView.ViewHolder{

        public TextViewHolder(View itemView) {
            super(itemView);
        }

    }
}
