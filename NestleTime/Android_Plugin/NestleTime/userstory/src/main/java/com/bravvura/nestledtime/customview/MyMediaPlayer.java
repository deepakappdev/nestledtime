package com.bravvura.nestledtime.customview;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.bravvura.nestledtime.utils.MyLogs;


/**
 * Created by android on 23/3/17.
 */

public class MyMediaPlayer {
    String TAG = "MyMediaPlayer";
    VideoView videoView;
    private int currentIndex;
    MediaPlayerCallBack mediaPlayerCallBack;


    public VideoView getMediaPlayer() {
        return videoView;
    }

    public MyMediaPlayer(Context context) {
        videoView = new VideoView(context);
        MyLogs.d(TAG, "Initialize");
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                MyLogs.d(TAG, "Prepared");
//                mediaPlayer = mp;
                mp.start();
                mp.setLooping(true);
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                MyLogs.d(TAG, "Complete");
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
                MyLogs.d(TAG, "Error: " + what + " Extra : " + extra);
                return true;
            }
        });
    }

    public void startPlayer(Uri mediaPath, MediaPlayerCallBack callBack) {
        this.mediaPlayerCallBack = callBack;
        MyLogs.d(TAG, "Starting " + mediaPath);

        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra) {
                MyLogs.d(TAG, "INFO: " + what + " Extra : " + extra);
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {
                        MyLogs.d(TAG, "Rendering Start");
                        if (mediaPlayerCallBack != null)
                            mediaPlayerCallBack.onRenderStart(currentIndex);
                        return true;
                    }
                }
                return false;
            }
        });
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayerCallBack != null)
                    mediaPlayerCallBack.onClick();
            }
        });

        videoView.setVideoURI(mediaPath);
//        videoView.requestFocus();
//        videoView.start();
    }

    public void stopPlayer() {
        videoView.stopPlayback();
        if (mediaPlayerCallBack != null) {
            mediaPlayerCallBack.onStop();
        }
        MyLogs.d(TAG, "Stop");
    }

    public void removeFromParent() {
        try {
            MyLogs.d(TAG, "Removing from Parent");
            if (getMediaPlayer().getParent() != null) {
                ((ViewGroup) getMediaPlayer().getParent()).removeView(getMediaPlayer());
                ((ViewGroup) getMediaPlayer().getParent()).removeAllViews();
            }
        } catch (Exception x) {
        }
    }

    public void setItemIndex(int adapterPosition) {
        currentIndex = adapterPosition;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public interface MediaPlayerCallBack {
        void onRenderStart(int currentIndex);

        void onStop();

        void onClick();
    }
}
