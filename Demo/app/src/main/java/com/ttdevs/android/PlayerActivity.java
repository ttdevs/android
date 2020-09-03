package com.ttdevs.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class PlayerActivity extends BaseActivity implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    public static final String KEY_URL = "view_url";

    private static final String TAG = "VideoPlayActivity";
    private static final int PLAY_RETURN = 2 * 1000; // 2 seconds
    private static final String KEY_PLAY_POSITON = "paly_position";
    private static final String TOAST_ERROR_URL = "Paly url is null, please check parameter:" + KEY_URL;
    private static final String TOAST_ERROR_PLAY = "Paly error, please check url exist!";
    private static final String DIALOG_TITILE = "奋力加载中，请稍后...";

    private static String url;

    private ProgressDialog progressDialog;
    private MediaController mc;
    private VideoView videoView;
    private LinearLayout llMain;
    private LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        url = getIntent().getStringExtra(KEY_URL);
        url = "http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4";
        if (url == null && savedInstanceState != null) {
            url = savedInstanceState.getString(KEY_URL);
        }

        if (url == null) {
            Toast.makeText(getApplicationContext(), TOAST_ERROR_URL, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        setTheme(android.R.style.Theme_NoTitleBar_Fullscreen);

        videoView = new VideoView(this);
        videoView.setVideoURI(Uri.parse(url));
        videoView.requestFocus();
        videoView.setOnPreparedListener(this);
        videoView.setOnCompletionListener(this);
        videoView.setOnErrorListener(this);

        mc = new MediaController(this);
        mc.setAnchorView(videoView);
        mc.setKeepScreenOn(true);

        videoView.setMediaController(mc);

        llMain = new LinearLayout(this);
        llMain.setGravity(Gravity.CENTER_VERTICAL);
        llMain.setOrientation(LinearLayout.VERTICAL);
        llMain.setLayoutParams(params);

        llMain.addView(videoView, params);
        setContentView(llMain);

        initDialog();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        int palyPosition = videoView.getCurrentPosition();
        if (palyPosition > PLAY_RETURN) {
            palyPosition -= PLAY_RETURN;
        }
        outState.putInt(KEY_PLAY_POSITON, palyPosition);
        outState.putString(KEY_URL, url);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        videoView.seekTo(savedInstanceState.getInt(KEY_PLAY_POSITON));
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        progressDialog.cancel();

        videoView.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d(TAG, "onError:" + url);

        Toast.makeText(getApplicationContext(), TOAST_ERROR_PLAY + "\n" + url, Toast.LENGTH_LONG).show();
        progressDialog.cancel();
        finish();

        return true;
    }

    private void initDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(DIALOG_TITILE);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void launch(Context context, String url) {
        if (null == context) {
            return;
        }
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra(KEY_URL, url);
        context.startActivity(intent);
    }
}
