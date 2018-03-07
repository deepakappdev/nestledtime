package com.bravvura.nestledtime.userstory.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.bravvura.nestledtime.mediagallery.model.MediaModel;

import java.util.ArrayList;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 26-02-2018.
 */

public class UserStoryElement implements Parcelable{
    public UserStoryElementType elementType;
    public UserStoryAddressModel addressModel;
    public UserStoryMediaModel mediaModel;
    public UserStoryTextModel textModel;
    public UserStoryDateModel dateModel;
    public boolean isdeleted;

    public UserStoryElement(String message, UserStoryElementType elementType) {
        textModel = new UserStoryTextModel();
        textModel.data = message;
        this.elementType = elementType;
    }

    public UserStoryElement(UserStoryAddressModel addressModel) {
        this.addressModel = addressModel;
        elementType = UserStoryElementType.ELEMENT_TYPE_LOCATION;
    }


    public UserStoryElement(UserStoryMediaModel mediaModel) {
        this.mediaModel = mediaModel;
        elementType = UserStoryElementType.ELEMENT_TYPE_MEDIA;
    }

    protected UserStoryElement(Parcel in) {
        addressModel = in.readParcelable(UserStoryAddressModel.class.getClassLoader());
        mediaModel = in.readParcelable(UserStoryMediaModel.class.getClassLoader());
        textModel = in.readParcelable(UserStoryTextModel.class.getClassLoader());
        dateModel = in.readParcelable(UserStoryDateModel.class.getClassLoader());
        isdeleted = in.readByte() != 0;
    }

    public static final Creator<UserStoryElement> CREATOR = new Creator<UserStoryElement>() {
        @Override
        public UserStoryElement createFromParcel(Parcel in) {
            return new UserStoryElement(in);
        }

        @Override
        public UserStoryElement[] newArray(int size) {
            return new UserStoryElement[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(addressModel, flags);
        dest.writeParcelable(mediaModel, flags);
        dest.writeParcelable(textModel, flags);
        dest.writeParcelable(dateModel, flags);
        dest.writeByte((byte) (isdeleted ? 1 : 0));
    }
}
