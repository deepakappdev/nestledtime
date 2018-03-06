package com.bravvura.nestledtime.imageedittor.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bravvura.nestledtime.imageedittor.R;
import com.bravvura.nestledtime.imageedittor.main.fragment.EditPhotoFragment;

/**
 * Created by Deepak Saini on 12/14/2017.
 */

public class EditPhotoActivity extends AppCompatActivity {

    public static final String INPUT_URL = "inputUrl";
    public static final String OUTPUT_URL = "inputUrl";
    TextView toolbarTitle;
    public static void start(Context context, String inputUrl) {
        Intent starter = new Intent(context, EditPhotoActivity.class);
        starter.putExtra(INPUT_URL, inputUrl);
        context.startActivity(starter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            onBackPressed();
        } else if(item.getItemId()==R.id.menu_done) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()>0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);
        setupToolBar();
        addFragment(EditPhotoFragment.create(getIntent().getStringExtra(INPUT_URL)));
    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_done, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    public void setupToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Edit Picture");
    }

    public void setTitle(String titleId) {
        toolbarTitle.setText(titleId);
    }

    private void addFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(R.id.rootMain, fragment);
        ft.commit();
    }

    public void addFragmentToStack(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.rootMain, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void finishWithResult(String filePath) {
        Intent intent = new Intent();
        intent.putExtra(OUTPUT_URL, filePath);
        setResult(RESULT_OK, intent);
        finish();
    }
}