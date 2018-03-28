package com.bravvura.nestledtime.userstory.ui.fragment;

import android.app.DownloadManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.mediagallery.model.MediaModel;
import com.bravvura.nestledtime.userstory.model.MediaStopEventModel;
import com.bravvura.nestledtime.utils.CloudinaryManager;
import com.bravvura.nestledtime.utils.Constants;
import com.bravvura.nestledtime.utils.StringUtils;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 07-03-2018.
 */

public class UserStoryVideoFragment extends BaseFragment implements SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener, View.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnInfoListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener {
    private MediaModel mediaModel;
    private ImageView imagePlayPause;
    Handler handler = new Handler();
    private VideoView videoView;
    private SeekBar seekBar;
    private View layouotControl;
    private ProgressBar progressBar;
    private boolean isControlShowing;
    private MediaPlayer mediaPlayer;
    private TextView textCurrentDuration, textTotalDuration;
    private TextView textDescription;
    private ImageView imageView;
    private int currentIndex;
    private int selectedIndex;

    public static UserStoryVideoFragment create(MediaModel mediaModel, int index) {
        UserStoryVideoFragment fragment = new UserStoryVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.BUNDLE_KEY.MEDIA_MODEL, mediaModel);
        bundle.putInt(Constants.BUNDLE_KEY.INDEX, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
//        setHasOptionsMenu(true);
        setRetainInstance(true);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return inflater.inflate(R.layout.user_story_pager_video, container, false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MediaStopEventModel event) {
        selectedIndex = event.selectedIndex;
        stopMediaPlayer();
    }

    @Override
    public void onDestroyView() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        stopMediaPlayer();
        super.onDestroyView();
    }

    private void stopMediaPlayer() {
        try {
            if (mediaPlayer != null) {
                resetView();
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mediaModel = bundle.getParcelable(Constants.BUNDLE_KEY.MEDIA_MODEL);
            currentIndex = bundle.getInt(Constants.BUNDLE_KEY.INDEX, 0);
        }
        initComponent(view);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_download, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_download) {
            if (mediaModel != null)
                downloadFile();
        }
        return super.onOptionsItemSelected(item);
    }

    void downloadFile() {
        Uri downloadUri = Uri.parse(mediaModel.getUrl());
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle("Nestled Time");
        request.setDescription("video");
        request.setMimeType("application/mp4");
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "UserStoryImageFile.mp4");
        DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    private void initComponent(View view) {
        progressBar = view.findViewById(R.id.progress_bar);
        imagePlayPause = view.findViewById(R.id.image_play_pause);
        imagePlayPause.setOnClickListener(this);
        layouotControl = view.findViewById(R.id.layout_control);
        view.findViewById(R.id.layout_content).setOnClickListener(this);
        videoView = view.findViewById(R.id.video_view);
        imageView = view.findViewById(R.id.image_view);
        seekBar = view.findViewById(R.id.seek_bar);
        textDescription = view.findViewById(R.id.text_description);
        seekBar.setOnSeekBarChangeListener(this);
        textCurrentDuration = view.findViewById(R.id.text_current_duration);
        textTotalDuration = view.findViewById(R.id.text_total_duration);
        textCurrentDuration.setText("");
        textTotalDuration.setText("");
        textDescription.setText(mediaModel.getTitle());
        Glide.with(getContext()).load(CloudinaryManager.getVideoThumbnail(mediaModel.getPublicId())).into(imageView);
    }

    void playMediaPlayer(String path) {
        imagePlayPause.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        videoView.stopPlayback();
        videoView.setVideoPath(path);
        videoView.setOnCompletionListener(this);
        videoView.setOnPreparedListener(this);
        videoView.setOnInfoListener(this);
        videoView.setOnErrorListener(this);
        videoView.start();
        updateProgressBar();
    }

