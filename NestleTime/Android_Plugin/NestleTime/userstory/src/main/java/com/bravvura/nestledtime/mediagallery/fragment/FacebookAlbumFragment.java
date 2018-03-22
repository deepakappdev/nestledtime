package com.bravvura.nestledtime.mediagallery.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import com.bravvura.nestledtime.activity.BaseActivity;
import com.bravvura.nestledtime.mediagallery.adapter.FacebookAlbumGalleryAdapter;
import com.bravvura.nestledtime.mediagallery.listener.MediaElementClick;
import com.bravvura.nestledtime.mediagallery.model.MEDIA_CELL_TYPE;
import com.bravvura.nestledtime.mediagallery.model.MEDIA_SOURCE_TYPE;
import com.bravvura.nestledtime.mediagallery.model.MediaModel;
import com.bravvura.nestledtime.mediagallery.model.facebook.FacebookAlbumData;
import com.bravvura.nestledtime.mediagallery.model.facebook.FacebookResponse;
import com.bravvura.nestledtime.mediagallery.ui.FacebookMediaAlbumActivity;
import com.bravvura.nestledtime.network.volley.NetworkError;
import com.bravvura.nestledtime.network.volley.RequestCallback;
import com.bravvura.nestledtime.network.volley.RequestController;
import com.bravvura.nestledtime.userstory.ui.fragment.BaseFragment;
import com.bravvura.nestledtime.utils.Constants;
import com.bravvura.nestledtime.utils.Utils;

import java.util.ArrayList;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 15-03-2018.
 */

public class FacebookAlbumFragment extends BaseFragment implements RequestCallback {
    private ViewGroup layoutFacebookLogin;
    private FacebookResponse facebookResponse;
    private ArrayList<MediaModel> mediaModels = new ArrayList<>();
    private FacebookAlbumGalleryAdapter adapter;
    private MediaElementClick albumClickListener = new MediaElementClick() {
        @Override
        public void onClick(int index, MediaModel mediaModel) {
            Intent intent = new Intent(getContext(), FacebookMediaAlbumActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.BUNDLE_KEY.MEDIA_MODEL, facebookResponse.data.data.get(index));
            intent.putExtras(bundle);
            startActivityForResult(intent, Constants.REQUEST_CODE.REQUEST_GALLERY_MEDIA);
        }
    };
    private MenuItem menuItem;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_done) {
            if (getActivity() != null && getActivity() instanceof BaseActivity) {
                ((BaseActivity) getActivity()).finishWithResult(getSelectedMediaModels());
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private int getSelectedMediaCount() {
        return getSelectedMediaModels().size();
    }

    private ArrayList<MediaModel> getSelectedMediaModels() {
        ArrayList<MediaModel> selectedMedias = new ArrayList<MediaModel>();
        for (int index = 0; index < mediaModels.size(); index++) {
            if (mediaModels.get(index).isSelected())
                selectedMedias.add(mediaModels.get(index));
        }
        return selectedMedias;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_done, menu);
        menuItem = menu.findItem(R.id.menu_done);

        SpannableString s = new SpannableString("Done");
        s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.white)), 0, s.length(), 0);
        menuItem.setTitle(s);

        int selectedCount = getSelectedMediaCount();
        menuItem.setEnabled(selectedCount > 0);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.frag_facebook_photo_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        fetchFaceAlbumPhoto();
    }

    private void fetchFaceAlbumPhoto() {
        RequestController.getFacebookAlbum(this);
    }

    private void initView(View view) {
        layoutFacebookLogin = view.findViewById(R.id.layout_facebook_login);
        layoutFacebookLogin.setVisibility(View.GONE);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        adapter = new FacebookAlbumGalleryAdapter(mediaModels, width / 2);
        adapter.setOnAlbumClick(albumClickListener);
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
                if (albumData.photos != null && !Utils.isEmpty(albumData.photos.data)) {
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
