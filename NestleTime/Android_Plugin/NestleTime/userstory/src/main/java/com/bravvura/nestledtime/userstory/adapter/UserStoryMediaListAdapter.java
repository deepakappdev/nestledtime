package com.bravvura.nestledtime.userstory.adapter;

import android.content.Context;
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
import com.bravvura.nestledtime.mediagallery.model.MEDIA_SOURCE_TYPE;
import com.bravvura.nestledtime.mediagallery.model.MediaModel;
import com.bravvura.nestledtime.utils.MyFileSystem;
import com.bravvura.nestledtime.utils.MyLogs;
import com.bravvura.nestledtime.utils.StringUtils;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 12-02-2018.
 */

public class UserStoryMediaListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_VIDEO = 1;
    private static final int VIEW_IMAGE = 2;
    private static final int VIEW_NONE = 3;
    private final MediaElementClick onEditClick;

    private ArrayList<MediaModel> mediaModels;
    private MyMediaPlayer mediaPlayer;

    public UserStoryMediaListAdapter(Context context, MediaElementClick onEditClick) {
        mediaPlayer = new MyMediaPlayer(context);
        this.onEditClick = onEditClick;
    }

    public void setResults(ArrayList<MediaModel> mediaModels) {
        this.mediaModels = mediaModels;
    }

    @Override
    public int getItemViewType(int position) {
        if (mediaModels.get(position).isDeleted)
            return VIEW_NONE;
        else if (mediaModels.get(position).mediaCellType == MEDIA_CELL_TYPE.TYPE_VIDEO)
            return VIEW_VIDEO;
        else
            return VIEW_IMAGE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_NONE)
            return new BlankViewHolder(new View(parent.getContext()));
        else if (viewType == VIEW_IMAGE)
            return new ImageViewHolder(View.inflate(parent.getContext(), R.layout.cell_story_media_list_image, null));
        else {
            return new VideoViewHolder(View.inflate(parent.getContext(), R.layout.cell_story_media_list_video, null));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MediaModel mediaModel = mediaModels.get(position);
        if (holder instanceof VideoViewHolder) {
            ((VideoViewHolder) holder).populateMediaItem(mediaModel);
            ((VideoViewHolder) holder).releaseMediaPlayer();
        } else if (holder instanceof ImageViewHolder) {
            ((ImageViewHolder) holder).populateMediaItem(mediaModel);
        }
    }

    @Override
    public int getItemCount() {
        return mediaModels.size();
    }

    public MediaModel getItem(int index) {
        return mediaModels.get(index);
    }

//    public void stopMediaPlayBack(int middlePosition) {
//        mediaPlayer.removeFromParent();
//        mediaPlayer.stopPlayer();
//    }

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

        ImageViewHolder(View itemView) {
            super(itemView);
            editDescription = itemView.findViewById(R.id.edit_description);
            imageView = itemView.findViewById(R.id.image_view);
            if (itemView.findViewById(R.id.text_edit) != null)
                itemView.findViewById(R.id.text_edit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onEditClick.onClick(getAdapterPosition(), mediaModel);
                    }
                });
            imageClose = itemView.findViewById(R.id.image_remove);
            imageClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaModel.sourceType == MEDIA_SOURCE_TYPE.TYPE_LOCAL
                            || mediaModel.sourceType == MEDIA_SOURCE_TYPE.TYPE_FACEBOOK
                            || mediaModel.sourceType == MEDIA_SOURCE_TYPE.TYPE_INSTAGRAM) {
                        if (mediaModel.isEdited) {
                            MyFileSystem.removeFile(mediaModel.getPathFile());
                        }
                        mediaModels.remove(mediaModel);
                        notifyItemRemoved(getAdapterPosition());
                    } else {
                        mediaModel.isDeleted = true;
                        notifyItemChanged(getAdapterPosition());
                    }
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
                    mediaModel.description = s.toString();
                }
            });
        }

        void populateMediaItem(MediaModel mediaModel) {
            this.mediaModel = mediaModel;
            editDescription.setText(mediaModel.description);
            editDescription.clearFocus();
            itemView.clearFocus();
            if (mediaModel.isEdited || mediaModel.sourceType != MEDIA_SOURCE_TYPE.TYPE_CLOUD) {
                Glide.with(itemView.getContext()).load(new File(mediaModel.getPathFile())).into(imageView);
            } else {
                Glide.with(itemView.getContext()).load(mediaModel.getUrl()).into(imageView);
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
            image_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playMediaPlayer(Uri.parse(mediaModel.getPathFile()));
                }
            });
        }

        private MyMediaPlayer.MediaPlayerCallBack mediaCallBack = new MyMediaPlayer.MediaPlayerCallBack() {
            int currentIndex;

            @Override
            public void onRenderStart(int currentIndex) {
                this.currentIndex = currentIndex;
                imageView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onStop() {
                imageView.setVisibility(View.VISIBLE);
            }
        };

        void playMediaPlayer(final Uri path) {
            releaseMediaPlayer();
            mediaPlayer.setItemIndex(getAdapterPosition());
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            layout_video.addView(mediaPlayer.getMediaPlayer(), layoutParams);
            imageView.setVisibility(View.VISIBLE);
            UserStoryMediaListAdapter.this.mediaPlayer.startPlayer(path, mediaCallBack);
/*
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                }
            });
*/
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
}
