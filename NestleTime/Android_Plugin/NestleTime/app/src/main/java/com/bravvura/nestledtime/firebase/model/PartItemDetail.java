package com.bravvura.nestledtime.firebase.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 26-03-2018.
 */

public class PartItemDetail implements Parcelable {

    public String caption;
    public String collectionId;
    public String body;
    public String title;

    protected PartItemDetail(){}

    protected PartItemDetail(Parcel in) {
        caption = in.readString();
        collectionId = in.readString();
        body = in.readString();
        title = in.readString();
    }

    public static final Creator<PartItemDetail> CREATOR = new Creator<PartItemDetail>() {
        @Override
        public PartItemDetail createFromParcel(Parcel in) {
            return new PartItemDetail(in);
        }

        @Override
        public PartItemDetail[] newArray(int size) {
            return new PartItemDetail[size];
        }
    };

    public static PartItemDetail from(Map<String, Object> objectMap) {
        PartItemDetail partItemDetail = new PartItemDetail();
        if(objectMap.containsKey("caption"))
            partItemDetail.caption = objectMap.get("caption").toString();
        if(objectMap.containsKey("body"))
            partItemDetail.body = objectMap.get("body").toString();
        if(objectMap.containsKey("collectionId"))
            partItemDetail.collectionId = objectMap.get("collectionId").toString();
        if(objectMap.containsKey("title"))
            partItemDetail.title = objectMap.get("title").toString();
        return partItemDetail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(caption);
        dest.writeString(collectionId);
        dest.writeString(body);
        dest.writeString(title);
    }
}