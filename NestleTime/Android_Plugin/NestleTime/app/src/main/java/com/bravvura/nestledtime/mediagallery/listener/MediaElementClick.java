package com.bravvura.nestledtime.mediagallery.listener;

import com.bravvura.nestledtime.mediagallery.model.MediaModel;

/**
 * Created by Deepak Saini on 08-02-2018.
 */

public interface MediaElementClick {
    void onClick(int index, MediaModel mediaModel);
}
