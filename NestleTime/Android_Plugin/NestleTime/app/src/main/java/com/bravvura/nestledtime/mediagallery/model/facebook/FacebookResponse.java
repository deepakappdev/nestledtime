package com.bravvura.nestledtime.mediagallery.model.facebook;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 19-03-2018.
 */

public class FacebookResponse implements Parcelable {
    public int total;
    public FacebookData data;
    public int fbtimestamp;
    public int page;
    public String fbdate;

    public FacebookResponse() {
    }

    protected FacebookResponse(Parcel in) {
        total = in.readInt();
        data = in.readParcelable(FacebookData.class.getClassLoader());
        fbtimestamp = in.readInt();
        page = in.readInt();
        fbdate = in.readString();
    }

    public static final Creator<FacebookResponse> CREATOR = new Creator<FacebookResponse>() {
        @Override
        public FacebookResponse createFromParcel(Parcel in) {
            return new FacebookResponse(in);
        }

        @Override
        public FacebookResponse[] newArray(int size) {
            return new FacebookResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(total);
        dest.writeParcelable(data, flags);
        dest.writeInt(fbtimestamp);
        dest.writeInt(page);
        dest.writeString(fbdate);
    }
}
