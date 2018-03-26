package com.bravvura.nestledtime.firebase.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 26-03-2018.
 */

public class MemoryItem implements Parcelable {
    public String memoryId;
    public String worldId;

    public String createdByName;
    public String createdByUserId;
    public String createdOn;//"2018-03-26T11:57:53+05:30"
    public String doe;//"2018-03-26"
    public boolean isDraft;
    public boolean isPublished;
    public String modifiedByName;
    public String modifiedByUserId;
    public String modifiedOn;//"2018-03-26T11:57:53+05:30"
    public ArrayList<MemoryPartItem> parts;
    public String title = "";

    public MemoryItem() {
    }

    public MemoryItem(Parcel in) {
        memoryId = in.readString();
        worldId = in.readString();
        createdByName = in.readString();
        createdByUserId = in.readString();
        createdOn = in.readString();
        doe = in.readString();
        isDraft = in.readByte() != 0;
        isPublished = in.readByte() != 0;
        modifiedByName = in.readString();
        modifiedByUserId = in.readString();
        modifiedOn = in.readString();
        parts = in.createTypedArrayList(MemoryPartItem.CREATOR);
        title = in.readString();
    }

    public static final Creator<MemoryItem> CREATOR = new Creator<MemoryItem>() {
        @Override
        public MemoryItem createFromParcel(Parcel in) {
            return new MemoryItem(in);
        }

        @Override
        public MemoryItem[] newArray(int size) {
            return new MemoryItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(memoryId);
        dest.writeString(worldId);
        dest.writeString(createdByName);
        dest.writeString(createdByUserId);
        dest.writeString(createdOn);
        dest.writeString(doe);
        dest.writeByte((byte) (isDraft ? 1 : 0));
        dest.writeByte((byte) (isPublished ? 1 : 0));
        dest.writeString(modifiedByName);
        dest.writeString(modifiedByUserId);
        dest.writeString(modifiedOn);
        dest.writeTypedList(parts);
        dest.writeString(title);
    }

    public Date getDoeDate() {
        String[] dates = doe.split("-");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(dates[0]),Integer.parseInt(dates[1]),Integer.parseInt(dates[2]));
        return calendar.getTime();
    }

    public static MemoryItem from(Map<String, Object> objectMap) {
        MemoryItem memoryItem = new MemoryItem();
        memoryItem.createdByName = objectMap.get("createdByName").toString();
        memoryItem.createdByUserId = objectMap.get("createdByUserId").toString();
        memoryItem.createdOn = objectMap.get("createdOn").toString();
        memoryItem.doe = objectMap.get("doe").toString();
        memoryItem.isDraft = (boolean) objectMap.get("isDraft");
        memoryItem.isPublished = (boolean) objectMap.get("isPublished");
        memoryItem.modifiedByName = objectMap.get("modifiedByName").toString();
        memoryItem.modifiedByUserId = objectMap.get("modifiedByUserId").toString();
        memoryItem.modifiedOn = objectMap.get("modifiedOn").toString();
        memoryItem.title = objectMap.get("title").toString();
        memoryItem.modifiedByUserId = objectMap.get("modifiedByUserId").toString();
        if (objectMap.get("parts") instanceof Map) {
            Map<String, Object> partMap = (Map<String, Object>) objectMap.get("parts");
            if (partMap != null) {
                memoryItem.parts = new ArrayList<>();
                for (String key : partMap.keySet()) {
                    Map<String, Object> partDetail = (Map<String, Object>) partMap.get(key);
                    MemoryPartItem partItem = MemoryPartItem.form(partDetail);
                    memoryItem.parts.add(partItem);
                }
            }
        }


        return memoryItem;
    }
}
