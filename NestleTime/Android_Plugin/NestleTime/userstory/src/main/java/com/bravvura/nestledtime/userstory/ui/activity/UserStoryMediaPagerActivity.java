package com.bravvura.nestledtime.userstory.ui.activity;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.activity.BaseActivity;
import com.bravvura.nestledtime.userstory.adapter.UserStoryMediaPagerAdapter;
import com.bravvura.nestledtime.userstory.model.MediaStopEventModel;
import com.bravvura.nestledtime.userstory.model.UserStoryElement;
import com.bravvura.nestledtime.userstory.model.UserStoryMediaModel;
import com.bravvura.nestledtime.utils.Constants;

import org.greenrobot.eventbus.EventBus;

import java.util.Random;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 07-03-2018.
 */

public class UserStoryMediaPagerActivity extends BaseActivity {
    private ViewPager viewPager;
    UserStoryMediaModel userStoryMediaModel;
    private int index;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_story_pager);
        setupToolBar();
        initArguments();
        initComponent();
//        registerReceiver(downloadCompleteReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
        void abc(){}
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0L);
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(id);
            Cursor cursor = downloadManager.query(query);

// it shouldn't be empty, but just in case
            if (cursor.moveToFirst()) {
                int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(statusIndex)) {
                    int uriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                    String downloadedPackageUriString = cursor.getString(uriIndex);
                    if (downloadedPackageUriString.substring(0, 7).matches("file://")) {
                        downloadedPackageUriString =  downloadedPackageUriString.substring(7);
                    }
                    Intent notificationIntent = new Intent(Intent.ACTION_VIEW);

                    notificationIntent.setData(Uri.parse(downloadedPackageUriString));
                    PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
                    Notification notification = new NotificationCompat.Builder(context)
                            .setTicker("yortext")
                            .setSmallIcon(android.R.drawable.ic_menu_report_image)
                            .setContentTitle("yortext")
                            .setContentText("sdsd")
                            .setContentIntent(contentIntent)
                            .setAutoCancel(true)
                            .build();
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(new Random(System.currentTimeMillis()).nextInt(), notification);
                }


            }
        }
    };


// when initialize


    private void initArguments() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(Constants.BUNDLE_KEY.USER_STORY_MEDIA_MODEL)) {
            userStoryMediaModel = bundle.getParcelable(Constants.BUNDLE_KEY.USER_STORY_MEDIA_MODEL);
            index = bundle.getInt(Constants.BUNDLE_KEY.INDEX, 0);
        }
    }

    private void initComponent() {
        if (toolbar != null)
            toolbar.setBackgroundResource(R.color.semi_transparent_grey);

        viewPager = findViewById(R.id.view_pager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTitle(position + 1 + " of " + userStoryMediaModel.mediaModels.size());
                MediaStopEventModel model = new MediaStopEventModel();
                model.selectedIndex = position;
                EventBus.getDefault().post(model);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        PagerAdapter adapter = new UserStoryMediaPagerAdapter(getSupportFragmentManager(), userStoryMediaModel);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(index);
        setTitle(index + 1 + " of " + userStoryMediaModel.mediaModels.size());

    }
}
