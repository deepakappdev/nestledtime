package com.bravvura.nestledtime.mediagallery.fragment;

import android.Manifest;
import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.eventbusmodel.MessageAlbumFound;
import com.bravvura.nestledtime.eventbusmodel.MessageLocalIndexEvent;
import com.bravvura.nestledtime.eventbusmodel.MessagePhotoFound;
import com.bravvura.nestledtime.mediagallery.adapter.LocalMediaGalleryAdapter;
import com.bravvura.nestledtime.mediagallery.filesystem.CursorManager;
import com.bravvura.nestledtime.mediagallery.model.MEDIA_CELL_TYPE;
import com.bravvura.nestledtime.mediagallery.model.MediaModel;
import com.bravvura.nestledtime.userstory.ui.fragment.BaseFragment;
import com.bravvura.nestledtime.utils.Constants;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 15-03-2018.
 */

public class LocalGalleryFragment extends BaseFragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    private LocalMediaGalleryAdapter adapter;
    public ArrayList<MediaModel> photoModels = new ArrayList<>();
    public ArrayList<MediaModel> albumModels = new ArrayList<>();
    private Cursor cursor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        return inflater.inflate(R.layout.fragment_media_gallery_pager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
    }

    private void initComponent(View view) {
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    for (int i = 0; i < photoModels.size(); i++) {
                        photoModels.get(i).setSelected(false);
                    }
                    MessageLocalIndexEvent localIndexEvent = new MessageLocalIndexEvent();
                    localIndexEvent.selectedIndex = position;
                    EventBus.getDefault().post(localIndexEvent);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            fetchAllPictureAlbums();
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.READ_STORAGE);
        }

    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == Constants.REQUEST_CODE.READ_STORAGE) {
            fetchAllPictureAlbums();
        } else {
            if(getActivity()!=null) {
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
            }
        }
    }
    private void fetchAllPictureAlbums() {
        if(cursor!=null)return;
        cursor = CursorManager.getCursorForMedia(getContext());
        adapter = new LocalMediaGalleryAdapter(getChildFragmentManager(), this);
        viewPager.setAdapter(adapter);
        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        TextView textOne = view1.findViewById(R.id.tab);
        textOne.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tab_photo_selector, 0, 0, 0);
        textOne.setText("Photos");
        View view2 = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        TextView textTwo = view2.findViewById(R.id.tab);
        textTwo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tab_album_selector, 0, 0, 0);
        textTwo.setText("Albums");
        tabLayout.getTabAt(0).setCustomView(view1);
        tabLayout.getTabAt(1).setCustomView(view2);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                photoModels.clear();
                addCameraElement(photoModels);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                makeMediaModel();
                return null;
            }
        }.execute((Void) null);

    }

    private void addCameraElement(ArrayList<MediaModel> mediaModels) {
        if (mediaModels.size() == 0 || mediaModels.get(0).mediaCellType != MEDIA_CELL_TYPE.TYPE_CAMERA) {
            MediaModel mediaModel = new MediaModel();
            mediaModel.mediaCellType = MEDIA_CELL_TYPE.TYPE_CAMERA;
            mediaModels.add(0, mediaModel);
        }
    }

    private void makeMediaModel() {
        if (cursor != null && !cursor.isClosed()) {
            int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
            int column_index_id = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID);
            int column_index_type = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE);

            int lastIndex = -1, mediaIndex = 0, albumIndex = 0;
            while (cursor.moveToNext()) {
                String pathFile = cursor.getString(column_index_data);
                int id = cursor.getInt(column_index_id);
                int mediaType = cursor.getInt(column_index_type);
                File file = new File(pathFile);
                MediaModel mediaModel = new MediaModel("", pathFile, file.getParent(), file.lastModified());
                if (file.exists()) {
                    if (/*file.getParentFile() != null && */!albumFolderExists(file)) {
                        MediaModel albumModel = new MediaModel(file.getParentFile().getName(), pathFile, file.getParent(), file.lastModified());
                        albumModel.mediaCount = 1;
                        albumModel.mediaCellType = MEDIA_CELL_TYPE.TYPE_ALBUM;
                        albumModels.add(albumModel);
                        albumIndex++;
                    }

                    if (photoModels.size()==1 || (photoModels.get(photoModels.size() - 1).getDate()!=null && !photoModels.get(photoModels.size() - 1).getDate().equalsIgnoreCase(mediaModel.getDate()))) {
                        photoModels.add(getDateHeader(file.lastModified()));
                        mediaIndex++;
                    }
                    mediaModel.setId(id);
                    mediaModel.setType(mediaType);
                    photoModels.add(mediaModel);


                    if (mediaIndex > lastIndex + 100) {
                        MessagePhotoFound message = new MessagePhotoFound();
                        message.insertionIndex = lastIndex+1;
                        message.totalCount = photoModels.size();
                        EventBus.getDefault().post(message);
                        lastIndex = mediaIndex;
                    }
                    if (albumIndex % 4 == 0)
                        EventBus.getDefault().post(new MessageAlbumFound());
                }
            }
            EventBus.getDefault().post(new MessageAlbumFound());

            MessagePhotoFound message = new MessagePhotoFound();
            message.insertionIndex = lastIndex+1;
            message.totalCount = photoModels.size();
            EventBus.getDefault().post(message);

            cursor.close();
            cursor = null;
        }
    }

    private boolean albumFolderExists(File file) {
        String parentFolder = file.getParentFile().getAbsolutePath();
        for (MediaModel mediaModel : albumModels) {
            if (mediaModel.getPathFolder().equalsIgnoreCase(parentFolder)) {
                mediaModel.mediaCount++;
                return true;
            }
        }
        return false;
    }




}
