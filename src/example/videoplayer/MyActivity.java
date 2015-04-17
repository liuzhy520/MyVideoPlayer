package example.videoplayer;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;

public class MyActivity extends Activity implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, SurfaceHolder.Callback{
    /**
     * this is a video demo to play stream video
     */
    private MediaPlayer mediaPlayer;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private int videoWidth;
    private int videoHeight;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.surfaceView = (SurfaceView) this.findViewById(R.id.surface);
        this.surfaceHolder = this.surfaceView.getHolder();
        this.surfaceHolder.addCallback(this);
        this.surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.progressBar.setVisibility(View.VISIBLE);
        Log.v("mplayer", ">>>create ok.");

    }
    protected void onStop(){
        super.onStop();
        this.mediaPlayer.stop();
    }
    protected void onDestroy(){
        super.onDestroy();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void playVideo() throws IllegalArgumentException,
            IllegalStateException, IOException {
        this.mediaPlayer = new MediaPlayer();
        String pathYsb = "http://v.ysbang.cn//data/video/2015/rkb/2015rkb01.mp4";
        String path3 = "http://live.3gv.ifeng.com/live/hongkong.m3u8";
        this.mediaPlayer.setDataSource(path3);
        this.mediaPlayer.setDisplay(this.surfaceHolder);
        this.mediaPlayer.prepare();
        this.mediaPlayer.start();
        this.mediaPlayer.setOnBufferingUpdateListener(this);
        this.mediaPlayer.setOnPreparedListener(this);
        //this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Log.v("mplayer", ">>>play video");
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // TODO Auto-generated method stub

        try {
            this.playVideo();
        } catch (Exception e) {
            Log.e("mplayer", ">>>error", e);
        }
        Log.v("mplayer", ">>>surface created");
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        this.videoWidth = this.mediaPlayer.getVideoWidth();
        this.videoHeight = this.mediaPlayer.getVideoHeight();
        this.progressBar.setVisibility(View.GONE);
        if (this.videoHeight != 0 && this.videoWidth != 0) {
            this.surfaceHolder.setFixedSize(this.videoWidth, this.videoHeight);
            this.mediaPlayer.start();
        }
        fixVideo();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Toast.makeText(this,"start!", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        fixVideo();
    }
    private void fixVideo(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenH = dm.heightPixels;
        int screenW = dm.widthPixels;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.surfaceView.getLayoutParams();
        if(screenH > screenW){
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = screenW * this.videoHeight / this.videoWidth;
            this.surfaceView.setLayoutParams(params);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }else if(screenH < screenW){
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            params.width = screenH * this.videoWidth / this.videoHeight;
            this.surfaceView.setLayoutParams(params);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        this.surfaceHolder.setFixedSize(this.videoWidth, this.videoHeight);
    }
}
