package com.ttdevs.jcvideo;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Toast;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class PlayerActivity extends AppCompatActivity {
    private static final String KEY_VIDEO_TITLE = "title";
    private static final String KEY_VIDEO_URL = "video_url";
    private static final String KEY_THUMB_URL = "thumb_url";

    private SensorManager mSensorManager;
    private JCVideoPlayer.JCAutoFullscreenListener mSensorListener;
    private JCVideoPlayerStandard jcPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

        jcPlayer = (JCVideoPlayerStandard) findViewById(R.id.jcPlay);

        Intent intent = getIntent();
        String videoUrl = intent.getStringExtra(KEY_VIDEO_URL);
        if (TextUtils.isEmpty(videoUrl)) {
            Toast.makeText(this, "Video url is null!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        String title = intent.getStringExtra(KEY_VIDEO_TITLE);

        jcPlayer.setUp(videoUrl, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, title);
        jcPlayer.startPlayLogic();

        // String thumbUrl = intent.getStringExtra(KEY_THUMB_URL);
        // jcPlayer.thumbImageView.setImageURI(Uri.parse(thumbUrl));

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorListener = new JCVideoPlayer.JCAutoFullscreenListener();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mSensorListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mSensorManager.unregisterListener(mSensorListener);

        JCVideoPlayer.releaseAllVideos();
    }

    /**
     * 播放视频
     *
     * @param context       上下文
     * @param videoUrl      视频地址
     * @param thumbImageUrl 缩略图地址
     */
    public static void play(Context context, String title, String videoUrl, String thumbImageUrl) {
        if (null == context) {
            throw new RuntimeException("Context is null!");
        }
        if (TextUtils.isEmpty(videoUrl)) {
            Toast.makeText(context, "Video url is null!", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra(KEY_VIDEO_URL, videoUrl);
        if (!TextUtils.isEmpty(title)) {
            intent.putExtra(KEY_VIDEO_TITLE, title);
        }
        if (!TextUtils.isEmpty(thumbImageUrl)) {
            intent.putExtra(KEY_THUMB_URL, thumbImageUrl);
        }
        context.startActivity(intent);
    }

    /**
     * 播放测试视频
     *
     * @param context
     */
    public static void playTest(Context context) {
        String title = "测试视频";
        // String videoUrl = "http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4";
        // String videoUrl = "http://video.boohee.cn/chaomo/female/6%E5%8F%A5%E8%AF%9D.mp4";
        String videoUrl = "http://media.fitapp.cn/fit/actmedia/6214206.mp4";
        String thumbImageUrl = "http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640";
        play(context, title, videoUrl, thumbImageUrl);
    }
}
