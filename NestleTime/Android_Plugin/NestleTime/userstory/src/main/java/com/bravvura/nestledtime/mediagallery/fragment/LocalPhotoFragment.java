package com.bravvura.nestledtime.mediagallery.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import com.bravvura.nestledtime.mediagallery.model.MEDIA_SOURCE_TYPE;
import com.bravvura.nestledtime.mediagallery.model.MediaModel;
import com.bravvura.nestledtime.mediagallery.ui.MediaGalleryActivity;
import com.bravvura.nestledtime.userstory.ui.fragment.BaseFragment;
import com.bravvura.nestledtime.utils.Constants;
import com.bravvura.nestledtime.utils.MyFileSystem;
import com.bravvura.nestledtime.utils.MyLogs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Deepak Saini on 07-02-2018.
 */

public class LocalPhotoFragment extends BaseFragment {
    private AllPhotoGalleryAdapter adapter;
    ArrayList<MediaModel> mediaModels;
    private MenuItem menuItem;
    private File newCameraFile;

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
        if (event.insertionIndex != 0)
            adapter.notifyItemRangeInserted(event.insertionIndex, event.totalCount);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
//        fetchAllPicture();
    }

//    private void fetchAllPicture() {
//        adapter.setResult(mediaModels);
//        adapter.notifyDataSetChanged();
//    }


    private void initComponent(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mediaModels.get(position).mediaCellType == MEDIA_CELL_TYPE.TYPE_HEADER
                        /*|| mediaModels.get(position).mediaCellType == MEDIA_CELL_TYPE.TYPE_CAMERA*/)
                    return 3;
                else return 1;
            }
        });
        recyclerView.setLayoutManager(layoutManager);

        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();

        mediaModels = ((MediaGalleryActivity) getActivity()).photoModels;
        adapter = new AllPhotoGalleryAdapter(mediaModels, width / 3);
        adapter.setOnMediaClickListener(new MediaElementClick() {
            @Override
            public void onClick(int index, MediaModel mediaModel) {
                if (mediaModel.mediaCellType == MEDIA_CELL_TYPE.TYPE_CAMERA) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    newCameraFile = MyFileSystem.getTempImageFile();

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newCameraFile));
                    } else {
                        File file = new File(Uri.fromFile(newCameraFile).getPath());
                        Uri photoUri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".provider", file);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    }
                    cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(cameraIntent, Constants.REQUEST_CODE.REQUEST_CAMERA);
                } else {
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
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_CODE.REQUEST_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    MediaModel mediaModel = new MediaModel();
                    mediaModel.mediaCellType=MEDIA_CELL_TYPE.TYPE_IMAGE;
                    mediaModel.setSelected(true);
                    mediaModel.sourceType = MEDIA_SOURCE_TYPE.TYPE_LOCAL;
                    mediaModel.setPathFile(newCameraFile.getAbsolutePath());
                    mediaModel.setLastModified(System.currentTimeMillis());

                    MediaModel headerModel = getDateHeader(newCameraFile.lastModified());


                    MediaModel firstMediaModel = null;
                    if(mediaModels.size()>1)
                        firstMediaModel = mediaModels.get(1);

                    if(firstMediaModel==null || !firstMediaModel.getDate().equalsIgnoreCase(mediaModel.getDate())) {
                        mediaModels.add(1, mediaModel);
                        mediaModels.add(1, headerModel);
                    } else {
                        mediaModels.add(2, mediaModel);
                    }
                    adapter.notifyDataSetChanged();
                    menuItem.setEnabled(true);

                } else if (resultCode == Activity.RESULT_CANCELED) {
                    MyFileSystem.deleteFile(newCameraFile);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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
