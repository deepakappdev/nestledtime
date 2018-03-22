package com.bravvura.nestledtime.mediagallery.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.activity.BaseActivity;
import com.bravvura.nestledtime.eventbusmodel.MessageFacebookIndexEvent;
import com.bravvura.nestledtime.eventbusmodel.MessageLocalIndexEvent;
import com.bravvura.nestledtime.mediagallery.adapter.AllPhotoGalleryAdapter;
import com.bravvura.nestledtime.mediagallery.listener.MediaElementClick;
import com.bravvura.nestledtime.mediagallery.model.MEDIA_CELL_TYPE;
import com.bravvura.nestledtime.mediagallery.model.MEDIA_SOURCE_TYPE;
import com.bravvura.nestledtime.mediagallery.model.MediaModel;
import com.bravvura.nestledtime.mediagallery.model.facebook.FacebookPhotoData;
import com.bravvura.nestledtime.network.volley.NetworkError;
import com.bravvura.nestledtime.network.volley.RequestCallback;
import com.bravvura.nestledtime.network.volley.RequestController;
import com.bravvura.nestledtime.userstory.ui.fragment.BaseFragment;
import com.bravvura.nestledtime.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 15-03-2018.
 */

public class FacebookPhotoFragment extends BaseFragment implements RequestCallback {
    private ViewGroup layoutFacebookLogin;
    private ArrayList<MediaModel> mediaModels = new ArrayList<>();
    private AllPhotoGalleryAdapter adapter;
    private MenuItem menuItem;
    private int myIndex;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        initArguments();
        return inflater.inflate(R.layout.frag_facebook_photo_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        fetchFaceBookPhoto();
    }

    private void initArguments() {
        Bundle bundle = getArguments();
        myIndex = bundle.getInt(Constants.BUNDLE_KEY.INDEX, -1);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageFacebookIndexEvent event) {
        if (event.selectedIndex != myIndex) {
            for (int i = 0; i < mediaModels.size(); i++) {
                mediaModels.get(i).setSelected(false);
            }
            adapter.notifyDataSetChanged();
            menuItem.setEnabled(getSelectedMediaCount() > 0);
        }
    }

    private void fetchFaceBookPhoto() {
        RequestController.getFacebookPhotos(this);
    }

    private void initView(View view) {
        layoutFacebookLogin = view.findViewById(R.id.layout_facebook_login);
        layoutFacebookLogin.setVisibility(View.GONE);
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
    public void error(NetworkError volleyError) {

    }

    @Override
    public void success(Object obj) {
        if (getActivity() != null && getContext() != null && obj instanceof JSONObject) {
            JSONObject json = (JSONObject) obj;
            try {
                JSONObject jsonData = json.getJSONObject("data");
                Iterator<String> iter = jsonData.keys();
                mediaModels.clear();
                while (iter.hasNext()) {
                    String key = iter.next();
                    try {
                        JSONObject jsonObj = jsonData.getJSONObject(key);
                        FacebookPhotoData photoData = new Gson().fromJson(jsonObj.toString(), new TypeToken<FacebookPhotoData>() {
                        }.getType());
                        MediaModel mediaModel = new MediaModel();
                        mediaModel.sourceType = MEDIA_SOURCE_TYPE.TYPE_FACEBOOK;
                        mediaModel.mediaCellType = MEDIA_CELL_TYPE.TYPE_IMAGE;
                        mediaModel.setTitle(photoData.title);
                        mediaModel.setUrl(photoData.images.source);
                        mediaModel.setThumbnail(photoData.images.source);
                        mediaModels.add(mediaModel);
                    } catch (Exception e) {
                        // Something went wrong!
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter.notifyDataSetChanged();
        }
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
        inflater.inflate(R.menu.menu_done, menu);
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
