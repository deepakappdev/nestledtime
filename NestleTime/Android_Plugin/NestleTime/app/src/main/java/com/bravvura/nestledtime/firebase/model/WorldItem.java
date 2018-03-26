package com.bravvura.nestledtime.firebase.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 24-03-2018.
 */

public class WorldItem implements Parcelable{
    public String worldId;
    public String bannerBlobId;
    public String bannerMediaId;
    public String createdByName;
    public String createdByUserId;
    public String createdOn;
    public String modifiedByName;
    public String modifiedByUserId;
    public String modifiedOn;
    public String ownerName;
    public String ownerPersonId;

    protected WorldItem(Parcel in) {
        worldId = in.readString();
        bannerBlobId = in.readString();
        bannerMediaId = in.readString();
        createdByName = in.readString();
        createdByUserId = in.readString();
        createdOn = in.readString();
        modifiedByName = in.readString();
        modifiedByUserId = in.readString();
        modifiedOn = in.readString();
        ownerName = in.readString();
        ownerPersonId = in.readString();
    }

    public static final Creator<WorldItem> CREATOR = new Creator<WorldItem>() {
        @Override
        public WorldItem createFromParcel(Parcel in) {
            return new WorldItem(in);
        }

        @Override
        public WorldItem[] newArray(int size) {
            return new WorldItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(worldId);
        dest.writeString(bannerBlobId);
        dest.writeString(bannerMediaId);
        dest.writeString(createdByName);
        dest.writeString(createdByUserId);
        dest.writeString(createdOn);
        dest.writeString(modifiedByName);
        dest.writeString(modifiedByUserId);
        dest.writeString(modifiedOn);
        dest.writeString(ownerName);
        dest.writeString(ownerPersonId);
    }
}
