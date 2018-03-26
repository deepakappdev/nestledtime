package com.bravvura.nestledtime.userstory.ui.fragment;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.mediagallery.model.MediaModel;
import com.bravvura.nestledtime.utils.Constants;
import com.bravvura.nestledtime.utils.StringUtils;
import com.bumptech.glide.Glide;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 07-03-2018.
 */

public class UserStoryImageFragment extends BaseFragment {
    private ImageView imageView;
    private TextView textDescription;
    private MediaModel mediaModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.user_story_pager_image, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_download, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_download) {
            if (mediaModel != null)
                downloadFile();
        }
        return super.onOptionsItemSelected(item);
    }

    void downloadFile() {
        Uri downloadUri = Uri.parse(mediaModel.getUrl());
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle("Nestled Time");
        request.setDescription("image");
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "UserStoryImageFile.jpg");

        DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        long refId = manager.enqueue(request);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.image_view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaModel != null && !StringUtils.isNullOrEmpty(mediaModel.getTitle()))
                    textDescription.setVisibility(textDescription.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

            }
        });
        textDescription = view.findViewById(R.id.text_description);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mediaModel = bundle.getParcelable(Constants.BUNDLE_KEY.MEDIA_MODEL);
            if (!StringUtils.isNullOrEmpty(mediaModel.getUrl()))
                Glide.with(getContext()).load(mediaModel.getUrl()).into(imageView);
            else if (!StringUtils.isNullOrEmpty(mediaModel.getPathFile())) {
                Glide.with(getContext()).load(mediaModel.getPathFile()).into(imageView);
            }
            if (!StringUtils.isNullOrEmpty(mediaModel.getTitle())) {
                textDescription.setVisibility(View.VISIBLE);
                textDescription.setText(mediaModel.getTitle());
            } else {
                textDescription.setVisibility(View.GONE);
            }
        }
    }

    public static UserStoryImageFragment create(MediaModel mediaModel, int index) {
        UserStoryImageFragment fragment = new UserStoryImageFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.BUNDLE_KEY.MEDIA_MODEL, mediaModel);
        bundle.putInt(Constants.BUNDLE_KEY.INDEX, index);
        fragment.setArguments(bundle);
        return fragment;
    }
}
