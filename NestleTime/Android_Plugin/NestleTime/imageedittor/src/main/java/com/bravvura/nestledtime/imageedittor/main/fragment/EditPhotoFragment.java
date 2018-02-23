package com.bravvura.nestledtime.imageedittor.main.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bravvura.nestledtime.imageedittor.R;
import com.bravvura.nestledtime.imageedittor.brightness.BrightnessFragment;
import com.bravvura.nestledtime.imageedittor.brightness.OnBrightnessListener;
import com.bravvura.nestledtime.imageedittor.contrast.ContrastFragment;
import com.bravvura.nestledtime.imageedittor.contrast.OnContrastListener;
import com.bravvura.nestledtime.imageedittor.crop.CropFragment;
import com.bravvura.nestledtime.imageedittor.crop.OnCropListener;
import com.bravvura.nestledtime.imageedittor.filter.FilterFragment;
import com.bravvura.nestledtime.imageedittor.filter.OnFilterListener;
import com.bravvura.nestledtime.imageedittor.main.StartSnapHelper;
import com.bravvura.nestledtime.imageedittor.main.Utils;
import com.bravvura.nestledtime.imageedittor.main.activity.EditPhotoActivity;
import com.bravvura.nestledtime.imageedittor.main.adapter.EditAdapter;
import com.bravvura.nestledtime.imageedittor.main.model.EditType;
import com.bravvura.nestledtime.imageedittor.rotate.OnRotateListener;
import com.bravvura.nestledtime.imageedittor.rotate.RotateFragment;
import com.bravvura.nestledtime.imageedittor.saturation.OnSaturationListener;
import com.bravvura.nestledtime.imageedittor.saturation.SaturationFragment;
import com.bravvura.nestledtime.imageedittor.ui.fragment.BaseFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Hoang Anh Tuan on 11/23/2017.
 */

public class EditPhotoFragment extends BaseFragment implements
        EditAdapter.OnItemEditPhotoClickedListener,
        OnCropListener,
        OnFilterListener,
        OnRotateListener,
        OnSaturationListener,
        OnBrightnessListener,
        OnContrastListener {

    private static final String INPUT_URL = "inputUrl";

    ImageView ivPhotoView;
    RecyclerView listEdit;

    public static EditPhotoFragment create(String inputUrl) {
        EditPhotoFragment fragment = new EditPhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(INPUT_URL, inputUrl);
        fragment.setArguments(bundle);
        return fragment;
    }


    private String outputUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_photo, container, false);
        setHasOptionsMenu(true);
        ivPhotoView = view.findViewById(R.id.ivPhotoView);
        listEdit = view.findViewById(R.id.listEdit);
        setTitle("Edit Image");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {

        EditAdapter editAdapter = new EditAdapter(getActivity());
        editAdapter.setOnItemEditPhotoClickedListener(this);
        listEdit.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        listEdit.setAdapter(editAdapter);
        new StartSnapHelper().attachToRecyclerView(listEdit);
        editAdapter.add(EditType.Crop);
        editAdapter.add(EditType.Filter);
        editAdapter.add(EditType.Rotate);
        editAdapter.add(EditType.Saturation);
        editAdapter.add(EditType.Brightness);
        editAdapter.add(EditType.Contrast);

        if (getArguments() != null) {
            outputUrl = getArguments().getString(INPUT_URL);
            showPhoto(outputUrl);
        }
    }


    private void showPhoto(String outputUrl) {
        this.outputUrl = outputUrl;
        Glide.with(getContext())
                .load(new File(outputUrl))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(ivPhotoView);
    }

    @Override
    public void saveImage() {
        ((EditPhotoActivity)getActivity()).finishWithResult(outputUrl);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onItemEditPhotoClicked(EditType type) {
        switch (type) {
            case Crop:
                ((EditPhotoActivity) getActivity()).addFragmentToStack(CropFragment.create(outputUrl, this));
                break;
            case Filter:
                ((EditPhotoActivity) getActivity()).addFragmentToStack(FilterFragment.create(outputUrl, this));
                break;
            case Rotate:
                ((EditPhotoActivity) getActivity()).addFragmentToStack(RotateFragment.create(outputUrl, this));
                break;
            case Saturation:
                ((EditPhotoActivity) getActivity()).addFragmentToStack(SaturationFragment.create(outputUrl, this));
                break;
            case Brightness:
                ((EditPhotoActivity) getActivity()).addFragmentToStack(BrightnessFragment.create(outputUrl, this));
                break;
            case Contrast:
                ((EditPhotoActivity) getActivity()).addFragmentToStack(ContrastFragment.create(outputUrl, this));
                break;
        }
    }

    @Override
    public void onBrightnessPhotoCompleted(String s) {
        showPhoto(s);
    }

    @Override
    public void onContrastPhotoCompleted(String s) {
        showPhoto(s);
    }

    @Override
    public void onCropPhotoCompleted(String s) {
        showPhoto(s);
    }

    @Override
    public void onFilterPhotoCompleted(String s) {
        showPhoto(s);
    }

    @Override
    public void onRotatePhotoCompleted(String s) {
        showPhoto(s);
    }

    @Override
    public void onSaturationPhotoCompleted(String s) {
        showPhoto(s);
    }
}
