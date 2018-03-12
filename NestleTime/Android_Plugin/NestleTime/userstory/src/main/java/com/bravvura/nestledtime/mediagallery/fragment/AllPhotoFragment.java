package com.bravvura.nestledtime.mediagallery.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.activity.BaseActivity;
import com.bravvura.nestledtime.eventbusmodel.MessagePhotoFound;
import com.bravvura.nestledtime.mediagallery.adapter.AllPhotoGalleryAdapter;
import com.bravvura.nestledtime.mediagallery.listener.MediaElementClick;
import com.bravvura.nestledtime.mediagallery.model.MEDIA_CELL_TYPE;
import com.bravvura.nestledtime.mediagallery.model.MediaModel;
import com.bravvura.nestledtime.mediagallery.ui.MediaGalleryActivity;
import com.bravvura.nestledtime.utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Created by Deepak Saini on 07-02-2018.
 */

public class AllPhotoFragment extends Fragment {
    private AllPhotoGalleryAdapter adapter;
    ArrayList<MediaModel> mediaModels = new ArrayList<>();
    private MenuItem menuItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.frag_all_photo_gallery, null);
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
    public void onMessageEvent(MessagePhotoFound event) {
        adapter.notifyItemRangeChanged(event.insertionIndex, event.totalCount);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
        fetchAllPicture();
    }

    private void fetchAllPicture() {
        mediaModels = ((MediaGalleryActivity) getActivity()).photoModels;
        adapter.setResult(mediaModels);
        adapter.notifyDataSetChanged();
    }


    private void initComponent(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
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
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_done) {
                if (getActivity() != null && getActivity() instanceof BaseActivity) {
                    ((BaseActivity)getActivity()).finishWithResult(getSelectedMediaModels());
                }
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.gallery_media_menu, menu);
        menuItem = menu.findItem(R.id.menu_done);

        SpannableString s = new SpannableString("Done");
        s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.white)), 0, s.length(), 0);
        menuItem.setTitle(s);

        int selectedCount = getSelectedMediaCount();
        menuItem.setEnabled(selectedCount > 0);

        super.onCreateOptionsMenu(menu, inflater);
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


}
