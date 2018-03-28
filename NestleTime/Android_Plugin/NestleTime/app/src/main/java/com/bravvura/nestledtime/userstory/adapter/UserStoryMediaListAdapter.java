package com.bravvura.nestledtime.userstory.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.customview.MyMediaPlayer;
import com.bravvura.nestledtime.mediagallery.listener.MediaElementClick;
import com.bravvura.nestledtime.mediagallery.model.MEDIA_CELL_TYPE;
import com.bravvura.nestledtime.mediagallery.model.MediaModel;
import com.bravvura.nestledtime.userstory.model.UserStoryMediaModel;
import com.bravvura.nestledtime.utils.CloudinaryManager;
import com.bravvura.nestledtime.utils.MyLogs;
import com.bravvura.nestledtime.utils.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 12-02-2018.
 */

public class UserStoryMediaListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TITLE = 1;
    private static final int VIEW_VIDEO = 2;
    private static final int VIEW_IMAGE = 3;
    private static final int VIEW_NONE = 4;
    private final MediaElementClick mediaElementClick;
    private final Context context;

    private UserStoryMediaModel userStoryMediaModel;
    private MyMediaPlayer mediaPlayer;

    public UserStoryMediaListAdapter(Context context, MediaElementClick mediaElementClick) {
        this.context = context;
        mediaPlayer = new MyMediaPlayer(context);
        this.mediaElementClick = mediaElementClick;
    }

    public void setResults(UserStoryMediaModel userStoryMediaModel) {
        this.userStoryMediaModel = userStoryMediaModel;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return VIEW_TITLE;
        MediaModel model = userStoryMediaModel.mediaModels.get(position - 1);
        if (model.isDeleted)
            return VIEW_NONE;
        else if (model.mediaCellType == MEDIA_CELL_TYPE.TYPE_VIDEO)
            return VIEW_VIDEO;
        else
            return VIEW_IMAGE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_NONE)
            return new BlankViewHolder(new View(context));
        else if (viewType == VIEW_TITLE)
            return new TextViewHolder(View.inflate(context, R.layout.cell_user_story_text, null));
        else if (viewType == VIEW_IMAGE)
            return new ImageViewHolder(View.inflate(context, R.layout.cell_story_media_list_image, null));
        else {
            return new VideoViewHolder(View.inflate(context, R.layout.cell_story_media_list_video, null));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TextViewHolder) {
            ((TextViewHolder) holder).edit_text.setText(userStoryMediaModel.title);
            return;
        }
        MediaModel mediaModel = userStoryMediaModel.mediaModels.get(position - 1);
        if (holder instanceof VideoViewHolder) {
            ((VideoViewHolder) holder).populateMediaItem(mediaModel);
//            ((VideoViewHolder) holder).releaseMediaPlayer();
        } else if (holder instanceof ImageViewHolder) {
            ((ImageViewHolder) holder).populateMediaItem(mediaModel);
        }
    }

    @Override
    public int getItemCount() {
        return userStoryMediaModel.mediaModels.size() + 1;
    }

    public MediaModel getItem(int index) {
        return userStoryMediaModel.mediaModels.get(index);
    }

    class BlankViewHolder extends RecyclerView.ViewHolder {

        BlankViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        View imageClose;
        EditText editDescription;
        ImageView imageView;
        public MediaModel mediaModel;

        ImageViewHolder(final View itemView) {
            super(itemView);
            editDescription = itemView.findViewById(R.id.edit_description);
            imageView = itemView.findViewById(R.id.image_view);
            if (itemView.findViewById(R.id.text_edit) != null)
                itemView.findViewById(R.id.text_edit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mediaElementClick.onEditClick(userStoryMediaModel.mediaModels.indexOf(mediaModel), mediaModel);
                    }
                });
            imageClose = itemView.findViewById(R.id.image_remove);
            imageClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaElementClick.onRemoveClick(userStoryMediaModel.mediaModels.indexOf(mediaModel), mediaModel);
                }
            });
            editDescription.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mediaModel.setTitle(s.toString());
                }
            });
        }

        void populateMediaItem(MediaModel mediaModel) {
            this.mediaModel = mediaModel;
            editDescription.setText(mediaModel.getTitle());
            editDescription.clearFocus();
            itemView.clearFocus();
            imageView.setVisibility(View.VISIBLE);
            if (mediaModel.isEdited) {
                Glide.with(context).load(new File(mediaModel.getPathFile())).into(imageView);
            } else {
                switch (mediaModel.sourceType) {
                    case TYPE_CLOUD:
                        if (mediaModel.mediaCellType == MEDIA_CELL_TYPE.TYPE_IMAGE) {
                            Glide.with(context).load(mediaModel.getUrl()).into(imageView);
                        } else {
                            Glide.with(context).load(CloudinaryManager.getVideoThumbnail(mediaModel.getPublicId())).asBitmap()
                                    .into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                            imageView.setImageBitmap(resource);
                                        }

                                        @Override
                                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                            super.onLoadFailed(e, errorDrawable);
                                        }
                                    });
