package com.bravvura.nestledtime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bravvura.nestledtime.activity.BaseActivity;
import com.bravvura.nestledtime.mediagallery.model.MediaModel;
import com.bravvura.nestledtime.userstory.adapter.UserStoryElementListAdapter;
import com.bravvura.nestledtime.userstory.listener.OnMediaClickListener;
import com.bravvura.nestledtime.userstory.model.UserStoryAddressModel;
import com.bravvura.nestledtime.userstory.model.UserStoryElement;
import com.bravvura.nestledtime.userstory.model.UserStoryElementType;
import com.bravvura.nestledtime.userstory.model.UserStoryMediaModel;
import com.bravvura.nestledtime.userstory.ui.activity.GoogleMapActivity;
import com.bravvura.nestledtime.userstory.ui.activity.UserStoryMediaListActivity;
import com.bravvura.nestledtime.userstory.ui.activity.UserStoryMediaPagerActivity;
import com.bravvura.nestledtime.utils.Constants;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private UserStoryElementListAdapter adapter;
    private LinearLayoutManager layoutManager;
    private int editMediaIndex, editLocationIndex;
    private OnMediaClickListener mediaClickListener = new OnMediaClickListener() {
        @Override
        public void onClick(UserStoryElement userStoryElement, int index) {
            switch (userStoryElement.elementType) {
                case ELEMENT_TYPE_MEDIA: {
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
        public void onRemoveClick(UserStoryElement userStoryElement, int index) {
            adapter.removeIndex(index);
            adapter.notifyItemChanged(index);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_story);
        initComponent();
        setupToolBar();
        setTitle("New Memory");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {

        }
        return super.onOptionsItemSelected(item);
    }

    private void initComponent() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager = new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(false);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.transparent_divider));
//        recyclerView.addItemDecoration(itemDecorator);
        adapter = new UserStoryElementListAdapter(getApplicationContext(), mediaClickListener);
        UserStoryElement textElement = new UserStoryElement("", UserStoryElementType.ELEMENT_TYPE_TITLE);
        adapter.addResult(textElement);
        recyclerView.setAdapter(adapter);
        findViewById(R.id.fab_message).setOnClickListener(this);
        findViewById(R.id.fab_media).setOnClickListener(this);
        findViewById(R.id.fab_audio).setOnClickListener(this);
        findViewById(R.id.fab_location).setOnClickListener(this);
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
                    if (adapter.middlePosition != highlightPosition || !adapter.showMediaPlayer) {
                        adapter.showMediaPlayer = true;
                        adapter.checkStopForMedia();
                        adapter.notifyItemChanged(adapter.middlePosition);
                    }
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    isIdle = false;
                    if (adapter.middlePosition != highlightPosition)
                        adapter.showMediaPlayer = false;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
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
                    adapter.addResult(new UserStoryElement(mediaModel));
                    adapter.notifyItemInserted(adapter.getItemCount() - 1);
                    scrollToBottom();
                }
                break;
            case Constants.REQUEST_CODE.REQUEST_LOCATION:
                if (resultCode == RESULT_OK) {
                    UserStoryAddressModel addressModel = data.getParcelableExtra(Constants.BUNDLE_KEY.SELECTED_LOCATION);
                    UserStoryElement textElement = new UserStoryElement(addressModel);
                    adapter.addResult(textElement);
                    adapter.notifyItemInserted(adapter.getItemCount() - 1);
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
                    adapter.getAllItems().get(editMediaIndex).mediaModel = mediaModel;
                    adapter.notifyItemChanged(editMediaIndex);
                }
                break;
        }
    }

    private void scrollToBottom() {
        layoutManager.smoothScrollToPosition(recyclerView, null, adapter.getItemCount() - 1);
    }

    @Override
    public void onClick(View v) {
        hideKeyBoard();
        if (v.getId() == R.id.fab_media) {
            Intent intent = new Intent(getApplicationContext(), UserStoryMediaListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.BUNDLE_KEY.ADD_PICTURE, true);
            intent.putExtras(bundle);
            startActivityForResult(intent, Constants.REQUEST_CODE.REQUEST_GALLERY_MEDIA);
        } else if (v.getId() == R.id.fab_location) {
            Intent intent = new Intent(getApplicationContext(), GoogleMapActivity.class);
            startActivityForResult(intent, Constants.REQUEST_CODE.REQUEST_LOCATION);
        } else if (v.getId() == R.id.fab_audio) {

        } else if (v.getId() == R.id.fab_message) {
            UserStoryElement textElement = new UserStoryElement("", UserStoryElementType.ELEMENT_TYPE_TEXT);
            adapter.addResult(textElement);
            adapter.notifyItemInserted(adapter.getItemCount() - 1);
            scrollToBottom();
        }
    }
}
