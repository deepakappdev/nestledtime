package com.bravvura.nestledtime.firebase.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 26-03-2018.
 */

public class MediaItemDetail implements Parcelable {
    public String blobId;
    public String caption;
    public String cloudnaryURL;
    //    public String createdByName;
    public String createdByUserId;
    public String createdOn;
    public String downloadURL;
    public String modifiedByName;
    public String modifiedByUserId;
    public String modifiedOn;
    public String type;

    protected MediaItemDetail(Parcel in) {
        blobId = in.readString();
        caption = in.readString();
        cloudnaryURL = in.readString();
//        createdByName = in.readString();
        createdByUserId = in.readString();
        createdOn = in.readString();
        downloadURL = in.readString();
        modifiedByName = in.readString();
        modifiedByUserId = in.readString();
        modifiedOn = in.readString();
        type = in.readString();
    }

    public static final Creator<MediaItemDetail> CREATOR = new Creator<MediaItemDetail>() {
        @Override
        public MediaItemDetail createFromParcel(Parcel in) {
            return new MediaItemDetail(in);
        }

        @Override
        public MediaItemDetail[] newArray(int size) {
            return new MediaItemDetail[size];
        }
    };

    public MediaItemDetail() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(blobId);
        dest.writeString(caption);
        dest.writeString(cloudnaryURL);
//        dest.writeString(createdByName);
        dest.writeString(createdByUserId);
        dest.writeString(createdOn);
        dest.writeString(downloadURL);
        dest.writeString(modifiedByName);
        dest.writeString(modifiedByUserId);
        dest.writeString(modifiedOn);
        dest.writeString(type);
    }

    public static MediaItemDetail from(Map<String, Object> objectMap) {

        MediaItemDetail itemDetail = new MediaItemDetail();
        itemDetail.blobId = objectMap.get("blobId").toString();
        itemDetail.caption = objectMap.get("caption").toString();
        itemDetail.cloudnaryURL = objectMap.get("cloudnaryURL").toString();
//            itemDetail.createdByName = objectMap.get("createdByName").toString();
//            itemDetail.createdByUserId = objectMap.get("createdByUserId").toString();
//        itemDetail.createdOn = objectMap.get("createdOn").toString();
        itemDetail.downloadURL = objectMap.get("downloadURL").toString();
        itemDetail.modifiedByName = objectMap.get("modifiedByName").toString();
        itemDetail.modifiedByUserId = objectMap.get("modifiedByUserId").toString();
        itemDetail.modifiedOn = objectMap.get("modifiedOn").toString();
        itemDetail.type = objectMap.get("type").toString();
        return itemDetail;
    }
}
