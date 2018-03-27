package com.bravvura.nestledtime.userstory.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 13-03-2018.
 */

public class UserStoryAudioModel implements Parcelable {
    public String audioUrl;
    public String publicId;
    public long totalSecond;
    public UserStoryAudioModel() {
    }


    protected UserStoryAudioModel(Parcel in) {
        audioUrl = in.readString();
        publicId = in.readString();
        totalSecond = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(audioUrl);
        dest.writeString(publicId);
        dest.writeLong(totalSecond);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserStoryAudioModel> CREATOR = new Creator<UserStoryAudioModel>() {
        @Override
        public UserStoryAudioModel createFromParcel(Parcel in) {
            return new UserStoryAudioModel(in);
        }

        @Override
        public UserStoryAudioModel[] newArray(int size) {
            return new UserStoryAudioModel[size];
        }
    };
}
