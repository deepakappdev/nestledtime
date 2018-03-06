package com.bravvura.nestledtime.userstory.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.activity.BaseActivity;
import com.bravvura.nestledtime.imageedittor.main.activity.EditPhotoActivity;
import com.bravvura.nestledtime.mediagallery.listener.MediaElementClick;
import com.bravvura.nestledtime.mediagallery.model.MEDIA_CELL_TYPE;
import com.bravvura.nestledtime.mediagallery.model.MEDIA_SOURCE_TYPE;
import com.bravvura.nestledtime.mediagallery.model.MediaModel;
import com.bravvura.nestledtime.mediagallery.ui.MediaGalleryActivity;
import com.bravvura.nestledtime.userstory.adapter.UserStoryMediaListAdapter;
import com.bravvura.nestledtime.userstory.model.UserStoryMediaModel;
import com.bravvura.nestledtime.utils.CloudinaryManager;
import com.bravvura.nestledtime.utils.Constants;
import com.bravvura.nestledtime.utils.MyFileSystem;
import com.bravvura.nestledtime.utils.StringUtils;
import com.cloudinary.Transformation;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Deepak Saini on 12-02-2018.
 */

public class UserStoryMediaListActivity extends BaseActivity implements View.OnClickListener {


    private RecyclerView recyclerView;
    UserStoryMediaModel userStoryMediaModel = new UserStoryMediaModel();
    private UserStoryMediaListAdapter adapter;
    private LinearLayoutManager layoutManager;
    private ProgressBar progressBar;
    private TextView textProgressBar;
    private MenuItem menuItem;
    private ProgressDialog dialog;
    private UploadCallback callBack = new UploadCallback() {
        @Override
        public void onStart(String requestId) {

        }

        @Override
        public void onProgress(String requestId, long bytes, long totalBytes) {
            MediaModel mediaModel = getMediaModelByRequestId(requestId);
            if (mediaModel != null) {
                mediaModel.setProgress((int) (bytes * 100 / totalBytes));
                publishProgress(mediaModel);
            }
        }

        @Override
        public void onSuccess(String requestId, Map resultData) {
            MediaModel mediaModel = getMediaModelByRequestId(requestId);
            if (mediaModel != null) {
                String serverUrl = "";
                String publilcId = "";
                if (resultData.containsKey("eager")) {
                    ArrayList eagerData = (ArrayList) resultData.get("eager");
                    if (eagerData.size() > 0) {
                        Map eagerMap = (Map) eagerData.get(0);
                        if (eagerMap.containsKey("url"))
                            serverUrl = eagerMap.get("url").toString();
                    }
                } else {
                    if (resultData.containsKey("url"))
                        serverUrl = resultData.get("url").toString();
                }
                publilcId = resultData.get("public_id").toString();
                if (!StringUtils.isNullOrEmpty(serverUrl)) {
                    mediaModel.setUrl(serverUrl);
                    if (!StringUtils.isNullOrEmpty(publilcId))
                        mediaModel.setPublicId(publilcId);
                    mediaModel.setIsUploaded(true);
                    updateProgress();
                    if (!startUploadingFiles()) {
                        startDeletingFiles();
                    }
                }
            }
        }

        @Override
        public void onError(String requestId, ErrorInfo error) {

        }

        @Override
        public void onReschedule(String requestId, ErrorInfo error) {

        }
    };
    private int editedIndex;
    private MediaElementClick onEditClick = new MediaElementClick() {
        @Override
        public void onClick(int index, MediaModel mediaModel) {
            File toFile = MyFileSystem.getTempImageFile();
            if (MyFileSystem.copyFile(mediaModel.getPathFile(), toFile.getAbsolutePath())) {
//                mediaModel.setPathFile(toFile.getAbsolutePath());
                editedIndex = index;
                Intent starter = new Intent(getApplicationContext(), EditPhotoActivity.class);
                starter.putExtra(EditPhotoActivity.INPUT_URL, toFile.getAbsolutePath());
                startActivityForResult(starter, Constants.REQUEST_CODE.REQUEST_EDIT_IMAGE);
            }
        }
    };

    private void updateProgress() {
    }