    private Runnable updateTimeTask = new Runnable() {
        public void run() {
            try {
                if (isMediaPlaying()) {
                    seekBar.setProgress(videoView.getCurrentPosition());
                    seekBar.setMax(videoView.getDuration());
                    int current = videoView.getCurrentPosition() / 1000;
                    int total = videoView.getDuration() / 1000;
                    textCurrentDuration.setText(String.format("%02d", current / 60) + ":" + String.format("%02d", current % 60));
                    textTotalDuration.setText(String.format("%02d", total / 60) + ":" + String.format("%02d", total % 60));
                }
                updateProgressBar();
            } catch (Exception x) {
            }
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            progressBar.setVisibility(View.VISIBLE);
            hideControls(true);
            mediaPlayer.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(updateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(updateTimeTask);
        videoView.seekTo(seekBar.getProgress());
        updateProgressBar();
    }

    private void updateProgressBar() {
        handler.postDelayed(updateTimeTask, 200);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        resetView();
    }

    private void resetView() {
        try {
            imagePlayPause.setVisibility(View.VISIBLE);
            imagePlayPause.setImageResource(R.drawable.ic_play_white);
            layouotControl.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            try {
                videoView.stopPlayback();
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
            } catch (Exception x) {
            }
        } catch (Exception x) {
        }
        mediaPlayer = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.image_play_pause) {
            if (mediaPlayer == null) {
                String filePath = "";
                if (mediaModel != null) {
                    if (!StringUtils.isNullOrEmpty(mediaModel.getPathFile()))
                        filePath = mediaModel.getPathFile();
                    else if (!StringUtils.isNullOrEmpty(mediaModel.getUrl()))
                        filePath = mediaModel.getUrl();
                }
//                filePath = "http://res.cloudinary.com/nestled-time-alpha/video/upload/f_m3u8/v1520500106/w11n79xbgyznfftvqjbn.m3u8";
                if (!StringUtils.isNullOrEmpty(filePath))
                    playMediaPlayer(filePath);
            } else if (isMediaPlaying()) {
                imagePlayPause.setImageResource(R.drawable.ic_play_white);
                showControls();
                videoView.pause();
            } else {
                hideControls(false);
                imagePlayPause.setImageResource(R.drawable.ic_pause_white);
                videoView.start();
            }

        } else if (v.getId() == R.id.layout_content) {
            if (mediaPlayer != null && progressBar.getVisibility() == View.GONE) {
                if (isControlShowing && imagePlayPause.getVisibility() != View.GONE) {
                    hideControls(false);
                } else {
                    showControls();
                }
            }
        }
    }

    public boolean isMediaPlaying() {
        try {
            return mediaPlayer != null && mediaPlayer.isPlaying();
        } catch (Exception x) {
            return false;
        }
    }

    private void showControls() {
        layouotControl.setVisibility(View.VISIBLE);
        seekBar.setVisibility(View.VISIBLE);
        textCurrentDuration.setVisibility(View.VISIBLE);
        textTotalDuration.setVisibility(View.VISIBLE);

        imagePlayPause.setVisibility(View.VISIBLE);
        isControlShowing = true;
        handler.removeCallbacks(hideRunnable);
//        ((BaseActivity)getActivity()).toolbar.setVisibility(View.VISIBLE);

        if (isMediaPlaying())
            handler.postDelayed(hideRunnable, 2000);

    }

    Runnable hideRunnable = new Runnable() {
        @Override
        public void run() {
            if (isMediaPlaying())
                imagePlayPause.setVisibility(View.GONE);
//            hideControls();
        }
    };

    private void hideControls(boolean force) {
        if (isMediaPlaying() || force && mediaPlayer != null) {
            imagePlayPause.setVisibility(View.GONE);
            layouotControl.setVisibility(View.GONE);
            isControlShowing = false;
        }
//        ((BaseActivity)getActivity()).toolbar.setVisibility(View.GONE);
        handler.removeCallbacks(hideRunnable);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        this.mediaPlayer = mp;
        mediaPlayer.setOnSeekCompleteListener(this);
        progressBar.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        imagePlayPause.setImageResource(R.drawable.ic_pause_white);
        hideControls(false);

    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {
                if (selectedIndex != currentIndex)
                    resetView();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        resetView();
        return true;
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        progressBar.setVisibility(View.GONE);
        showControls();
    }
}
