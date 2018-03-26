package com.bravvura.nestledtime.mediagallery.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;

import com.bravvura.nestledtime.activity.BaseActivity;
import com.bravvura.nestledtime.utils.Constants;
import com.bravvura.nestledtime.utils.MyLogs;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.common.api.GoogleApiClient;

import java.security.MessageDigest;
import java.util.Arrays;

/**
 * Created by sverdhiya on 8/31/2016.
 */
public class CommonSocialActivity extends BaseActivity {

    private CallbackManager mFacebookCallbackManager;
    Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bundle = getIntent().getExtras();

        if (bundle.getInt("type") == Constants.REQUEST_CODE.REQUEST_SOCIAL_FB_LOGIN) {
            facebookInitilization();
        } else if (bundle.getInt("type") == Constants.REQUEST_CODE.REQUEST_SOCIAL_FB_LOGOUT) {
            facebookLogout();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (bundle.getInt("type") == Constants.REQUEST_CODE.REQUEST_SOCIAL_FB_LOGIN) {
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void facebookInitilization() {
        FacebookSdk.sdkInitialize(getApplicationContext());

        mFacebookCallbackManager = CallbackManager.Factory.create();
        MyLogs.d("============ facebookInitilization " + mFacebookCallbackManager.toString());
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.munchado", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                MyLogs.d("YourKeyHash: ", "============  KEY HASH " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
        }

        facebookLogin();
    }

    public void facebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile, email, user_birthday, user_friends")); //, friends
        LoginManager.getInstance().registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                setResult(RESULT_OK, new Intent().putExtra("usertoken", loginResult.getAccessToken().getToken()));
                finish();

            }

            @Override
            public void onCancel() {
                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();
                }
                setResult(RESULT_CANCELED);
                finish();
            }

            @Override
            public void onError(FacebookException exception) {
                if (exception instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut();
                    }
                }
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void facebookLogout() {
        LoginManager.getInstance().logOut();
        setResult(RESULT_OK);
        finish();
    }
}
