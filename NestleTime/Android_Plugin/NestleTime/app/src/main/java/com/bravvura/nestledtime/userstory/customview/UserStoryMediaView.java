package com.bravvura.nestledtime.userstory.customview;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.customview.MyMediaPlayer;
import com.bravvura.nestledtime.mediagallery.model.MEDIA_CELL_TYPE;
import com.bravvura.nestledtime.mediagallery.model.MediaModel;
import com.bravvura.nestledtime.utils.CloudinaryManager;
import com.bravvura.nestledtime.utils.MyLogs;
import com.bravvura.nestledtime.utils.StringUtils;
import com.bumptech.glide.Glide;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 08-03-2018.
 */

public class UserStoryMediaView extends LinearLayout {
    private ImageView imageView;
    public MediaModel mediaModel;
    private MyMediaPlayer mediaPlayer;
    private RelativeLayout layout_video;
    private MyMediaPlayer.MediaPlayerCallBack mediaCallBack = new MyMediaPlayer.MediaPlayerCallBack() {
        @Override
        public void onRenderStart(int currentIndex) {
            imageView.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onStop() {
            imageView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onClick() {

        }
    };
    private int position = -1;

    public UserStoryMediaView(Context context) {
        super(context);
        initView();
    }

    public UserStoryMediaView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public UserStoryMediaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public UserStoryMediaView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.custom_user_story_media_view, this, true);
        imageView = findViewById(R.id.image_view);
        layout_video = findViewById(R.id.layout_video);
    }

    public void showMedia(MediaModel mediaModel) {
        if (mediaModel != null) {
            this.mediaModel = mediaModel;
            setVisibility(View.VISIBLE);
            post(showMediaRunnable);
        }

    }

    private int width;
    private int height;
    Runnable showMediaRunnable = new Runnable() {
        @Override
        public void run() {
            width = getWidth();
            height = getHeight();
            if (mediaModel != null) {
                imageView.setVisibility(View.VISIBLE);
                if (mediaModel.mediaCellType == MEDIA_CELL_TYPE.TYPE_IMAGE) {
                    if(!StringUtils.isNullOrEmpty(mediaModel.getPathFile())) {
                        Glide.with(getContext()).load(mediaModel.getPathFile()).into(imageView);
                    } else {
                        String url = CloudinaryManager.getFacesThumbnail(mediaModel.getPublicId(), width, height);
                        Glide.with(getContext()).load(url).into(imageView);
                    }
                } else {
                    if(!StringUtils.isNullOrEmpty(mediaModel.getPathFile())) {
                        Glide.with(getContext()).load(mediaModel.getPathFile()).into(imageView);
                    } else {
                        String url = CloudinaryManager.getVideoThumbnail(mediaModel.getPublicId(), width, height);
                        Glide.with(getContext()).load(url).into(imageView);
                    }
                }
            }
        }
    };

    public void releaseMediaPlayer(int adapterPosition) {
        if (mediaPlayer != null || layout_video.getChildCount() > 0) {
            MyLogs.d("MyMediaPlayer", "Releasing" + adapterPosition);
            layout_video.removeAllViews();
            if (mediaPlayer != null && mediaPlayer.getCurrentIndex() != adapterPosition) {
                MyLogs.d("MyMediaPlayer", "Released");
                mediaPlayer.removeFromParent();
                mediaPlayer.stopPlayer();
            }
            mediaPlayer = null;
        }
        imageView.setVisibility(View.VISIBLE);
    }

    public void playMediaPlayer(MyMediaPlayer mediaPlayer, int adapterPosition) {
        position = adapterPosition;
        if (this.mediaPlayer == null || this.mediaPlayer.getCurrentIndex() != adapterPosition) {
//            releaseMediaPlayer(adapterPosition);
            this.mediaPlayer = mediaPlayer;
            mediaPlayer.removeFromParent();
            mediaPlayer.setItemIndex(adapterPosition);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            layout_video.addView(mediaPlayer.getMediaPlayer(), layoutParams);
            imageView.setVisibility(View.VISIBLE);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    width = getWidth();
                    height = getHeight();
                    String url = CloudinaryManager.getVideoUrl(mediaModel.getPublicId(), width, height);
                    if (UserStoryMediaView.this.mediaPlayer != null)
                        UserStoryMediaView.this.mediaPlayer.startPlayer(url, mediaCallBack);
                }
            });
        }
    }

    public void hideView() {
        setVisibility(View.GONE);
        mediaModel = null;
        imageView.setImageBitmap(null);
        position = -1;
    }

    public int getPosition() {
        return position;
    }

    public boolean isPlaying() {
        return mediaPlayer!=null && mediaPlayer.getMediaPlayer()!=null && mediaPlayer.getMediaPlayer().isPlaying();
    }

    public void pause() {
        if(mediaPlayer!=null && mediaPlayer.getMediaPlayer()!=null)
            mediaPlayer.getMediaPlayer().pause();
    }

    public void start() {
        if(mediaPlayer!=null && mediaPlayer.getMediaPlayer()!=null)
            mediaPlayer.getMediaPlayer().start();
    }
}
