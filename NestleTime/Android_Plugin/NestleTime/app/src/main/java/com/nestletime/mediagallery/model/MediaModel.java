package com.nestletime.mediagallery.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MediaModel implements Parcelable {
    int id;
    String title;
    String pathFile;
    String url;

    String thumbnailFile;
    String pathFolder;
    long lastModified;
    String date;

    boolean isSelected = false;
    public MEDIA_CELL_TYPE mediaCellType;
    public String thumbnail;
    public int duration;
    public int mediaCount;
    public String description;
    public MEDIA_SOURCE_TYPE sourceType = MEDIA_SOURCE_TYPE.TYPE_LOCAL;
    public boolean isEdited;
    public boolean isDeleted;
    private String compressPath;
    private String requestId;


    public MediaModel() {

    }

    protected MediaModel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        pathFile = in.readString();
        url = in.readString();
        thumbnailFile = in.readString();
        pathFolder = in.readString();
        lastModified = in.readLong();
        date = in.readString();
        isSelected = in.readByte() != 0;
        mediaCellType = MEDIA_CELL_TYPE.valueOf(in.readString());
        thumbnail = in.readString();
        description = in.readString();
        sourceType = MEDIA_SOURCE_TYPE.valueOf(in.readString());
        isEdited = in.readInt()==1;
        isDeleted = in.readInt()==1;
        compressPath = in.readString();
        requestId = in.readString();
    }

    public static final Creator<MediaModel> CREATOR = new Creator<MediaModel>() {
        @Override
        public MediaModel createFromParcel(Parcel in) {
            return new MediaModel(in);
        }

        @Override
        public MediaModel[] newArray(int size) {
            return new MediaModel[size];
        }
    };

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
        Date d = new Date(this.lastModified);
        SimpleDateFormat sdf = new SimpleDateFormat("dd - MMM - yyyy");
        setDate(sdf.format(d));
    }

    public MediaModel(String title, String pathFile, String pathFolder, long lastModified) {
        this.title = title;
        this.pathFile = pathFile;
        this.pathFolder = pathFolder;
        setLastModified(lastModified);
    }

    public String getPathFile() {
        return this.pathFile;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPathFolder() {
        return this.pathFolder;
    }

    public void setPathFolder(String pathFolder) {
        this.pathFolder = pathFolder;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(int type) {
        switch(type) {
            case MediaStore.Files.FileColumns.MEDIA_TYPE_NONE://0
                mediaCellType = MEDIA_CELL_TYPE.TYPE_NONE;
                break;
            case MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO://2
                mediaCellType = MEDIA_CELL_TYPE.TYPE_AUDIO;
                break;
            case MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE://1
                mediaCellType = MEDIA_CELL_TYPE.TYPE_IMAGE;
                break;
            case MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO://3
                mediaCellType = MEDIA_CELL_TYPE.TYPE_VIDEO;
                break;
            case MediaStore.Files.FileColumns.MEDIA_TYPE_PLAYLIST://4
                break;
            case -1:
                mediaCellType = MEDIA_CELL_TYPE.TYPE_HEADER;
                break;
            case 5:
                mediaCellType = MEDIA_CELL_TYPE.TYPE_ALBUM;
                break;
        }
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(pathFile);
        dest.writeString(url);
        dest.writeString(thumbnailFile);
        dest.writeString(pathFolder);
        dest.writeLong(lastModified);
        dest.writeString(date);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeString(mediaCellType.name());
        dest.writeString(thumbnail);
        dest.writeString(description);
        dest.writeString(sourceType.name());
        dest.writeInt(isEdited ?1:0);
        dest.writeInt(isDeleted?1:0);
        dest.writeString(compressPath);
        dest.writeString(requestId);
    }

    public void setCompressPath(String compressPath) {
        this.compressPath = compressPath;
    }

    public String getCompressPath() {
        return compressPath;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }
}