package com.bravvura.nestledtime.userstory.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.customview.MyMediaPlayer;
import com.bravvura.nestledtime.mediagallery.model.MEDIA_CELL_TYPE;
import com.bravvura.nestledtime.mediagallery.model.MediaModel;
import com.bravvura.nestledtime.userstory.customview.UserStoryMediaView;
import com.bravvura.nestledtime.userstory.listener.OnMediaClickListener;
import com.bravvura.nestledtime.userstory.model.UserStoryElement;
import com.bravvura.nestledtime.userstory.model.UserStoryElementType;
import com.bravvura.nestledtime.userstory.model.UserStoryMediaModel;
import com.bravvura.nestledtime.userstory.ui.activity.UserStoryMediaPagerActivity;
import com.bravvura.nestledtime.utils.Constants;
import com.bravvura.nestledtime.utils.MyLogs;
import com.bravvura.nestledtime.utils.StringUtils;
import com.bravvura.nestledtime.utils.Utils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 26-02-2018.
 */

public class UserStoryElementListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TITLE = 1, VIEW_TEXT = 2, VIEW_MEDIA = 3, VIEW_LOCATION = 4, VIEW_AUDIO = 5, VIEW_DATE = 6, VIEW_BLANK = 7;
    private MyMediaPlayer mediaPlayer;
    private UserStoryMediaView lastMediaPlayerView;
    OnMediaClickListener mediaClickListener;
    ArrayList<UserStoryElement> userStoryElements = new ArrayList<>();
    public int middlePosition;
    public boolean showMediaPlayer;

    public UserStoryElementListAdapter(Context context, OnMediaClickListener mediaClickListener) {
        this.mediaClickListener = mediaClickListener;
        mediaPlayer = new MyMediaPlayer(context);

    }

    public void setResult(ArrayList<UserStoryElement> userStoryElements) {
        this.userStoryElements = userStoryElements;
    }

    public void appendResult(ArrayList<UserStoryElement> userStoryElements) {
        if (this.userStoryElements == null)
            this.userStoryElements = new ArrayList<>();
        this.userStoryElements.addAll(userStoryElements);
    }

    public void addResult(UserStoryElement userStoryElement) {
        if (this.userStoryElements == null)
            this.userStoryElements = new ArrayList<>();
        this.userStoryElements.add(userStoryElement);
    }

    public void removeIndex(int index) {
        if (!Utils.isEmpty(userStoryElements) && userStoryElements.size() > index) {
//            userStoryElements.get(index).isdeleted = true;
            userStoryElements.remove(index);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (userStoryElements.get(position).isdeleted)
            return VIEW_BLANK;
        switch (userStoryElements.get(position).elementType) {
            case ELEMENT_TYPE_AUDIO:
                return VIEW_AUDIO;
            case ELEMENT_TYPE_DATE:
                return VIEW_DATE;
            case ELEMENT_TYPE_LOCATION:
                return VIEW_LOCATION;
            case ELEMENT_TYPE_MEDIA:
                return VIEW_MEDIA;
            case ELEMENT_TYPE_TITLE:
                return VIEW_TITLE;
            case ELEMENT_TYPE_TEXT:
                return VIEW_TEXT;
            default:
                return VIEW_TEXT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_MEDIA) {
            return new MediaViewHolder(View.inflate(parent.getContext(), R.layout.cell_user_story_media, null));
        } else if (viewType == VIEW_DATE) {
            return new DateViewHolder(View.inflate(parent.getContext(), R.layout.cell_user_story_location, null));
        } else if (viewType == VIEW_LOCATION) {
            return new LocationViewHolder(View.inflate(parent.getContext(), R.layout.cell_user_story_location, null));
        } else if (viewType == VIEW_TEXT) {
            return new TextViewHolder(View.inflate(parent.getContext(), R.layout.cell_user_story_text, null));
        } else if (viewType == VIEW_TITLE) {
            return new TextViewHolder(View.inflate(parent.getContext(), R.layout.cell_user_story_title, null));
        } else {
            return new BlankViewHolder(new View(parent.getContext()));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MediaViewHolder) {
            ((MediaViewHolder) holder).populateMedia(userStoryElements.get(position).mediaModel);
        } else if (holder instanceof LocationViewHolder) {
            Glide.with(holder.itemView.getContext())
                    .load(Utils.getStaticMapUrl(userStoryElements.get(position).addressModel.latLng))
                    .into(((LocationViewHolder) holder).imageView);
        } else if (holder instanceof TextViewHolder) {
            if (userStoryElements.get(position).elementType == UserStoryElementType.ELEMENT_TYPE_TITLE) {
                ((TextViewHolder) holder).edit_text.setHint("Enter your memory's title here");
            } else if (userStoryElements.get(position).elementType == UserStoryElementType.ELEMENT_TYPE_TEXT) {
                ((TextViewHolder) holder).edit_text.setHint("Text");
            }
            ((TextViewHolder) holder).edit_text.setText(userStoryElements.get(position).textModel.data);
        }
    }

    @Override
    public int getItemCount() {
        return userStoryElements.size();
    }

    public ArrayList<UserStoryElement> getAllItems() {
        return userStoryElements;
    }

    public void updateHighlightPosition(int highlightPosition) {
        middlePosition = highlightPosition;

    }

    public void checkStopForMedia() {
        if(lastMediaPlayerView!=null && lastMediaPlayerView.getPosition()!=middlePosition) {
            lastMediaPlayerView.releaseMediaPlayer(middlePosition);
        }
    }


    class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaClickListener != null)
                        mediaClickListener.onClick(userStoryElements.get(getAdapterPosition()), getAdapterPosition());
                }
            });
            if (itemView.findViewById(R.id.ic_close) != null)
                itemView.findViewById(R.id.ic_close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRemoveClick();
                    }
                });
        }

        protected void onRemoveClick() {
            if (mediaClickListener != null)
                mediaClickListener.onRemoveClick(userStoryElements.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    class DateViewHolder extends BaseViewHolder {

        public DateViewHolder(View itemView) {
            super(itemView);
        }
    }

    class BlankViewHolder extends BaseViewHolder {

        public BlankViewHolder(View itemView) {
            super(itemView);
        }
    }

    class TextViewHolder extends BaseViewHolder {

        public final EditText edit_text;

        public TextViewHolder(View itemView) {
            super(itemView);
            edit_text = itemView.findViewById(R.id.edit_text);
            edit_text.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    userStoryElements.get(getAdapterPosition()).textModel.data = s.toString();
                }
            });

        }

        @Override
        protected void onRemoveClick() {
            if (userStoryElements.get(getAdapterPosition()).elementType == UserStoryElementType.ELEMENT_TYPE_TEXT) {
                super.onRemoveClick();
            } else if (userStoryElements.get(getAdapterPosition()).elementType == UserStoryElementType.ELEMENT_TYPE_TITLE) {
                edit_text.setText("");
            }
        }
    }

    class LocationViewHolder extends BaseViewHolder {

        private final ImageView imageView;

        public LocationViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }

    class MediaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final UserStoryMediaView mediaView1, mediaView2, mediaView3, mediaView4;
        public final TextView textPicMore, textTitle;
        private final LinearLayout layoutMorePic;
        private final View textEdit;
        public View layouotImage4;
        private UserStoryMediaModel userStoryMediaModel;


        public MediaViewHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.text_title);
            mediaView1 = itemView.findViewById(R.id.media_view_1);
            layoutMorePic = itemView.findViewById(R.id.layout_more_pic);
            mediaView2 = itemView.findViewById(R.id.media_view_2);
            mediaView3 = itemView.findViewById(R.id.media_view_3);
            mediaView4 = itemView.findViewById(R.id.media_view_4);
            textPicMore = itemView.findViewById(R.id.text_more_pic);
            layouotImage4 = itemView.findViewById(R.id.layout_image_4);
            (textEdit = itemView.findViewById(R.id.text_edit)).setOnClickListener(this);

            mediaView1.setOnClickListener(this);
            mediaView2.setOnClickListener(this);
            mediaView3.setOnClickListener(this);
            mediaView4.setOnClickListener(this);

        }

        void showMediaGallery(final int index) {
            if (mediaClickListener != null)
                mediaClickListener.onClick(userStoryElements.get(getAdapterPosition()), index);
        }

        public void populateMedia(UserStoryMediaModel userStoryMediaModel) {
            this.userStoryMediaModel = userStoryMediaModel;
            textTitle.setText(userStoryMediaModel.title);
            showMediaImage();
            if (showMediaPlayer && middlePosition == getAdapterPosition()) {
                UserStoryMediaView userStoryMediaView = getVideoView();
                if (userStoryMediaView != null) {
                    if (UserStoryElementListAdapter.this.lastMediaPlayerView != null) {
                        UserStoryElementListAdapter.this.lastMediaPlayerView.releaseMediaPlayer(getAdapterPosition());
                    }
                    UserStoryElementListAdapter.this.lastMediaPlayerView = userStoryMediaView;
                    lastMediaPlayerView.playMediaPlayer(mediaPlayer, getAdapterPosition());
                }
            } else {
                if (lastMediaPlayerView != null && lastMediaPlayerView.getPosition() != middlePosition)
                    lastMediaPlayerView.releaseMediaPlayer(getAdapterPosition());
            }
        }

        private void showMediaImage() {
            ArrayList<MediaModel> mediaModels = userStoryMediaModel.mediaModels;
            if (mediaModels.size() == 1) {
                mediaView1.showMedia(mediaModels.get(0));
                layoutMorePic.setVisibility(View.GONE);
                mediaView2.hideView();
                mediaView3.hideView();
                mediaView4.hideView();
                layouotImage4.setVisibility(View.GONE);
                textPicMore.setVisibility(View.GONE);
            } else {
                layoutMorePic.setVisibility(View.VISIBLE);
                if (mediaModels.size() == 2) {
                    mediaView1.showMedia(mediaModels.get(0));
                    mediaView2.showMedia(mediaModels.get(1));
                    mediaView3.hideView();
                    mediaView4.hideView();
                    layouotImage4.setVisibility(View.GONE);
                    textPicMore.setVisibility(View.GONE);
                } else if (mediaModels.size() == 3) {
                    mediaView1.showMedia(mediaModels.get(0));
                    mediaView2.showMedia(mediaModels.get(1));
                    mediaView3.showMedia(mediaModels.get(2));
                    mediaView4.hideView();
                    layouotImage4.setVisibility(View.GONE);
                    textPicMore.setVisibility(View.GONE);
                } else if (mediaModels.size() == 4) {
                    mediaView1.showMedia(mediaModels.get(0));
                    mediaView2.showMedia(mediaModels.get(1));
                    mediaView3.showMedia(mediaModels.get(2));
                    mediaView4.showMedia(mediaModels.get(3));
                    layouotImage4.setVisibility(View.VISIBLE);
                    textPicMore.setVisibility(View.GONE);
                } else if (mediaModels.size() > 4) {
                    mediaView1.showMedia(mediaModels.get(0));
                    mediaView2.showMedia(mediaModels.get(1));
                    mediaView3.showMedia(mediaModels.get(2));
                    mediaView4.showMedia(mediaModels.get(3));
                    layouotImage4.setVisibility(View.VISIBLE);
                    textPicMore.setVisibility(View.VISIBLE);
                    textPicMore.setText(mediaModels.size() - 4 + "+");
                }
            }
        }

        public UserStoryMediaView getVideoView() {
            if (mediaView1.mediaModel != null && mediaView1.mediaModel.mediaCellType == MEDIA_CELL_TYPE.TYPE_VIDEO)
                return mediaView1;
            else if (mediaView2.mediaModel != null && mediaView2.mediaModel.mediaCellType == MEDIA_CELL_TYPE.TYPE_VIDEO)
                return mediaView2;
            else if (mediaView3.mediaModel != null && mediaView3.mediaModel.mediaCellType == MEDIA_CELL_TYPE.TYPE_VIDEO)
                return mediaView3;
            else if (mediaView4.mediaModel != null && mediaView4.mediaModel.mediaCellType == MEDIA_CELL_TYPE.TYPE_VIDEO)
                return mediaView4;
            else return null;
        }

        @Override
        public void onClick(View v) {
            if (v.equals(mediaView1))
                showMediaGallery(0);
            else if (v.equals(mediaView2))
                showMediaGallery(1);
            else if (v.equals(mediaView3))
                showMediaGallery(2);
            else if (v.equals(mediaView4))
                showMediaGallery(3);
            else if (v.equals(textEdit)) {
                mediaClickListener.onEditClick(userStoryElements.get(getAdapterPosition()), getAdapterPosition());
            }
        }
    }
}
