package com.bravvura.nestledtime;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bravvura.nestledtime.activity.BaseActivity;
import com.bravvura.nestledtime.eventbusmodel.AudioRecordEventModel;
import com.bravvura.nestledtime.eventbusmodel.MediaModelUploadEvent;
import com.bravvura.nestledtime.firebase.OnValueEventListener;
import com.bravvura.nestledtime.firebase.manager.FirebaseUtils;
import com.bravvura.nestledtime.firebase.manager.MyFirebaseManager;
import com.bravvura.nestledtime.firebase.model.MemoryItem;
import com.bravvura.nestledtime.firebase.model.MemoryMediaItem;
import com.bravvura.nestledtime.firebase.model.MemoryPartItem;
import com.bravvura.nestledtime.mediagallery.model.MEDIA_CELL_TYPE;
import com.bravvura.nestledtime.mediagallery.model.MEDIA_SOURCE_TYPE;
import com.bravvura.nestledtime.mediagallery.model.MediaModel;
import com.bravvura.nestledtime.userstory.adapter.UserStoryActionListAdapter;
import com.bravvura.nestledtime.userstory.adapter.UserStoryElementListAdapter;
import com.bravvura.nestledtime.userstory.customview.UserStoryMediaView;
import com.bravvura.nestledtime.userstory.listener.OnMediaClickListener;
import com.bravvura.nestledtime.userstory.model.UserStoryAddressModel;
import com.bravvura.nestledtime.userstory.model.UserStoryAudioModel;
import com.bravvura.nestledtime.userstory.model.UserStoryDateModel;
import com.bravvura.nestledtime.userstory.model.UserStoryElement;
import com.bravvura.nestledtime.userstory.model.UserStoryElementType;
import com.bravvura.nestledtime.userstory.model.UserStoryMediaModel;
import com.bravvura.nestledtime.userstory.ui.activity.GoogleMapActivity;
import com.bravvura.nestledtime.userstory.ui.activity.UserStoryMediaListActivity;
import com.bravvura.nestledtime.userstory.ui.activity.UserStoryMediaPagerActivity;
import com.bravvura.nestledtime.utils.CloudinaryManager;
import com.bravvura.nestledtime.utils.Constants;
import com.bravvura.nestledtime.utils.FRAGMENTS;
import com.bravvura.nestledtime.utils.MyFileSystem;
import com.bravvura.nestledtime.utils.StringUtils;
import com.bravvura.nestledtime.worlds.ui.activity.MyWorldsActivity;
import com.cloudinary.Transformation;
import com.cloudinary.android.MediaManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Map;

