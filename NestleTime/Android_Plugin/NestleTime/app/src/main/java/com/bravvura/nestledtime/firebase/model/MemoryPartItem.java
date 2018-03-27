package com.bravvura.nestledtime.firebase.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.bravvura.nestledtime.firebase.manager.MyFirebaseManager;
import com.bravvura.nestledtime.utils.MyDateFormatUtils;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Map;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 26-03-2018.
 */

public class MemoryPartItem implements Parcelable {
    public String createdByName;
    public String createdByUserId;
    public String createdOn;//"2018-03-26T12:38:01+05:30"
    public ArrayList<MemoryMediaItem> images;
    public String modifiedByName;//"facebookUserId::1617625578331129"
    public String modifiedByUserId;//"facebookUserId::1617625578331129"
    public String modifiedOn;//"2018-03-26T12:38:01+05:30"
    public PartItemDetail partDetail;
    public String partType;
    public long position;

    public MemoryPartItem() {
    }


    protected MemoryPartItem(Parcel in) {
        createdByName = in.readString();
        createdByUserId = in.readString();
        createdOn = in.readString();
        images = in.createTypedArrayList(MemoryMediaItem.CREATOR);
        modifiedByName = in.readString();
        modifiedByUserId = in.readString();
        modifiedOn = in.readString();
        partDetail = in.readParcelable(PartItemDetail.class.getClassLoader());
        partType = in.readString();
        position = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(createdByName);
        dest.writeString(createdByUserId);
        dest.writeString(createdOn);
        dest.writeTypedList(images);
        dest.writeString(modifiedByName);
        dest.writeString(modifiedByUserId);
        dest.writeString(modifiedOn);
        dest.writeParcelable(partDetail, flags);
        dest.writeString(partType);
        dest.writeLong(position);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MemoryPartItem> CREATOR = new Creator<MemoryPartItem>() {
        @Override
        public MemoryPartItem createFromParcel(Parcel in) {
            return new MemoryPartItem(in);
        }

        @Override
        public MemoryPartItem[] newArray(int size) {
            return new MemoryPartItem[size];
        }
    };

    public static MemoryPartItem form(Map<String, Object> partDetail) {
        MemoryPartItem memoryPartItem = new MemoryPartItem();
        memoryPartItem.createdByName = partDetail.get("createdByName").toString();
        memoryPartItem.createdByUserId = partDetail.get("createdByUserId").toString();
        memoryPartItem.createdOn = partDetail.get("createdOn").toString();
        memoryPartItem.modifiedByName = partDetail.get("modifiedByName").toString();
        memoryPartItem.modifiedByUserId = partDetail.get("modifiedByUserId").toString();
        memoryPartItem.modifiedOn = partDetail.get("modifiedOn").toString();
        memoryPartItem.partType = partDetail.get("partType").toString();
        memoryPartItem.position = (long) partDetail.get("position");
        memoryPartItem.createdByName = partDetail.get("createdByName").toString();
        if (partDetail.get("images") instanceof ArrayList) {
            ArrayList<Map> imagesMap = (ArrayList<Map>) partDetail.get("images");
            memoryPartItem.images = new ArrayList<>();
            for (Map map : imagesMap) {
                MemoryMediaItem memoryMediaItem = MemoryMediaItem.from(map);
                memoryMediaItem.imageObject.createdByUserId = memoryPartItem.createdByUserId;
                memoryMediaItem.imageObject.createdOn = memoryPartItem.createdOn;

                memoryPartItem.images.add(memoryMediaItem);
            }
        }

        if (partDetail.get("partDetail") instanceof Map) {
            memoryPartItem.partDetail = PartItemDetail.from((Map<String, Object>) partDetail.get("partDetail"));
        }
        return memoryPartItem;
    }


    public void setCreation() {
        createdByName = MyFirebaseManager.userId;
        createdByUserId = MyFirebaseManager.userId;
        createdOn = MyDateFormatUtils.getNewDate();
    }

    public void setModification() {
        modifiedByName = MyFirebaseManager.userId;
        modifiedByUserId = MyFirebaseManager.userId;
        modifiedOn = MyDateFormatUtils.getNewDate();
    }
}
