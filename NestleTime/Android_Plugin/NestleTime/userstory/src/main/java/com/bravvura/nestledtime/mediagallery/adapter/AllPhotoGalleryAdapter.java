package com.bravvura.nestledtime.mediagallery.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.mediagallery.filesystem.CursorManager;
import com.bravvura.nestledtime.mediagallery.listener.MediaElementClick;
import com.bravvura.nestledtime.mediagallery.model.MEDIA_CELL_TYPE;
import com.bravvura.nestledtime.mediagallery.model.MediaModel;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Deepak Saini on 07-02-2018.
 */


public class AllPhotoGalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEADER_VIEW = 1;
    private static final int CAMERA_VIEW = 2;
    private static final int CELL_VIEW = 3;


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
        else if (viewType == CAMERA_VIEW)
            return new CameraViewHolder(View.inflate(parent.getContext(), R.layout.cell_camera_gallery, null));
        else
            return new HeaderViewHolder(View.inflate(parent.getContext(), R.layout.cell_header_gallery, null));
    }

    @Override
    public int getItemViewType(int position) {
        if (mediaModels.get(position).mediaCellType == MEDIA_CELL_TYPE.TYPE_HEADER)
            return HEADER_VIEW;
        else if (mediaModels.get(position).mediaCellType == MEDIA_CELL_TYPE.TYPE_CAMERA)
            return CAMERA_VIEW;
        else
            return CELL_VIEW;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MediaViewHolder) {
            ((MediaViewHolder) holder).layoutContent.getLayoutParams().height = cellWidth;
            ((MediaViewHolder) holder).layoutContent.getLayoutParams().width = cellWidth;
            ((MediaViewHolder) holder).populate(mediaModels.get(position));
        } else if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).textDate.setText(mediaModels.get(position).getDate());
        } else if (holder instanceof CameraViewHolder) {
            ((CameraViewHolder) holder).updateModel(mediaModels.get(position));
            ((CameraViewHolder) holder).updateSize(cellWidth, cellWidth);
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

    class CameraViewHolder extends RecyclerView.ViewHolder {
        public View layoutContent;
        private MediaModel mediaModel;

        public CameraViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaElementClick != null)
                        mediaElementClick.onClick(getAdapterPosition(), mediaModel);
                }
            });
            layoutContent = itemView.findViewById(R.id.layout_content);
        }

        public void updateModel(MediaModel mediaModel) {
            this.mediaModel = mediaModel;
        }

        public void updateSize(final int cellWidth, int cellHeight) {
            layoutContent.post(new Runnable() {
                @Override
                public void run() {
                    layoutContent.getLayoutParams().height = cellWidth;
                    layoutContent.getLayoutParams().width = cellWidth;
                }
            });
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
