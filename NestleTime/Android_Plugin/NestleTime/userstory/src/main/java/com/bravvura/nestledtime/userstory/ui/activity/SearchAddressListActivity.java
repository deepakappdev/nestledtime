package com.bravvura.nestledtime.userstory.ui.activity;

import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.activity.BaseActivity;
import com.bravvura.nestledtime.model.googleaddress.PlacePredictions;
import com.bravvura.nestledtime.network.volley.NetworkError;
import com.bravvura.nestledtime.network.volley.RequestCallback;
import com.bravvura.nestledtime.network.volley.RequestController;
import com.bravvura.nestledtime.userstory.adapter.BaseTextAdapter;
import com.bravvura.nestledtime.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deepak Saini on 19-02-2018.
 */

public class SearchAddressListActivity extends BaseActivity {

    private EditText editText;
    private RecyclerView recyclerView;
    private BaseTextAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_address);
        initComponent();
    }

    private void initComponent() {
        editText = findViewById(R.id.edit_text);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new BaseTextAdapter();
        recyclerView.setAdapter(adapter);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        RequestController.getGooglePlace(s.toString(), new RequestCallback() {
                            @Override
                            public void error(NetworkError volleyError) { }

                            @Override
                            public void success(Object object) {
                                PlacePredictions predictions = (PlacePredictions) object;
                                adapter.setResults(predictions.getResults());
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                };

                if (handler != null) {
                    handler.removeCallbacksAndMessages(null);
                } else {
                    handler = new Handler();
                }
                handler.postDelayed(run, 800);
            }
        });
    }
}
