package com.bravvura.nestledtime.userstory.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.mediagallery.model.MediaModel;
import com.bravvura.nestledtime.userstory.listener.OnMediaClickListener;
import com.bravvura.nestledtime.userstory.model.UserStoryElement;
import com.bravvura.nestledtime.userstory.model.UserStoryElementType;
import com.bravvura.nestledtime.userstory.model.UserStoryMediaModel;
import com.bravvura.nestledtime.utils.Utils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 26-02-2018.
 */

public class UserStoryElementListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TITLE = 1, VIEW_TEXT = 2, VIEW_MEDIA = 3, VIEW_LOCATION = 4, VIEW_AUDIO = 5, VIEW_DATE = 6, VIEW_BLANK = 7;
    OnMediaClickListener mediaClickListener;
    ArrayList<UserStoryElement> userStoryElements = new ArrayList<>();

    public UserStoryElementListAdapter(OnMediaClickListener mediaClickListener) {
        this.mediaClickListener = mediaClickListener;
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
            userStoryElements.get(index).isdeleted = true;
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

    private void showImage(ImageView imageView, String url) {
        imageView.setVisibility(View.VISIBLE);
        Glide.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }

    class MediaViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView1, imageView2, imageView3, imageView4;
        public final TextView textPicMore, textTitle;
        private final LinearLayout layoutMorePic;
        public View layouotImage4;

        public MediaViewHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.text_title);
            imageView1 = itemView.findViewById(R.id.image_view_1);
            layoutMorePic = itemView.findViewById(R.id.layout_more_pic);
            imageView2 = itemView.findViewById(R.id.image_view_2);
            imageView3 = itemView.findViewById(R.id.image_view_3);
            imageView4 = itemView.findViewById(R.id.image_view_4);
            textPicMore = itemView.findViewById(R.id.text_more_pic);
            layouotImage4 = itemView.findViewById(R.id.layout_image_4);
            itemView.findViewById(R.id.text_edit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaClickListener.onEditClick(userStoryElements.get(getAdapterPosition()), getAdapterPosition());

                }
            });
        }

        public void populateMedia(UserStoryMediaModel userStoryMediaModel) {
            hideAllViews();
            textTitle.setText(userStoryMediaModel.title);
            ArrayList<MediaModel> mediaModels = userStoryMediaModel.mediaModels;
            if (mediaModels.size() == 1) {
                showImage(imageView1, mediaModels.get(0).getUrl());
            } else {
                layoutMorePic.setVisibility(View.VISIBLE);
                if (mediaModels.size() == 2) {
                    showImage(imageView1, mediaModels.get(0).getUrl());
                    showImage(imageView2, mediaModels.get(1).getUrl());
                } else if (mediaModels.size() == 3) {
                    showImage(imageView1, mediaModels.get(0).getUrl());
                    showImage(imageView2, mediaModels.get(1).getUrl());
                    showImage(imageView3, mediaModels.get(2).getUrl());
                } else if (mediaModels.size() == 4) {
                    showImage(imageView1, mediaModels.get(0).getUrl());
                    showImage(imageView2, mediaModels.get(1).getUrl());
                    showImage(imageView3, mediaModels.get(2).getUrl());
                    showImage(imageView4, mediaModels.get(3).getUrl());
                    layouotImage4.setVisibility(View.VISIBLE);
                } else if (mediaModels.size() > 4) {
                    showImage(imageView1, mediaModels.get(0).getUrl());
                    showImage(imageView2, mediaModels.get(1).getUrl());
                    showImage(imageView3, mediaModels.get(2).getUrl());
                    showImage(imageView4, mediaModels.get(3).getUrl());
                    layouotImage4.setVisibility(View.VISIBLE);
                    textPicMore.setVisibility(View.VISIBLE);
                    textPicMore.setText(mediaModels.size() - 4 + "+");
                }
            }
        }

        private void hideAllViews() {
            layoutMorePic.setVisibility(View.GONE);
            imageView2.setVisibility(View.GONE);
            imageView3.setVisibility(View.GONE);
            imageView4.setVisibility(View.GONE);
            layouotImage4.setVisibility(View.GONE);
            textPicMore.setVisibility(View.GONE);
        }
    }
}
