package com.bravvura.nestledtime.userstory.model;

import com.bravvura.nestledtime.mediagallery.model.MediaModel;

import java.util.ArrayList;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 26-02-2018.
 */

public class UserStoryElement {
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

//    public UserStoryElement(MediaModel audioFile) {
//        this.audioFile = audioFile;
//        elementType = UserStoryElementType.ELEMENT_TYPE_AUDIO;
//    }

    public UserStoryElement(UserStoryAddressModel addressModel) {
        this.addressModel = addressModel;
        elementType = UserStoryElementType.ELEMENT_TYPE_LOCATION;
    }


    public UserStoryElement(UserStoryMediaModel mediaModel) {
        this.mediaModel = mediaModel;
        elementType = UserStoryElementType.ELEMENT_TYPE_MEDIA;
    }
}
