package com.bravvura.nestledtime.mediagallery.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.mediagallery.listener.MediaElementClick;
import com.bravvura.nestledtime.mediagallery.model.MediaModel;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Deepak Saini on 07-02-2018.
 */


public class AllAlbumGalleryAdapter extends RecyclerView.Adapter<AllAlbumGalleryAdapter.MediaViewHolder> {
    private ArrayList<MediaModel> mediaModels;
    private final int cellWidth;
    private MediaElementClick albumClickListener;

    public AllAlbumGalleryAdapter(ArrayList<MediaModel> mediaModels, int width) {
        this.mediaModels = mediaModels;
        this.cellWidth = width;
    }

    @Override
    public MediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.cell_album_gallery, null);
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MediaViewHolder holder, int position) {
        holder.layoutContent.getLayoutParams().height = cellWidth;
        holder.layoutContent.getLayoutParams().width = cellWidth;
        holder.populate(mediaModels.get(position));
    }

    @Override
    public int getItemCount() {
        return mediaModels.size();
    }

    public void setOnAlbumClick(MediaElementClick albumClickListener) {
        this.albumClickListener = albumClickListener;
    }

    public void setResult(ArrayList<MediaModel> albumModels) {
        this.mediaModels = albumModels;
    }

    class MediaViewHolder extends RecyclerView.ViewHolder {
        ImageView imageItem;
        View layoutContent;
        TextView textAlbumName, textMediaCount;
        private MediaModel mediaModel;

        MediaViewHolder(View itemView) {
            super(itemView);
            layoutContent = itemView.findViewById(R.id.layout_content);
            imageItem = itemView.findViewById(R.id.image_item);
            textAlbumName = itemView.findViewById(R.id.text_album_name);
            textMediaCount = itemView.findViewById(R.id.text_media_count);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(albumClickListener!=null)
                        albumClickListener.onClick(getAdapterPosition(), mediaModel);
                }
            });
        }

        public void populate(MediaModel mediaModel) {
            this.mediaModel = mediaModel;
            Glide.with(itemView.getContext()).load(new File(mediaModel.getPathFile())).into(imageItem);
            textAlbumName.setText(mediaModel.getTitle());
            textMediaCount.setText(mediaModel.mediaCount + "");
        }
    }
}
