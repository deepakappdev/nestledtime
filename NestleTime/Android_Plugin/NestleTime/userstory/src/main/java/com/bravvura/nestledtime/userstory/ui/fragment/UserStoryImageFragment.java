package com.bravvura.nestledtime.userstory.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
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
        return inflater.inflate(R.layout.user_story_pager_image, container, false);
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

    public static UserStoryImageFragment create(MediaModel mediaModel) {
        UserStoryImageFragment fragment = new UserStoryImageFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.BUNDLE_KEY.MEDIA_MODEL, mediaModel);
        fragment.setArguments(bundle);
        return fragment;
    }
}
