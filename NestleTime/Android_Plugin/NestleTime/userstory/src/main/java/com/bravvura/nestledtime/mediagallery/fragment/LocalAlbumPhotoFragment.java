package com.bravvura.nestledtime.mediagallery.fragment;

import android.os.AsyncTask;
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
import com.bravvura.nestledtime.mediagallery.filesystem.CursorManager;
import com.bravvura.nestledtime.mediagallery.listener.MediaElementClick;
import com.bravvura.nestledtime.mediagallery.model.MEDIA_CELL_TYPE;
import com.bravvura.nestledtime.mediagallery.model.MediaModel;
import com.bravvura.nestledtime.userstory.ui.fragment.BaseFragment;
import com.bravvura.nestledtime.utils.Constants;
import com.bravvura.nestledtime.utils.MyFileSystem;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Deepak Saini on 07-02-2018.
 */

public class LocalAlbumPhotoFragment extends BaseFragment {
    private AllPhotoGalleryAdapter adapter;
    private ArrayList<MediaModel> mediaModels = new ArrayList<>();
    private MediaModel albumModel;
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
        initComponent(view);
        fetchFolderPicture();
    }

    private void fetchFolderPicture() {

        Bundle bundle = getArguments();
        if (bundle == null) return;
        albumModel = bundle.getParcelable(Constants.BUNDLE_KEY.MEDIA_MODEL);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                makeMediaModels();
                return null;
            }

            @Override
            protected void onPreExecute() {
                mediaModels.clear();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (getActivity() == null) return;
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            private void makeMediaModels() {
                File file = new File(albumModel.getPathFolder());
                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    Arrays.sort(files, new Comparator() {
                        public int compare(Object o1, Object o2) {
                            if (((File) o1).lastModified() > ((File) o2).lastModified()) return -1;
                            else if (((File) o1).lastModified() < ((File) o2).lastModified())
                                return +1;
                            else return 0;
                        }
                    });

                    for (File fileTmp : files) {
                        if (fileTmp.exists()) {
                            boolean isImage = MyFileSystem.isImageFile(fileTmp);
                            boolean isVideo = !isImage ? MyFileSystem.isVideoFile(fileTmp) : false;
                            boolean check = isImage || isVideo;
                            if (!fileTmp.isDirectory() && check) {
                                MediaModel mediaModel = new MediaModel("", fileTmp.getAbsolutePath(), fileTmp.getAbsolutePath(), fileTmp.lastModified());
                                if (mediaModels.isEmpty() || !mediaModels.get(mediaModels.size() - 1).getDate().equalsIgnoreCase(mediaModel.getDate())) {
                                    mediaModels.add(getDateHeader(mediaModel.getLastModified()));
                                }
                                if (isImage)
                                    mediaModel.mediaCellType = MEDIA_CELL_TYPE.TYPE_IMAGE;
                                else {
                                    if (getActivity() == null || getContext() == null) return;
                                    mediaModel.setId((int) CursorManager.getVideoIdFromFilePath(getContext(), mediaModel.getPathFile()));
                                    mediaModel.mediaCellType = MEDIA_CELL_TYPE.TYPE_VIDEO;
                                }
                                mediaModels.add(mediaModel);
                                publishProgress(null);
                            }
                        }
                    }
                }
            }
        }.execute((Void) null);
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
