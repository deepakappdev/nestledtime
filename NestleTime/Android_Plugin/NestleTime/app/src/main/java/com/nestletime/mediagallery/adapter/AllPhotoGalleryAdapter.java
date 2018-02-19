package com.nestletime.mediagallery.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nestletime.R;
import com.nestletime.mediagallery.filesystem.CursorManager;
import com.nestletime.mediagallery.listener.MediaElementClick;
import com.nestletime.mediagallery.model.MEDIA_CELL_TYPE;
import com.nestletime.mediagallery.model.MediaModel;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Deepak Saini on 07-02-2018.
 */


public class AllPhotoGalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEADER_VIEW = 1;
    private static final int CELL_VIEW = 2;

    private ArrayList<MediaModel> mediaModels;
    private final int cellWidth;
    private MediaElementClick mediaElementClick;

    public AllPhotoGalleryAdapter(ArrayList<MediaModel> mediaModels, int width) {
        this.mediaModels = mediaModels;
        this.cellWidth = width;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == CELL_VIEW)
            return new MediaViewHolder(View.inflate(parent.getContext(), R.layout.cell_media_gallery, null));
        else
            return new HeaderViewHolder(View.inflate(parent.getContext(), R.layout.cell_header_gallery, null));
    }

    @Override
    public int getItemViewType(int position) {
        if (mediaModels.get(position).mediaCellType == MEDIA_CELL_TYPE.TYPE_HEADER)
            return HEADER_VIEW;
        else return CELL_VIEW;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MediaViewHolder) {
            ((MediaViewHolder) holder).layoutContent.getLayoutParams().height = cellWidth;
            ((MediaViewHolder) holder).layoutContent.getLayoutParams().width = cellWidth;
            ((MediaViewHolder) holder).populate(mediaModels.get(position));
        } else if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).textDate.setText(mediaModels.get(position).getDate());
        }
    }

    @Override
    public int getItemCount() {
        return mediaModels.size();
    }

    public void setResult(ArrayList<MediaModel> mediaModels) {
        this.mediaModels = mediaModels;
    }

    public void setOnMediaClickListener(MediaElementClick mediaElementClick) {
        this.mediaElementClick = mediaElementClick;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView textDate;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            textDate = itemView.findViewById(R.id.text_date);
        }
    }

    class MediaViewHolder extends RecyclerView.ViewHolder {
        TextView textDuration;
        ImageView imageItem;
        View viewSelected;
        ImageView imageSelected;
        View layoutContent;
        ImageView iconVideo;
        private MediaModel mediaModel;

        MediaViewHolder(View itemView) {
            super(itemView);
            layoutContent = itemView.findViewById(R.id.layout_content);
            imageItem = itemView.findViewById(R.id.image_item);
            viewSelected = itemView.findViewById(R.id.view_select);
            imageSelected = itemView.findViewById(R.id.image_selected);
            iconVideo = itemView.findViewById(R.id.icon_video);
            textDuration = itemView.findViewById(R.id.text_album_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaElementClick != null)
                        mediaElementClick.onClick(getAdapterPosition(), mediaModel);
                }
            });
        }

        void populate(MediaModel mediaModel) {
            this.mediaModel = mediaModel;
            viewSelected.setSelected(mediaModel.isSelected());
            Glide.with(itemView.getContext()).load(new File(mediaModel.getPathFile())).into(imageItem);
            if (mediaModel.mediaCellType == MEDIA_CELL_TYPE.TYPE_VIDEO) {
                iconVideo.setVisibility(View.VISIBLE);
                textDuration.setVisibility(View.VISIBLE);
                if (mediaModel.duration == 0 && mediaModel.getId() > 0) {
                    mediaModel.duration = CursorManager.getVideoDuration(itemView.getContext(), mediaModel.getId()) / 1000;
                }

                if (mediaModel.duration > 0) {
                    textDuration.setText(String.format("%02d", mediaModel.duration / 60) + ":"
                            + String.format("%02d", mediaModel.duration % 60));
                }
            } else {
                iconVideo.setVisibility(View.GONE);
                textDuration.setVisibility(View.GONE);
            }
        }

    }
}