public class MemoryDetailActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private UserStoryElementListAdapter adapter;
    private LinearLayoutManager layoutManager;
    private int editMediaIndex, editLocationIndex;
    ArrayList<UserStoryElement> userStoryElements = new ArrayList<>();
    private OnMediaClickListener mediaClickListener = new OnMediaClickListener() {
        @Override
        public void onClick(RecyclerView.ViewHolder viewHolder, UserStoryElement userStoryElement, int index) {
            switch (userStoryElement.elementType) {
                case ELEMENT_TYPE_MEDIA: {
                    if (viewHolder instanceof UserStoryElementListAdapter.MediaViewHolder) {
                        UserStoryMediaView videoView = ((UserStoryElementListAdapter.MediaViewHolder) viewHolder).getVideoView();
                        if (videoView != null) {
                            if (videoView.isPlaying()) {
                                videoView.pause();
                            }
                        }
                    }
                    Intent intent = new Intent(getApplicationContext(), UserStoryMediaPagerActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Constants.BUNDLE_KEY.USER_STORY_MEDIA_MODEL, userStoryElement.mediaModel);
                    bundle.putInt(Constants.BUNDLE_KEY.INDEX, index);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
                case ELEMENT_TYPE_LOCATION: {
                    editLocationIndex = index;
                    Intent intent = new Intent(getApplicationContext(), GoogleMapActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Constants.BUNDLE_KEY.SELECTED_LOCATION, userStoryElement.addressModel);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, Constants.REQUEST_CODE.REQUEST_EDIT_LOCATION);
                }
                break;
            }
        }

        @Override
        public void onEditClick(UserStoryElement userStoryElement, int index) {
            switch (userStoryElement.elementType) {
                case ELEMENT_TYPE_MEDIA:
                    editMediaIndex = index;
                    Intent intent = new Intent(getApplicationContext(), UserStoryMediaListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Constants.BUNDLE_KEY.SELECTED_MEDIA, userStoryElement.mediaModel);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, Constants.REQUEST_CODE.REQUEST_EDIT_GALLERY_MEDIA);
                    break;
            }

        }

        @Override
        public void onRemoveClick(UserStoryElement userStoryElement, final int index) {
            showRemoveDialog(new DialogCallBack() {
                @Override
                public void onOKClick() {
                    adapter.removeIndex(index);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelClick() {

                }
            });

        }
    };
    private View fab_message, fab_audio, fab_media, fab_location;
    private BottomSheetBehavior<RecyclerView> actionListBottomSheetPager;
    MemoryItem memoryItem = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_story);
        initArguments();
        initComponent();
        setupToolBar();
        setTitle("New Memory");
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    private void initArguments() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey(Constants.BUNDLE_KEY.WORLD_ID) && bundle.containsKey(Constants.BUNDLE_KEY.MEMORY_ID)) {
                memoryItem = new MemoryItem();
                memoryItem.memoryId = bundle.getString(Constants.BUNDLE_KEY.MEMORY_ID);
                memoryItem.worldId = bundle.getString(Constants.BUNDLE_KEY.WORLD_ID);
                fetchMemoryDetail(memoryItem);
            } else if (bundle.containsKey(Constants.BUNDLE_KEY.MEMORY_ITEM)) {
                memoryItem = bundle.getParcelable(Constants.BUNDLE_KEY.MEMORY_ITEM);
            }
        }

        userStoryElements.clear();
        initActionList();
        if (memoryItem == null) {
            memoryItem = new MemoryItem();
            userStoryElements.add(new UserStoryElement(memoryItem.title, UserStoryElementType.ELEMENT_TYPE_TITLE));
            UserStoryElement dateElement = new UserStoryElement(new UserStoryDateModel(memoryItem.getDoeDate()));
            userStoryElements.add(dateElement);
            actionListBottomSheetPager.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            actionListBottomSheetPager.setState(BottomSheetBehavior.STATE_HIDDEN);
            userStoryElements.add(new UserStoryElement(memoryItem.title, UserStoryElementType.ELEMENT_TYPE_TITLE));
            UserStoryElement dateElement = new UserStoryElement(new UserStoryDateModel(memoryItem.getDoeDate()));
            userStoryElements.add(dateElement);
            makeUserStoryElement();
        }
    }

    private void fetchMemoryDetail(final MemoryItem memoryItem) {
        MyFirebaseManager.getMemoryItem(getApplicationContext(), memoryItem.worldId, memoryItem.memoryId, new OnValueEventListener<MemoryItem>() {
            @Override
            public void onValueRecived(MemoryItem value) {
                MemoryDetailActivity.this.memoryItem = value;
                makeUserStoryElement();
            }

            @Override
            public void onCancelled(String errorMessage) {

            }
        });
//        MyFirebaseManager.getMemoryItem()
    }

    private void makeUserStoryElement() {
        if (memoryItem.parts != null) {
            for (MemoryPartItem memoryPartItem : memoryItem.parts) {
                UserStoryMediaModel mediaModel = new UserStoryMediaModel();
                mediaModel.title = memoryPartItem.partDetail.title;
                if (memoryPartItem.partType.equalsIgnoreCase("collection")) {
                    mediaModel.mediaModels = new ArrayList<>();
                    if (memoryPartItem.images != null)
                        for (MemoryMediaItem mediaItem : memoryPartItem.images) {
                            mediaModel.mediaModels.add(FirebaseUtils.getMediaModel(mediaItem));
                        }
                    mediaModel.mediaCount = mediaModel.mediaModels.size();
                    userStoryElements.add(new UserStoryElement(mediaModel));
                } else if (memoryPartItem.partType.equalsIgnoreCase("text")) {
                    userStoryElements.add(new UserStoryElement(memoryPartItem.partDetail.body, UserStoryElementType.ELEMENT_TYPE_TEXT));
                } else if (memoryPartItem.partType.equalsIgnoreCase("voice")) {
                    UserStoryAudioModel audioModel = new UserStoryAudioModel();
                    audioModel.audioUrl = memoryPartItem.partDetail.path;
                    audioModel.totalSecond = (int) memoryPartItem.partDetail.totalSeconds;
                    userStoryElements.add(new UserStoryElement(audioModel));
                    userStoryElements.add(new UserStoryElement(memoryPartItem.partDetail.body, UserStoryElementType.ELEMENT_TYPE_TEXT));
                } else if (memoryPartItem.partType.equalsIgnoreCase("location")) {
                    userStoryElements.add(new UserStoryElement(new UserStoryAddressModel(memoryPartItem.partDetail.name, memoryPartItem.partDetail.latitude, memoryPartItem.partDetail.longitude)));
                }
            }
            if (adapter != null)
                adapter.notifyDataSetChanged();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AudioRecordEventModel event) {
        UserStoryAudioModel model = new UserStoryAudioModel();
        model.audioUrl = event.audioFileUrl;
        model.publicId = event.publicId;
        UserStoryElement userStoryMedia = new UserStoryElement(model);
        int index = userStoryElements.size();
        userStoryElements.add(userStoryMedia);
        adapter.notifyItemInserted(index);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MediaModelUploadEvent mediaModelUploadEvent) {
        for (int i = 0; i < userStoryElements.size(); i++) {
            if (userStoryElements.get(i).elementType == UserStoryElementType.ELEMENT_TYPE_MEDIA) {
                ArrayList<MediaModel> mediaModels = userStoryElements.get(i).mediaModel.mediaModels;
                boolean found = false;
                for (int j = 0; j < mediaModels.size(); j++) {
                    if (!StringUtils.isNullOrEmpty(mediaModels.get(j).getRequestId()) && mediaModels.get(j).getRequestId().equalsIgnoreCase(mediaModelUploadEvent.requestId)) {
                        mediaModels.get(j).setPublicId(mediaModelUploadEvent.publicId);
                        mediaModels.get(j).setUrl(mediaModelUploadEvent.serverUrl);
                        mediaModels.get(j).removeTempFile();
                        found = true;
                        break;
                    }
                }
                if (found) {
                    adapter.notifyItemChanged(i);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            startActivity(new Intent(this, MyWorldsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void initComponent() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager = new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        if (adapter == null)
            adapter = new UserStoryElementListAdapter(getApplicationContext(), userStoryElements, mediaClickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideKeyBoard();
            }
        }, 100);
        (fab_message = findViewById(R.id.fab_message)).setOnClickListener(this);
        (fab_media = findViewById(R.id.fab_media)).setOnClickListener(this);
        (fab_audio = findViewById(R.id.fab_audio)).setOnClickListener(this);
        (fab_location = findViewById(R.id.fab_location)).setOnClickListener(this);

        findViewById(R.id.layout_content).setOnClickListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public int lastPos, firstPos;
            public int highlightPosition;
            boolean isIdle = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    isIdle = true;
                    if ((adapter.middlePosition != highlightPosition || !adapter.showMediaPlayer) && adapter.middlePosition < adapter.getItemCount()) {
                        adapter.showMediaPlayer = true;
                        adapter.checkStopForMedia();
                        if (adapter.getAllItems().get(adapter.middlePosition).elementType == UserStoryElementType.ELEMENT_TYPE_MEDIA)
                            adapter.notifyItemChanged(adapter.middlePosition);
                    }
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    hideKeyBoard();
                    isIdle = false;
                    if (adapter.middlePosition != highlightPosition)
                        adapter.showMediaPlayer = false;
                } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                hideKeyBoard();
                int firstPos = layoutManager.findFirstCompletelyVisibleItemPosition();
                int lastPos = layoutManager.findLastCompletelyVisibleItemPosition();
                int newPos = -1;
                if (this.firstPos != firstPos) {
                    newPos = firstPos;
                    this.firstPos = firstPos;
                } else if (this.lastPos != lastPos) {
                    newPos = lastPos;
                    this.lastPos = lastPos;
                }
                if (newPos == 0) newPos = 1;

                if (highlightPosition != newPos && newPos != -1) {
                    highlightPosition = newPos;
                    if (isIdle)
                        adapter.showMediaPlayer = true;
                    else
                        adapter.showMediaPlayer = false;
                    adapter.updateHighlightPosition(highlightPosition);
                }
            }
        });
    }

    private void initActionList() {
        RecyclerView actionRecyclerView = findViewById(R.id.action_recycler_view);
        actionRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        actionListBottomSheetPager = BottomSheetBehavior.from(actionRecyclerView);
        final UserStoryActionListAdapter actionButtonAdapter;
        actionRecyclerView.setAdapter(actionButtonAdapter = new UserStoryActionListAdapter(new UserStoryActionListAdapter.UserStoryActionButtonListener() {
            @Override
            public void onClick(UserStoryElementType elementType) {
                actionListBottomSheetPager.setState(BottomSheetBehavior.STATE_COLLAPSED);
                switch (elementType) {
                    case ELEMENT_TYPE_TEXT:
                        findViewById(R.id.fab_message).performClick();
                        break;
                    case ELEMENT_TYPE_AUDIO:
                        findViewById(R.id.fab_audio).performClick();
                        break;
                    case ELEMENT_TYPE_MEDIA:
                        findViewById(R.id.fab_media).performClick();
                        break;
                    case ELEMENT_TYPE_LOCATION:
                        findViewById(R.id.fab_location).performClick();
                        break;
                }
            }
        }));
        actionButtonAdapter.addActionButtonItem(new UserStoryActionListAdapter.UserStoryActionButton("Add Text", R.mipmap.add_text, UserStoryElementType.ELEMENT_TYPE_TEXT));
        actionButtonAdapter.addActionButtonItem(new UserStoryActionListAdapter.UserStoryActionButton("Add Media", R.mipmap.add_media, UserStoryElementType.ELEMENT_TYPE_MEDIA));
        actionButtonAdapter.addActionButtonItem(new UserStoryActionListAdapter.UserStoryActionButton("Add Audio", R.mipmap.add_voice, UserStoryElementType.ELEMENT_TYPE_AUDIO));
        actionButtonAdapter.addActionButtonItem(new UserStoryActionListAdapter.UserStoryActionButton("Add Location", R.mipmap.add_location, UserStoryElementType.ELEMENT_TYPE_LOCATION));

        //        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() { actionButtonAdapter.notifyItemInserted(0);}}, 5000);
//
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() { actionButtonAdapter.notifyItemInserted(1);}}, 300);
//
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() { actionButtonAdapter.notifyItemInserted(2);}}, 500);
//
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//            }
//        }, 2000);

