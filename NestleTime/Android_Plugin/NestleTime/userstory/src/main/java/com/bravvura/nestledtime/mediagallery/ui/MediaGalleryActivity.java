package com.bravvura.nestledtime.mediagallery.ui;

import android.Manifest;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.activity.BaseActivity;
import com.bravvura.nestledtime.eventbusmodel.MessageAlbumFound;
import com.bravvura.nestledtime.eventbusmodel.MessageMediaFound;
import com.bravvura.nestledtime.mediagallery.adapter.MediaGalleryAdapter;
import com.bravvura.nestledtime.mediagallery.filesystem.CursorManager;
import com.bravvura.nestledtime.mediagallery.model.MEDIA_CELL_TYPE;
import com.bravvura.nestledtime.mediagallery.model.MediaModel;
import com.bravvura.nestledtime.utils.Constants;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by Deepak Saini on 06-02-2018.
 */

public class MediaGalleryActivity extends BaseActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    private MediaGalleryAdapter adapter;
    public ArrayList<MediaModel> mediaModels = new ArrayList<>();
    public ArrayList<MediaModel> albumModels = new ArrayList<>();

    private Cursor cursor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_gallery);
        initComponent();
        setupToolBar();
        setTitle("Photos & Videos");
    }

    private void initComponent() {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    for (int i = 0; i < mediaModels.size(); i++) {
                        mediaModels.get(i).setSelected(false);
                    }
                    EventBus.getDefault().post(new MessageMediaFound());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            fetchAllPictureAlbums();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_CODE.READ_STORAGE);
        }

    }

    private void fetchAllPictureAlbums() {
        cursor = CursorManager.getCursorForMedia(getApplicationContext());
        adapter = new MediaGalleryAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        View view1 = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView textOne = view1.findViewById(R.id.tab);
        textOne.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tab_photo_selector, 0, 0, 0);
        textOne.setText("Photos");
        View view2 = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView textTwo = view2.findViewById(R.id.tab);
        textTwo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tab_album_selector, 0, 0, 0);
        textTwo.setText("Albums");
        tabLayout.getTabAt(0).setCustomView(view1);
        tabLayout.getTabAt(1).setCustomView(view2);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mediaModels.clear();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                makeMediaModel();
                return null;
            }
        }.execute((Void) null);

    }

    private void makeMediaModel() {
        if (cursor != null) {
            int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
            int column_index_id = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID);
            int column_index_type = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE);
            int column_index_size = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE);

            int mediaIndex = 0, albumIndex = 0;
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

                    if (mediaModels.isEmpty() || !mediaModels.get(mediaModels.size() - 1).getDate().equalsIgnoreCase(mediaModel.getDate())) {
                        mediaModels.add(getDateHeader(file.lastModified()));
                    }
                    mediaModel.setId(id);
                    mediaModel.setType(mediaType);
                    mediaModels.add(mediaModel);
                    mediaIndex++;

                    if (mediaIndex % 100 == 0)
                        EventBus.getDefault().post(new MessageMediaFound());
                    if (albumIndex % 4 == 0)
                        EventBus.getDefault().post(new MessageAlbumFound());
                }
            }
            EventBus.getDefault().post(new MessageAlbumFound());
            EventBus.getDefault().post(new MessageMediaFound());
            cursor.close();
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

    private MediaModel getDateHeader(long time) {
        MediaModel mediaModel = new MediaModel();
        mediaModel.setLastModified(time);
        mediaModel.setType(-1);
        return mediaModel;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == Constants.REQUEST_CODE.READ_STORAGE) {
            fetchAllPictureAlbums();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}