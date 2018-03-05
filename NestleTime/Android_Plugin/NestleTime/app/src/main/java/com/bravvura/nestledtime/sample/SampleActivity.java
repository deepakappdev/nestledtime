package com.bravvura.nestledtime.sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bravvura.nestledtime.MainActivity;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 25-02-2018.
 */

public class SampleActivity extends AppCompatActivity {

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, MainActivity.class));
    }
}