//        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getApplicationContext(), android.R.anim.slide_in_left);
//        actionRecyclerView.setLayoutAnimation(controller);
//        recyclerView.scheduleLayoutAnimation();
//        actionRecyclerView.getAdapter().notifyDataSetChanged();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.REQUEST_CODE.REQUEST_AUDIO:
                scrollToBottom();
                break;
            case Constants.REQUEST_CODE.REQUEST_GALLERY_MEDIA:
                if (resultCode == RESULT_OK) {
                    UserStoryMediaModel mediaModel = data.getParcelableExtra(Constants.BUNDLE_KEY.SELECTED_MEDIA);
                    mediaModel.mediaCount = mediaModel.mediaModels.size();
                    UserStoryElement userStoryElement = new UserStoryElement(mediaModel);
                    userStoryElements.add(userStoryElement);
                    adapter.notifyItemInserted(userStoryElements.size() - 1);
                    scrollToBottom();
                    prepareMediaFiles(mediaModel);
                }
                break;
            case Constants.REQUEST_CODE.REQUEST_LOCATION:
                if (resultCode == RESULT_OK) {
                    UserStoryAddressModel addressModel = data.getParcelableExtra(Constants.BUNDLE_KEY.SELECTED_LOCATION);
                    UserStoryElement textElement = new UserStoryElement(addressModel);
                    userStoryElements.add(textElement);
                    adapter.notifyItemInserted(userStoryElements.size() - 1);
                    scrollToBottom();
                }
                break;
            case Constants.REQUEST_CODE.REQUEST_EDIT_LOCATION:
                if (resultCode == RESULT_OK) {
                    adapter.getAllItems().get(editLocationIndex).addressModel = data.getParcelableExtra(Constants.BUNDLE_KEY.SELECTED_LOCATION);
                    adapter.notifyItemChanged(editLocationIndex);
                }
                break;

            case Constants.REQUEST_CODE.REQUEST_EDIT_GALLERY_MEDIA:
                if (resultCode == RESULT_OK) {
                    UserStoryMediaModel mediaModel = data.getParcelableExtra(Constants.BUNDLE_KEY.SELECTED_MEDIA);
                    mediaModel.mediaCount = mediaModel.mediaModels.size();
                    adapter.getAllItems().get(editMediaIndex).mediaModel = mediaModel;
                    adapter.notifyItemChanged(editMediaIndex);
                }
                break;
        }
    }

    private void prepareMediaFiles(final UserStoryMediaModel userStoryMediaModel) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                for (MediaModel mediaModel : userStoryMediaModel.mediaModels) {
                    if ((mediaModel.sourceType == MEDIA_SOURCE_TYPE.TYPE_LOCAL || mediaModel.isEdited)
                            && !StringUtils.isNullOrEmpty(mediaModel.getPathFile()) && StringUtils.isNullOrEmpty(mediaModel.getCompressPath())) {
                        if (mediaModel.mediaCellType == MEDIA_CELL_TYPE.TYPE_IMAGE) {
                            String compressPath = MyFileSystem.compressImage(mediaModel.getPathFile());
                            mediaModel.setCompressPath(compressPath);
                        } else if (mediaModel.mediaCellType == MEDIA_CELL_TYPE.TYPE_VIDEO) {
                            String compressPath = MyFileSystem.compressVideo(mediaModel.getPathFile());
                            mediaModel.setCompressPath(compressPath);
                        }
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                startUploadingFiles(userStoryMediaModel);
            }
        }.execute((Void) null);
    }

    private boolean startUploadingFiles(UserStoryMediaModel userStoryMediaModel) {
        MediaModel mediaModelToUpload = null;
        for (MediaModel mediaModel : userStoryMediaModel.mediaModels) {
            if (StringUtils.isNullOrEmpty(mediaModel.getRequestId()) && !mediaModel.isDeleted) {
                if (mediaModel.sourceType != MEDIA_SOURCE_TYPE.TYPE_CLOUD || mediaModel.isEdited) {
                    mediaModelToUpload = mediaModel;
                    if (mediaModelToUpload.isEdited || mediaModelToUpload.sourceType == MEDIA_SOURCE_TYPE.TYPE_LOCAL) {
                        doUploadLocalFile(mediaModelToUpload);
                    } else {
                        doUploadRemoteFile(mediaModelToUpload);
                    }
                }
            }
        }

        if (mediaModelToUpload != null) {
//            if (mediaModelToUpload.isEdited || mediaModelToUpload.sourceType == MEDIA_SOURCE_TYPE.TYPE_LOCAL) {
//                doUploadLocalFile(mediaModelToUpload);
//            } else {
//                doUploadRemoteFile(mediaModelToUpload);
//            }
            return true;
        }
        return false;
    }

    private void doUploadLocalFile(MediaModel mediaModel) {
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
                String requestId = CloudinaryManager.uploadImageFile(fileToUpload);
                mediaModel.setRequestId(requestId);
            } else if (mediaModel.mediaCellType == MEDIA_CELL_TYPE.TYPE_VIDEO) {
                mediaModel.setIsUploaded(false);
                String requestId = CloudinaryManager.uploadVideoFile(fileToUpload);
                mediaModel.setRequestId(requestId);
            }
        }
    }

    private void doUploadRemoteFile(final MediaModel mediaModel) {
        String fileToUpload = null;
        if (!StringUtils.isNullOrEmpty(mediaModel.getUrl())) {
            fileToUpload = mediaModel.getUrl();
        }
        if (!StringUtils.isNullOrEmpty(fileToUpload)) {
            if (mediaModel.mediaCellType == MEDIA_CELL_TYPE.TYPE_IMAGE) {
                mediaModel.setIsUploaded(true);
                String serverUrl = MediaManager.get().url().generate(fileToUpload);
                mediaModel.setThumbnail(null);
                mediaModel.setUrl(serverUrl);
                mediaModel.setPublicId(fileToUpload);
                mediaModel.sourceType = MEDIA_SOURCE_TYPE.TYPE_CLOUD;
            } else if (mediaModel.mediaCellType == MEDIA_CELL_TYPE.TYPE_VIDEO) {
                mediaModel.setIsUploaded(false);
                String requestId = CloudinaryManager.uploadVideoFile(fileToUpload);
                mediaModel.setRequestId(requestId);
            }
        }
    }

    private void startDeletingFiles(final UserStoryMediaModel userStoryMediaModel) {
        new AsyncTask<Void, Map, Void>() {
            MediaModel getMediaToDelete() {
                for (MediaModel mediaModel : userStoryMediaModel.mediaModels) {
                    if (mediaModel.isDeleted || (mediaModel.isEdited && mediaModel.sourceType == MEDIA_SOURCE_TYPE.TYPE_CLOUD)) {
                        return mediaModel;
                    }
                }
                return null;
            }

            @Override
            protected Void doInBackground(Void... voids) {

                while (true) {
                    MediaModel mediaModel = getMediaToDelete();
                    if (mediaModel != null) {
                        Map map = CloudinaryManager.deleteFile(mediaModel.getPublicId());
                        mediaModel.setPublicId("");
                        mediaModel.setRequestId("");
                        mediaModel.sourceType = MEDIA_SOURCE_TYPE.TYPE_LOCAL;
                        mediaModel.isEdited = false;
                        if (mediaModel.isDeleted)
                            userStoryMediaModel.mediaModels.remove(mediaModel);
                        this.publishProgress(map);
                    } else {
                        break;
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute((Void) null);
    }

    private void scrollToBottom() {
        layoutManager.smoothScrollToPosition(recyclerView, null, adapter.getItemCount() - 1);
    }

    @Override
    public void onClick(View v) {
        hideKeyBoard();
        deselectAll();
        if (v.getId() == R.id.fab_media) {
            fab_media.setSelected(true);
            Intent intent = new Intent(getApplicationContext(), UserStoryMediaListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.BUNDLE_KEY.ADD_PICTURE, true);
            intent.putExtras(bundle);
            startActivityForResult(intent, Constants.REQUEST_CODE.REQUEST_GALLERY_MEDIA);
        } else if (v.getId() == R.id.fab_location) {
            fab_location.setSelected(true);
            Intent intent = new Intent(getApplicationContext(), GoogleMapActivity.class);
            startActivityForResult(intent, Constants.REQUEST_CODE.REQUEST_LOCATION);
        } else if (v.getId() == R.id.fab_audio) {
            fab_audio.setSelected(true);
            pushFragment(FRAGMENTS.AUDIO_RECORDER, null, true, true);
        } else if (v.getId() == R.id.fab_message) {
            fab_message.setSelected(true);
            UserStoryElement textElement = new UserStoryElement("", UserStoryElementType.ELEMENT_TYPE_TEXT);
            userStoryElements.add(textElement);
            adapter.notifyItemInserted(userStoryElements.size() - 1);
            scrollToBottom();
        }
    }

    void deselectAll() {
        fab_audio.setSelected(false);
        fab_media.setSelected(false);
        fab_message.setSelected(false);
        fab_location.setSelected(false);

    }
}
