package com.bravvura.nestledtime;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bravvura.nestledtime.mediagallery.model.MediaModel;
import com.bravvura.nestledtime.activity.BaseActivity;
import com.bravvura.nestledtime.userstory.ui.activity.UserStoryMediaListActivity;
import com.bravvura.nestledtime.utils.Constants;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    TextView textMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                        message = message + " \n" + mediaModel.getUrl();
                    }
                    textMessage.setText(message);
                }
                break;
        }
    }
}
