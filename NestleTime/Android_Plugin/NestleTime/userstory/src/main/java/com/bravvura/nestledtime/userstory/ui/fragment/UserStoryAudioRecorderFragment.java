package com.bravvura.nestledtime.userstory.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.eventbusmodel.AudioRecordEventModel;
import com.bravvura.nestledtime.utils.CloudinaryManager;
import com.bravvura.nestledtime.utils.Constants;
import com.bravvura.nestledtime.utils.MyFileSystem;
import com.bravvura.nestledtime.utils.PermissionUtils;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.ListenerService;
import com.cloudinary.android.callback.UploadCallback;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 13-03-2018.
 */

public class UserStoryAudioRecorderFragment extends BaseFragment implements View.OnClickListener {
    private TextView textCancel, textDone;
    private Chronometer chronometer_timer;
    private ImageView imageRecord;
    private MediaRecorder mRecorder;
    private File outputFile;
    private String requestId;
    private UploadCallback callBack = new ListenerService() {
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
            if (UserStoryAudioRecorderFragment.this.requestId.equalsIgnoreCase(requestId)) {
                if (resultData.containsKey("url")) {
                    AudioRecordEventModel eventModel = new AudioRecordEventModel();
                    eventModel.audioFileUrl = resultData.get("url").toString();
                    eventModel.publicId = resultData.get("public_id").toString();

                    EventBus.getDefault().post(eventModel);
                    MyFileSystem.deleteFile(outputFile);
                    hideDialog();
                    getFragmentManager().popBackStack();
                }
            }
        }

        @Override
        public void onError(String requestId, ErrorInfo error) {
            if (UserStoryAudioRecorderFragment.this.requestId.equalsIgnoreCase(requestId)) {
                showToast("Something working wrong");
//                startUploadingToCloudinary(outputFile.getAbsolutePath());
            }
        }

        @Override
        public void onReschedule(String requestId, ErrorInfo error) {
            if (UserStoryAudioRecorderFragment.this.requestId.equalsIgnoreCase(requestId)) {
//                startUploadingToCloudinary(outputFile.getAbsolutePath());
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_audio_recorder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
    }

    private void initComponent(View view) {
        textCancel = view.findViewById(R.id.text_cancel);
        textDone = view.findViewById(R.id.text_done);
        chronometer_timer = view.findViewById(R.id.chronometer_timer);
        imageRecord = view.findViewById(R.id.image_record);

        textCancel.setOnClickListener(this);
        textDone.setOnClickListener(this);
        imageRecord.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.text_cancel) {
            cancelRecording();
        } else if (v.getId() == R.id.image_record) {
            if (PermissionUtils.checkAudioRecord(getContext()) &&
                    PermissionUtils.checkReadExternal(getContext()) &&
                    PermissionUtils.checkWriteExternal(getContext())) {
                startRecording();
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constants.REQUEST_CODE.REQUEST_RECORD_AUDIO);
            }
        } else if (v.getId() == R.id.text_done) {
            finishRecording();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.REQUEST_CODE.REQUEST_RECORD_AUDIO:
                if (PermissionUtils.checkAudioRecord(getContext()) &&
                        PermissionUtils.checkReadExternal(getContext()) &&
                        PermissionUtils.checkWriteExternal(getContext())) {
                    startRecording();
                } else {
                    Toast.makeText(getContext(), "You must give permissions to use audio recording.", Toast.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();
                }
                break;
        }
    }

    private void startRecording() {
        textDone.setEnabled(true);
        textDone.setTextColor(Color.WHITE);
        imageRecord.setEnabled(false);

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        outputFile = MyFileSystem.getTempVideoFile();
        mRecorder.setOutputFile(outputFile.getAbsolutePath());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        chronometer_timer.setBase(SystemClock.elapsedRealtime());
        chronometer_timer.start();
    }

    private void finishRecording() {
        stopRecording();
        startUploadingToCloudinary(outputFile.getAbsolutePath());
    }

    private void startUploadingToCloudinary(String absolutePath) {
        showDialog();
        requestId = CloudinaryManager.uploadAudioFile(absolutePath, callBack);
    }


    private void cancelRecording() {
        stopRecording();
        MyFileSystem.deleteFile(outputFile);
        getFragmentManager().popBackStack();
    }

    private void stopRecording() {
        try {
            mRecorder.stop();
            mRecorder.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mRecorder = null;
        //starting the chronometer
        chronometer_timer.stop();
        chronometer_timer.setBase(SystemClock.elapsedRealtime());
        //showing the play button
    }
}
