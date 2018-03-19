package com.bravvura.nestledtime.mediagallery.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.mediagallery.adapter.FacebookAlbumGalleryAdapter;
import com.bravvura.nestledtime.mediagallery.model.MEDIA_CELL_TYPE;
import com.bravvura.nestledtime.mediagallery.model.MEDIA_SOURCE_TYPE;
import com.bravvura.nestledtime.mediagallery.model.MediaModel;
import com.bravvura.nestledtime.mediagallery.model.facebook.FacebookAlbumData;
import com.bravvura.nestledtime.mediagallery.model.facebook.FacebookResponse;
import com.bravvura.nestledtime.network.volley.NetworkError;
import com.bravvura.nestledtime.network.volley.RequestCallback;
import com.bravvura.nestledtime.network.volley.RequestController;
import com.bravvura.nestledtime.userstory.ui.fragment.BaseFragment;
import com.bravvura.nestledtime.utils.Utils;

import java.util.ArrayList;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 15-03-2018.
 */

public class FacebookPhotoFragment extends BaseFragment implements RequestCallback {
    public FacebookGalleryFragment parentFragment;
    private ViewGroup layoutFacebookLogin;
    private FacebookResponse facebookResponse;
    private ArrayList<MediaModel> mediaModels = new ArrayList<>();
    private FacebookAlbumGalleryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_facebook_photo_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        fetchFaceBookPhoto();
    }

    private void fetchFaceBookPhoto() {
        RequestController.getFacebookPhotos(this);
    }

    private void initView(View view) {
        layoutFacebookLogin = view.findViewById(R.id.layout_facebook_login);
        layoutFacebookLogin.setVisibility(View.GONE);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        adapter = new FacebookAlbumGalleryAdapter(mediaModels, width/2);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void error(NetworkError volleyError) {

    }

    @Override
    public void success(Object obj) {
        if (getActivity() != null && getContext() != null && obj instanceof FacebookResponse) {
            facebookResponse = (FacebookResponse) obj;
            makeMediaModel();
        }
    }

    private void makeMediaModel() {
        if (facebookResponse != null && facebookResponse.data != null && !Utils.isEmpty(facebookResponse.data.data)) {
            for (FacebookAlbumData albumData : facebookResponse.data.data) {
                if(albumData.photos!=null && !Utils.isEmpty(albumData.photos.data)) {
                    MediaModel mediaModel = new MediaModel();
                    mediaModel.sourceType = MEDIA_SOURCE_TYPE.TYPE_FACEBOOK;
                    mediaModel.mediaCellType = MEDIA_CELL_TYPE.TYPE_ALBUM;
                    mediaModel.setTitle(albumData.name);
                    mediaModel.mediaCount = albumData.photo_count;
                    mediaModel.setUrl(albumData.photos.data.get(0).source);
                    mediaModel.setThumbnail(albumData.photos.data.get(0).source_thumb);
                    mediaModels.add(mediaModel);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }
}
