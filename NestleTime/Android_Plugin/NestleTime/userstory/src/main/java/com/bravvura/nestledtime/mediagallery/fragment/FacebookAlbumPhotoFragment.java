package com.bravvura.nestledtime.mediagallery.fragment;

import android.os.Bundle;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.activity.BaseActivity;
import com.bravvura.nestledtime.mediagallery.adapter.AllPhotoGalleryAdapter;
import com.bravvura.nestledtime.mediagallery.listener.MediaElementClick;
import com.bravvura.nestledtime.mediagallery.model.MEDIA_CELL_TYPE;
import com.bravvura.nestledtime.mediagallery.model.MEDIA_SOURCE_TYPE;
import com.bravvura.nestledtime.mediagallery.model.MediaModel;
import com.bravvura.nestledtime.mediagallery.model.facebook.FacebookAlbumData;
import com.bravvura.nestledtime.mediagallery.model.facebook.FacebookItemData;
import com.bravvura.nestledtime.userstory.ui.fragment.BaseFragment;
import com.bravvura.nestledtime.utils.Constants;
import com.bravvura.nestledtime.utils.Utils;

import java.util.ArrayList;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 15-03-2018.
 */

public class FacebookAlbumPhotoFragment extends BaseFragment {
    private AllPhotoGalleryAdapter adapter;
    private ArrayList<MediaModel> mediaModels = new ArrayList<>();
    private FacebookAlbumData albumModel;
    ProgressBar progressBar;
    private MenuItem menuItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.frag_all_photo_gallery, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) return;
        albumModel = bundle.getParcelable(Constants.BUNDLE_KEY.MEDIA_MODEL);
        initComponent(view);
        makeMediaModel();
    }

    private void makeMediaModel() {
        if (albumModel.photos != null && !Utils.isEmpty(albumModel.photos.data))
            for (FacebookItemData facebookItemData : albumModel.photos.data) {
                MediaModel mediaModel = new MediaModel();
                mediaModel.sourceType = MEDIA_SOURCE_TYPE.TYPE_FACEBOOK;
                mediaModel.mediaCellType = MEDIA_CELL_TYPE.TYPE_IMAGE;
                mediaModel.setUrl(facebookItemData.source);
                mediaModel.setThumbnail(facebookItemData.source_thumb);
                mediaModels.add(mediaModel);
            }
        adapter.notifyDataSetChanged();
    }

    private void initComponent(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mediaModels.get(position).mediaCellType == MEDIA_CELL_TYPE.TYPE_HEADER)
                    return 3;
                else return 1;
            }
        });
        recyclerView.setLayoutManager(layoutManager);

        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        adapter = new AllPhotoGalleryAdapter(mediaModels, width / 3);
        adapter.setOnMediaClickListener(new MediaElementClick() {
            @Override
            public void onEditClick(int index, MediaModel mediaModel) {

            }

            @Override
            public void onClick(int index, MediaModel mediaModel) {
                mediaModel.setSelected(!mediaModel.isSelected());
                int selectedCount = getSelectedMediaCount();

                menuItem.setEnabled(selectedCount > 0);

                if (selectedCount > Constants.MAX_MEDIA_SELECTED_COUNT) {
                    mediaModel.setSelected(!mediaModel.isSelected());
                    Toast.makeText(getContext(), "Maximum selection count is " + Constants.MAX_MEDIA_SELECTED_COUNT, Toast.LENGTH_SHORT).show();
                } else {
                    adapter.notifyItemChanged(index);
                }
            }

            @Override
            public void onRemoveClick(int index, MediaModel mediaModel) {

            }
        });
        recyclerView.setAdapter(adapter);
    }

    private int getSelectedMediaCount() {
        int count = 0;
        for (int index = 0; index < mediaModels.size(); index++) {
            if (mediaModels.get(index).isSelected())
                count++;
        }
        return count;
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

    private ArrayList<MediaModel> getSelectedMediaModels() {
        ArrayList<MediaModel> selectedMedias = new ArrayList<>();
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

        int selectedCount = getSelectedMediaCount();
        menuItem.setEnabled(selectedCount > 0);

        menuItem.setTitle(s);

        super.onCreateOptionsMenu(menu, inflater);
    }
}
