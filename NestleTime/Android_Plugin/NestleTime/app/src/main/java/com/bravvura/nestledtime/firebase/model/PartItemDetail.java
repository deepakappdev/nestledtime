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
    public Double latitude;
    public Double longitude;
    public String name;


    public String hour;
    public String minutes;
    public String path;
    public Long seconds;
    public Long totalSeconds;


    public PartItemDetail() {
    }


    protected PartItemDetail(Parcel in) {
        caption = in.readString();
        collectionId = in.readString();
        body = in.readString();
        title = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        name = in.readString();
        hour = in.readString();
        minutes = in.readString();
        path = in.readString();
        seconds = in.readLong();
        totalSeconds = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(caption);
        dest.writeString(collectionId);
        dest.writeString(body);
        dest.writeString(title);
        dest.writeDouble(latitude == null ? 0 : latitude.doubleValue());
        dest.writeDouble(longitude == null ? 0 : longitude.doubleValue());
        dest.writeString(name);
        dest.writeString(hour);
        dest.writeString(minutes);
        dest.writeString(path);
        dest.writeLong(seconds == null ? 0 : seconds.longValue());
        dest.writeLong(totalSeconds == null ? 0 : totalSeconds.longValue());
    }

    @Override
    public int describeContents() {
        return 0;
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
        if (objectMap.containsKey("caption"))
            partItemDetail.caption = objectMap.get("caption").toString();

        if (objectMap.containsKey("body"))
            partItemDetail.body = objectMap.get("body").toString();

        if (objectMap.containsKey("collectionId"))
            partItemDetail.collectionId = objectMap.get("collectionId").toString();

        if (objectMap.containsKey("title"))
            partItemDetail.title = objectMap.get("title").toString();

        if (objectMap.containsKey("latitude"))
            partItemDetail.latitude = (double) objectMap.get("latitude");

        if (objectMap.containsKey("longitude"))
            partItemDetail.longitude = (double) objectMap.get("longitude");

        if (objectMap.containsKey("name"))
            partItemDetail.name = objectMap.get("name").toString();

        if (objectMap.containsKey("hour"))
            partItemDetail.hour = objectMap.get("hour").toString();

        if (objectMap.containsKey("minutes"))
            partItemDetail.minutes = objectMap.get("minutes").toString();

        if (objectMap.containsKey("path"))
            partItemDetail.path = objectMap.get("path").toString();

        if (objectMap.containsKey("seconds"))
            partItemDetail.seconds = (long) objectMap.get("seconds");

        if (objectMap.containsKey("totalSeconds"))
            partItemDetail.totalSeconds = (long) objectMap.get("totalSeconds");

        return partItemDetail;
    }


}