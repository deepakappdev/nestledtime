package com.bravvura.nestledtime.mediagallery.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.eventbusmodel.MessageAlbumFound;
import com.bravvura.nestledtime.mediagallery.adapter.AllAlbumGalleryAdapter;
import com.bravvura.nestledtime.mediagallery.listener.MediaElementClick;
import com.bravvura.nestledtime.mediagallery.model.MediaModel;
import com.bravvura.nestledtime.mediagallery.ui.LocalMediaAlbumActivity;
import com.bravvura.nestledtime.mediagallery.ui.MediaGalleryActivity;
import com.bravvura.nestledtime.userstory.ui.fragment.BaseFragment;
import com.bravvura.nestledtime.utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Created by Deepak Saini on 07-02-2018.
 */

public class LocalAlbumFragment extends BaseFragment {
    private AllAlbumGalleryAdapter adapter;
    ArrayList<MediaModel> albumModels = new ArrayList<>();
    private MediaElementClick albumClickListener = new MediaElementClick() {
        @Override
        public void onClick(int index, MediaModel mediaModel) {
            Intent intent = new Intent(getContext(), LocalMediaAlbumActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.BUNDLE_KEY.MEDIA_MODEL, mediaModel);
            intent.putExtras(bundle);
            startActivityForResult(intent, Constants.REQUEST_CODE.REQUEST_GALLERY_MEDIA);
        }
    };
    public LocalGalleryFragment parentFragment;

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
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.frag_all_photo_gallery, null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_done, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_done);
        SpannableString s = new SpannableString("Done");
        s.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, s.length(), 0);
        menuItem.setTitle("Done");
//        menuItem.setEnabled(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
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


    private void initComponent(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        if (parentFragment == null)
            parentFragment = ((MediaGalleryActivity) getActivity()).adapter.localGalleryFragment;

        albumModels = parentFragment.albumModels;
        adapter = new AllAlbumGalleryAdapter(albumModels, width / 2);
        adapter.setOnAlbumClick(albumClickListener);
        recyclerView.setAdapter(adapter);
    }
}
