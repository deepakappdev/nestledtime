package com.bravvura.nestledtime.mediagallery.model.facebook;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 19-03-2018.
 */

public class FacebookAlbumData implements Parcelable {
    public String name;
    public int photo_count;
    public String id;
    public FacebookPhotoCollection photos;

    public FacebookAlbumData() {
    }

    protected FacebookAlbumData(Parcel in) {
        name = in.readString();
        photo_count = in.readInt();
        id = in.readString();
        photos = in.readParcelable(FacebookPhotoCollection.class.getClassLoader());
    }

    public static final Creator<FacebookAlbumData> CREATOR = new Creator<FacebookAlbumData>() {
        @Override
        public FacebookAlbumData createFromParcel(Parcel in) {
            return new FacebookAlbumData(in);
        }

        @Override
        public FacebookAlbumData[] newArray(int size) {
            return new FacebookAlbumData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(photo_count);
        dest.writeString(id);
        dest.writeParcelable(photos, flags);
    }
}
