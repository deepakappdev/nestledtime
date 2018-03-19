package com.bravvura.nestledtime.mediagallery.model.facebook;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 19-03-2018.
 */

public class FacebookItemData implements Parcelable {
    public String source;
    public String id;
    public String source_thumb;
    public String type;
    public String socialtype;

    public FacebookItemData() {
    }

    protected FacebookItemData(Parcel in) {
        source = in.readString();
        id = in.readString();
        source_thumb = in.readString();
        type = in.readString();
        socialtype = in.readString();
    }

    public static final Creator<FacebookItemData> CREATOR = new Creator<FacebookItemData>() {
        @Override
        public FacebookItemData createFromParcel(Parcel in) {
            return new FacebookItemData(in);
        }

        @Override
        public FacebookItemData[] newArray(int size) {
            return new FacebookItemData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(source);
        dest.writeString(id);
        dest.writeString(source_thumb);
        dest.writeString(type);
        dest.writeString(socialtype);
    }
}
