package com.bravvura.nestledtime.application.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 24-03-2018.
 */

public class RegistrationIntentService extends IntentService {
    public RegistrationIntentService(){super(RegistrationIntentService.class.getSimpleName());}
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public RegistrationIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
