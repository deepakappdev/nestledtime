package com.nestletime.mediagallery.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nestletime.R;
import com.nestletime.model.eventbusmodel.MessageAlbumFound;
import com.nestletime.mediagallery.adapter.AllAlbumGalleryAdapter;
import com.nestletime.mediagallery.listener.MediaElementClick;
import com.nestletime.mediagallery.model.MediaModel;
import com.nestletime.mediagallery.ui.MediaAlbumActivity;
import com.nestletime.mediagallery.ui.MediaGalleryActivity;
import com.nestletime.utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Created by Deepak Saini on 07-02-2018.
 */

public class AllAlbumFragment extends Fragment {
    private AllAlbumGalleryAdapter adapter;
    ArrayList<MediaModel> albumModels = new ArrayList<>();
    private MediaElementClick albumClickListener = new MediaElementClick() {
        @Override
        public void onClick(int index, MediaModel mediaModel) {
            Intent intent = new Intent(getContext(), MediaAlbumActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.BUNDLE_KEY.MEDIA_MODEL, mediaModel);
            intent.putExtras(bundle);
            startActivityForResult(intent, Constants.REQUEST_CODE.REQUEST_GALLERY_MEDIA);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.REQUEST_CODE.REQUEST_GALLERY_MEDIA:
                if (resultCode == Activity.RESULT_OK) {
                    if (getActivity() != null) {
                        getActivity().setResult(resultCode, data);
                        getActivity().finish();
                    }
                }
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_all_photo_gallery, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
        fetchAllPicture();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageAlbumFound event) {
        adapter.notifyDataSetChanged();
    }


    private void fetchAllPicture() {
        albumModels = ((MediaGalleryActivity) getActivity()).albumModels;
        adapter.setResult(albumModels);
        adapter.notifyDataSetChanged();
    }


    private void initComponent(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        adapter = new AllAlbumGalleryAdapter(albumModels, width / 2);
        adapter.setOnAlbumClick(albumClickListener);
        recyclerView.setAdapter(adapter);
    }
}
