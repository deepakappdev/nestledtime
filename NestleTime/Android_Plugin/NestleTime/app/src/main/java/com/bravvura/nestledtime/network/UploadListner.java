package com.bravvura.nestledtime.network;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.bravvura.nestledtime.eventbusmodel.MediaModelUploadEvent;
import com.bravvura.nestledtime.utils.MyLogs;
import com.cloudinary.android.callback.ErrorInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Map;

public class UploadListner extends com.cloudinary.android.callback.ListenerService {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(String requestId) {

    }

    @Override
    public void onProgress(String requestId, long bytes, long totalBytes) {

    }

    @Override
    public void onSuccess(String requestId, Map resultData) {
        String serverUrl = "";
        String publilcId = "";
        if (resultData.containsKey("eager")) {
            ArrayList eagerData = (ArrayList) resultData.get("eager");
            if (eagerData.size() > 0) {
                Map eagerMap = (Map) eagerData.get(0);
                if (eagerMap.containsKey("url"))
                    serverUrl = eagerMap.get("url").toString();
            }
        } else {
            if (resultData.containsKey("url"))
                serverUrl = resultData.get("url").toString();
        }
        publilcId = resultData.get("public_id").toString();
        MediaModelUploadEvent event = new MediaModelUploadEvent();
        event.requestId = requestId;
        event.serverUrl = serverUrl;
        event.publicId = publilcId;
        EventBus.getDefault().post(event);
        MyLogs.i("CLOUD_UPLOAD", "Req:" + requestId + ", Public Id:" + publilcId + ", URL : " + serverUrl);

    }

    @Override
    public void onError(String requestId, ErrorInfo error) {

    }

    @Override
    public void onReschedule(String requestId, ErrorInfo error) {

    }
}