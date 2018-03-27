package com.bravvura.nestledtime.firebase.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.bravvura.nestledtime.firebase.manager.MyFirebaseManager;
import com.bravvura.nestledtime.utils.MyDateFormatUtils;

import java.util.Map;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 26-03-2018.
 */

public class MemoryMediaItem implements Parcelable{
    public String cloudnaryURL;
    public long commentsCount;
    public String downloadURL;
    public String imageId;
    public MediaItemDetail imageObject;
    public boolean isLoaded;
    public long likesCount;
    public String type;
    public String userId;

    protected MemoryMediaItem(Parcel in) {
        cloudnaryURL = in.readString();
        commentsCount = in.readLong();
        downloadURL = in.readString();
        imageId = in.readString();
        imageObject = in.readParcelable(MediaItemDetail.class.getClassLoader());
        isLoaded = in.readByte() != 0;
        likesCount = in.readLong();
        type = in.readString();
        userId = in.readString();
    }

    public MemoryMediaItem() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cloudnaryURL);
        dest.writeLong(commentsCount);
        dest.writeString(downloadURL);
        dest.writeString(imageId);
        dest.writeParcelable(imageObject, flags);
        dest.writeByte((byte) (isLoaded ? 1 : 0));
        dest.writeLong(likesCount);
        dest.writeString(type);
        dest.writeString(userId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MemoryMediaItem> CREATOR = new Creator<MemoryMediaItem>() {
        @Override
        public MemoryMediaItem createFromParcel(Parcel in) {
            return new MemoryMediaItem(in);
        }

        @Override
        public MemoryMediaItem[] newArray(int size) {
            return new MemoryMediaItem[size];
        }
    };

    public static MemoryMediaItem from(Map<String, Object> imageDetailMap) {
        MemoryMediaItem mediaItem = new MemoryMediaItem();
        mediaItem.cloudnaryURL = imageDetailMap.get("cloudnaryURL").toString();
        mediaItem.commentsCount = (long) imageDetailMap.get("commentsCount");
        mediaItem.downloadURL = imageDetailMap.get("downloadURL").toString();
        mediaItem.imageId = imageDetailMap.get("imageId").toString();

        if(imageDetailMap.get("imageObject") instanceof Map)
            mediaItem.imageObject = MediaItemDetail.from((Map<String, Object>)imageDetailMap.get("imageObject"));

        imageDetailMap.get("cloudnaryURL").toString();
        mediaItem.isLoaded = (boolean) imageDetailMap.get("isLoaded");
        mediaItem.likesCount = (long) imageDetailMap.get("likesCount");
//        mediaItem.userId = imageDetailMap.get("userId").toString();
        return mediaItem;
    }

    public void setCreateion() {
        imageObject = new MediaItemDetail();
        imageObject.createdOn = MyDateFormatUtils.getNewDate();
        imageObject.createdByUserId = MyFirebaseManager.userId;
    }

    public void setModification() {
        imageObject.modifiedOn = MyDateFormatUtils.getNewDate();
        imageObject.modifiedByName = MyFirebaseManager.userId;
        imageObject.modifiedByUserId = MyFirebaseManager.userId;

    }
}