//                            Glide.with(context).load(CloudinaryManager.getVideoThumbnail(mediaModel.getPublicId())).into(imageView);
                        }
                    case TYPE_FACEBOOK:
                    case TYPE_INSTAGRAM:
                        Glide.with(context).load(mediaModel.getUrl()).into(imageView);
                        break;
                    case TYPE_LOCAL:
                        Glide.with(context).load(new File(mediaModel.getPathFile())).into(imageView);
                        break;
                }

            }
        }
    }

    class VideoViewHolder extends ImageViewHolder {
        private final ViewGroup layout_video;
        ImageView image_play;

        VideoViewHolder(final View itemView) {
            super(itemView);
            layout_video = itemView.findViewById(R.id.layout_video);
            image_play = itemView.findViewById(R.id.image_play);
            image_play.setVisibility(View.GONE);
            image_play.setImageResource(R.drawable.ic_play_white);
            image_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mediaPlayer.getMediaPlayer().isPlaying() && mediaPlayer.getCurrentIndex() == getAdapterPosition()) {
                        mediaPlayer.getMediaPlayer().pause();
                        image_play.setVisibility(View.VISIBLE);
                        image_play.setImageResource(R.drawable.ic_play_white);
                    } else {
                        image_play.setVisibility(View.GONE);
                        image_play.setImageResource(R.drawable.ic_pause_white);
                        if (mediaPlayer.getCurrentIndex() == getAdapterPosition()) {
                            mediaPlayer.getMediaPlayer().start();
                        } else {
                            String filePath = "";
                            if (!StringUtils.isNullOrEmpty(mediaModel.getPathFile())) {
                                filePath = mediaModel.getPathFile();
                                playMediaPlayer(Uri.parse(filePath));
                            } else if (!StringUtils.isNullOrEmpty(mediaModel.getUrl())) {
                                filePath = mediaModel.getUrl();
                                playMediaPlayer(filePath);
                            }
                        }
                    }
                }
            });

            layout_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaPlayer.getMediaPlayer().isPlaying())
                        image_play.setImageResource(R.drawable.ic_pause_white);
                    else
                        image_play.setImageResource(R.drawable.ic_play_white);
                    image_play.setVisibility(View.VISIBLE);
                }
            });
        }

        private MyMediaPlayer.MediaPlayerCallBack mediaCallBack = new MyMediaPlayer.MediaPlayerCallBack() {
            int currentIndex;

            @Override
            public void onRenderStart(int currentIndex) {
                this.currentIndex = currentIndex;
                imageView.setVisibility(View.INVISIBLE);
                image_play.setImageResource(R.drawable.ic_pause_white);
                image_play.setVisibility(View.GONE);
            }

            @Override
            public void onStop() {
                imageView.setVisibility(View.VISIBLE);
                image_play.setImageResource(R.drawable.ic_play_white);
                image_play.setVisibility(View.VISIBLE);
            }

            @Override
            public void onClick() {
            }
        };

        void attachMediaPlayer() {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            layout_video.addView(mediaPlayer.getMediaPlayer(), layoutParams);
        }

        void playMediaPlayer(final Uri path) {
            releaseMediaPlayer();
            mediaPlayer.setItemIndex(getAdapterPosition());
            attachMediaPlayer();
            imageView.setVisibility(View.VISIBLE);
            UserStoryMediaListAdapter.this.mediaPlayer.startPlayer(path, mediaCallBack);
        }

        void playMediaPlayer(String path) {
            releaseMediaPlayer();
            mediaPlayer.setItemIndex(getAdapterPosition());
            attachMediaPlayer();
            imageView.setVisibility(View.VISIBLE);
            UserStoryMediaListAdapter.this.mediaPlayer.startPlayer(path, mediaCallBack);
        }

        void releaseMediaPlayer() {
//            if (layout_video.getChildCount() > 0) {
            MyLogs.d("MyMediaPlayer", "Releasing" + getAdapterPosition());
            layout_video.removeAllViews();
            mediaPlayer.removeFromParent();
            mediaPlayer.stopPlayer();
//            }
        }
    }

    class TextViewHolder extends RecyclerView.ViewHolder {

        public final EditText edit_text;

        public TextViewHolder(View itemView) {
            super(itemView);
            edit_text = itemView.findViewById(R.id.edit_text);
            edit_text.setHint("Title");
            edit_text.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    userStoryMediaModel.title = s.toString();
                }
            });
            itemView.findViewById(R.id.ic_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edit_text.setText("");
                }
            });

        }
    }
}
