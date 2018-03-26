package com.bravvura.nestledtime.firebase.manager;

import com.bravvura.nestledtime.firebase.model.MemoryMediaItem;
import com.bravvura.nestledtime.mediagallery.model.MEDIA_CELL_TYPE;
import com.bravvura.nestledtime.mediagallery.model.MEDIA_SOURCE_TYPE;
import com.bravvura.nestledtime.mediagallery.model.MediaModel;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 26-03-2018.
 */

public class FirebaseUtils {
    public static MediaModel getMediaModel(MemoryMediaItem mediaItem) {
        MediaModel mediaModel = new MediaModel();
        mediaModel.sourceType = MEDIA_SOURCE_TYPE.TYPE_CLOUD;
        mediaModel.isUploaded = true;
        mediaModel.setTitle(mediaItem.imageObject.caption);
        mediaModel.setUrl(mediaItem.cloudnaryURL);
        mediaModel.setPublicId(mediaItem.imageObject.blobId);
        if(mediaItem.imageObject.type.equalsIgnoreCase("image"))
            mediaModel.mediaCellType = MEDIA_CELL_TYPE.TYPE_IMAGE;
        else
            mediaModel.mediaCellType = MEDIA_CELL_TYPE.TYPE_VIDEO;
        return mediaModel;
    }
}
