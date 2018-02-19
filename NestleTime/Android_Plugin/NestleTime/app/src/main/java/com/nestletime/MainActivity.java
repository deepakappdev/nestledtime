package com.nestletime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.nestletime.mediagallery.model.MediaModel;
import com.nestletime.mediagallery.ui.MediaGalleryActivity;
import com.nestletime.ui.activity.BaseActivity;
import com.nestletime.userstory.ui.activity.UserStoryMediaListActivity;
import com.nestletime.utils.Constants;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    TextView textMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setContentView(R.layout.activity_main);
        textMessage = findViewById(R.id.text_message);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserStoryMediaListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constants.BUNDLE_KEY.ADD_PICTURE, true);
                intent.putExtras(bundle);
                startActivityForResult(intent, Constants.REQUEST_CODE.REQUEST_GALLERY_MEDIA);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.REQUEST_CODE.REQUEST_GALLERY_MEDIA:
                if (resultCode == RESULT_OK) {
                    ArrayList<MediaModel> mediaModels = data.getExtras().getParcelableArrayList(Constants.BUNDLE_KEY.SELECTED_MEDIA);
                    String message = "";
                    for (MediaModel mediaModel : mediaModels) {
                        message = message + "\n" + mediaModel.getRequestId();
                    }
                    textMessage.setText(message);
                }
                break;
        }
    }
}