    private void publishProgress(MediaModel mediaModel) {
        int totalImage = userStoryMediaModel.mediaModels.size();
        int progress = 0;
        for (MediaModel model : userStoryMediaModel.mediaModels) {
            progress = (int) (progress + model.getProgress());
        }
//        Toast.makeText(this, "Total Progress : " + progress + " / " + (totalImage * 100), Toast.LENGTH_SHORT).show();
        if (progress == totalImage * 100) {
            textProgressBar.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        } else {
            textProgressBar.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }
        textProgressBar.setText(progress / (totalImage) + " % percentage");
        progressBar.setProgress(progress / (totalImage));
    }


    private MediaModel getMediaModelByRequestId(String requestId) {
        for (MediaModel model : userStoryMediaModel.mediaModels) {
            if (model.getRequestId() != null && model.getRequestId().equalsIgnoreCase(requestId))
                return model;
        }
        return null;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_story_media_list);
        initComponent();
        setupToolBar();
        setTitle("Edit Story");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.BUNDLE_KEY.ADD_PICTURE) && bundle.getBoolean(Constants.BUNDLE_KEY.ADD_PICTURE)) {
                findViewById(R.id.fab_add_button).performClick();
            }
            if (bundle.containsKey(Constants.BUNDLE_KEY.SELECTED_MEDIA)) {
                userStoryMediaModel = bundle.getParcelable(Constants.BUNDLE_KEY.SELECTED_MEDIA);
            }
        }
        initAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gallery_media_menu, menu);
        menuItem = menu.findItem(R.id.menu_done);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_done) {
            compressMediaFiles();
        }
        return super.onOptionsItemSelected(item);
    }

    private void compressMediaFiles() {
        if (dialog != null) dialog.dismiss();
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.show();
        AsyncFileCompression compressingFileAsync = new AsyncFileCompression();
        compressingFileAsync.execute((Void) null);
    }

    MediaModel getMediaToDelete() {
        for (MediaModel mediaModel : userStoryMediaModel.mediaModels) {
            if (mediaModel.isDeleted || (mediaModel.isEdited && mediaModel.sourceType == MEDIA_SOURCE_TYPE.TYPE_CLOUD)) {
                return mediaModel;
            }
        }
        return null;
    }


    MediaModel getMediaToUpload() {
        for (MediaModel mediaModel : userStoryMediaModel.mediaModels) {
            if (StringUtils.isNullOrEmpty(mediaModel.getRequestId()) && !mediaModel.isDeleted) {
                if (mediaModel.sourceType != MEDIA_SOURCE_TYPE.TYPE_CLOUD || mediaModel.isEdited) {
                    return mediaModel;
                }
            }
        }
        return null;
    }


    private boolean startUploadingFiles() {
        MediaModel mediaModel = getMediaToUpload();
        if (mediaModel != null) {
            doUpload(mediaModel);
            return true;
        }
        return false;
    }

    private void startDeletingFiles() {
        new AsyncTask<Void, Map, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                while (true) {
                    MediaModel mediaModel = getMediaToDelete();
                    if (mediaModel != null) {
                        Map map = CloudinaryManager.deleteFile(mediaModel.getPublicId());
                        userStoryMediaModel.mediaModels.remove(mediaModel);
                        this.publishProgress(map);
                    } else {
                        break;
                    }
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Map... values) {

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                checkForFinish();
            }
        }.execute((Void) null);
    }


    private void doUpload(MediaModel mediaModel) {
        String fileToUpload = null;
        if (!StringUtils.isNullOrEmpty(mediaModel.getCompressPath())) {
            fileToUpload = mediaModel.getCompressPath();
        } else if (!StringUtils.isNullOrEmpty(mediaModel.getPathFile())) {
            fileToUpload = mediaModel.getPathFile();
        } else if (!StringUtils.isNullOrEmpty(mediaModel.getUrl())) {
            fileToUpload = mediaModel.getUrl();
        }
        Transformation tr = new Transformation();
        tr.crop("fit").width(100);
        if (!StringUtils.isNullOrEmpty(fileToUpload)) {
            if (mediaModel.mediaCellType == MEDIA_CELL_TYPE.TYPE_IMAGE) {
                mediaModel.setIsUploaded(false);
                String requestId = CloudinaryManager.uploadImageFile(fileToUpload, callBack);
                mediaModel.setRequestId(requestId);
            } else if (mediaModel.mediaCellType == MEDIA_CELL_TYPE.TYPE_VIDEO) {
                mediaModel.setIsUploaded(false);
                String requestId = CloudinaryManager.uploadVideoFile(fileToUpload, callBack);
                mediaModel.setRequestId(requestId);
            }
        }
    }

    void checkForFinish() {
        boolean canFinish = true;
        for (MediaModel mediaModel : userStoryMediaModel.mediaModels) {
            if (!mediaModel.isUploaded()) {
                canFinish = false;
                break;
            }
        }
        if (canFinish)
            finishWithResult(userStoryMediaModel);
    }

    class AsyncFileCompression extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            for (MediaModel mediaModel : userStoryMediaModel.mediaModels) {
                if ((mediaModel.sourceType == MEDIA_SOURCE_TYPE.TYPE_LOCAL || mediaModel.isEdited)
                        && !StringUtils.isNullOrEmpty(mediaModel.getPathFile()) && StringUtils.isNullOrEmpty(mediaModel.getCompressPath())) {
                    if (mediaModel.mediaCellType == MEDIA_CELL_TYPE.TYPE_IMAGE) {
                        publishProgress("Compressing " + mediaModel.getTitle());
                        String compressPath = MyFileSystem.compressImage(mediaModel.getPathFile());
                        mediaModel.setCompressPath(compressPath);
                    } else if (mediaModel.mediaCellType == MEDIA_CELL_TYPE.TYPE_VIDEO) {
                        publishProgress("Compressing " + mediaModel.getTitle());
                        String compressPath = MyFileSystem.compressVideo(mediaModel.getPathFile());
                        mediaModel.setCompressPath(compressPath);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            showProgressBar(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hideProgressBar();
            if (!startUploadingFiles()) {
                startDeletingFiles();
            }
        }
    }

    void showProgressBar(String message) {
        progressBar.setVisibility(View.VISIBLE);
        textProgressBar.setVisibility(View.VISIBLE);
        textProgressBar.setText(message);
        menuItem.setEnabled(false);
    }

    void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        textProgressBar.setVisibility(View.GONE);
        menuItem.setEnabled(true);
        dialog.dismiss();
    }

    private void initComponent() {
        progressBar = findViewById(R.id.progress_bar);
        textProgressBar = findViewById(R.id.text_progress_bar);
        recyclerView = findViewById(R.id.recycler_view);
        findViewById(R.id.fab_add_button).setOnClickListener(this);
        recyclerView.setLayoutManager(layoutManager = new LinearLayoutManager(getApplicationContext()));
    }

    private void initAdapter() {
        adapter = new UserStoryMediaListAdapter(this, onEditClick);
        adapter.setResults(userStoryMediaModel);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_add_button) {
            startActivityForResult(new Intent(getApplicationContext(), MediaGalleryActivity.class), Constants.REQUEST_CODE.REQUEST_GALLERY_MEDIA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.REQUEST_CODE.REQUEST_EDIT_IMAGE:
                if (resultCode == RESULT_OK && data != null && data.hasExtra(EditPhotoActivity.OUTPUT_URL)) {
                    String editedPath = data.getStringExtra(EditPhotoActivity.OUTPUT_URL);
                    MediaModel mediaModel = adapter.getItem(editedIndex);
                    mediaModel.setPathFile(editedPath);
                    mediaModel.isEdited = true;
                    adapter.notifyItemChanged(editedIndex);
                }
                break;
            case Constants.REQUEST_CODE.REQUEST_GALLERY_MEDIA:
                if (resultCode == RESULT_OK) {
                    ArrayList<MediaModel> selectedModels = data.getParcelableArrayListExtra(Constants.BUNDLE_KEY.SELECTED_MEDIA);
                    if (selectedModels != null) {
                        userStoryMediaModel.mediaModels.addAll(selectedModels);
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }
}
