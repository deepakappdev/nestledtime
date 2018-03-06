package com.bravvura.nestledtime.userstory.model;

import com.bravvura.nestledtime.mediagallery.model.MediaModel;

import java.util.ArrayList;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 26-02-2018.
 */

public class UserStoryElement {
    public UserStoryElementType elementType;
    public ArrayList<MediaModel> mediaModels;
    public String message;
    public UserStoryAddressModel addressModel;
    public MediaModel audioFile;
    public boolean isdeleted;

    public UserStoryElement(String message) {
        this.message = message;
        elementType = UserStoryElementType.ELEMENT_TYPE_TEXT;
    }
    public UserStoryElement() {
        this.message = "";
        elementType = UserStoryElementType.ELEMENT_TYPE_TITLE;
    }

    public UserStoryElement(ArrayList<MediaModel> mediaModels) {
        this.mediaModels = mediaModels;
        elementType = UserStoryElementType.ELEMENT_TYPE_MEDIA;
    }

    public UserStoryElement(MediaModel audioFile) {
        this.audioFile = audioFile;
        elementType = UserStoryElementType.ELEMENT_TYPE_AUDIO;
    }

    public UserStoryElement(UserStoryAddressModel addressModel) {
        this.addressModel = addressModel;
        elementType = UserStoryElementType.ELEMENT_TYPE_LOCATION;
    }


}
