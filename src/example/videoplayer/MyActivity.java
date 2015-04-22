package example.videoplayer;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
    private boolean isPause = false;
    private boolean isLandscape = false;
    private boolean isFullScreenClick = false;
    private int currentPosition = 0;
    private LinearLayout controller;
    @SuppressWarnings("deprecation")
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
        controller = (LinearLayout) findViewById(R.id.base_video_control_layout);
        Log.v("mplayer", ">>>create ok.");
        this.surfaceView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                Intent intent = new Intent(MyActivity.this, UseWidgetActivity.class);
                startActivity(intent);

//				if(!isPause){
//					MyActivity.this.mediaPlayer.pause();
//					isPause = true;
//				}else{
//					MyActivity.this.mediaPlayer.start();
//					isPause = false;
//				}
				
			}
		});
        this.controller.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isLandscape){
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);					
					isFullScreenClick = true;
				}else {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);					
					isFullScreenClick = true;
				}

			}
		});
        new OrientationEventListener(this){

			@Override
			public void onOrientationChanged(int rotation) {
				// TODO Auto-generated method stub
				
					if(isLandscape && !isFullScreenClick){					
						if (((rotation >= 0) && (rotation <= 30)) || (rotation >= 330)) {
							setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
							isLandscape = false;
						}
					}else if(!isFullScreenClick && !isLandscape){
						if (((rotation >= 230) && (rotation <= 310))) {     
							setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
							isLandscape = true;

						}
						
					}

					if(isFullScreenClick){
						if(isLandscape){
							if (((rotation >= 0) && (rotation <= 30)) || (rotation >= 330)) {     // Mark A
								isFullScreenClick = false;
							}
						}else {
							if (((rotation >= 230) && (rotation <= 310))) {     
								isFullScreenClick = false;							
							}
						}
					}


			}
        	
        }.enable();

    }
    @Override
    protected void onResume(){
        super.onResume();
        if(isPause){
            try{
                this.mediaPlayer.seekTo(currentPosition);
                this.mediaPlayer.start();
                fixVideo();
            }catch (Exception e) {e.printStackTrace();}
            isPause = false;
        }
    }
    @Override
    protected void onPause(){
        super.onResume();
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                isPause = true;
                currentPosition = this.mediaPlayer.getCurrentPosition();
            }

        }catch (Exception e) {e.printStackTrace();}

    }
    @Override
    protected void onStop(){
        super.onStop();
        try {
            this.mediaPlayer.stop();
        }catch (Exception e){e.printStackTrace();}

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        try {
            this.mediaPlayer.release();
        }catch (Exception e) {e.printStackTrace();}

    }

    private void playVideo() throws IllegalArgumentException,
            IllegalStateException, IOException {
        this.mediaPlayer = new MediaPlayer();
        String pathYsb = "http://v.ysbang.cn//data/video/2015/rkb/2015rkb01.mp4";
        String YsbAd = "http://v.ysbang.cn/data/test/test0.mp4";
        String ifengZ = "http://live.3gv.ifeng.com/zixun.m3u8";
        String ifeng = "http://live.3gv.ifeng.com/live/zhongwen.m3u8";
        String ifengHK = "http://live.3gv.ifeng.com/live/hongkong.m3u8";
        
        String test = "http://v.ysbang.cn//data/video/2015/rkb/2015rkb01.mp4";
        this.mediaPlayer.setDataSource(ifengHK);
        this.mediaPlayer.setDisplay(this.surfaceHolder);
        this.mediaPlayer.prepareAsync();
        this.mediaPlayer.start();
        this.mediaPlayer.setOnBufferingUpdateListener(this);
        this.mediaPlayer.setOnPreparedListener(this);
        //this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Log.v("mplayer", ">>>play video");
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // TODO Auto-generated method stub
        if(isPause){
            try{
                this.mediaPlayer.seekTo(currentPosition);
                this.mediaPlayer.start();
                fixVideo();
            }catch (Exception e) {e.printStackTrace();}
            isPause = false;
        }else {
            try {
                this.playVideo();
            } catch (Exception e) {
                Log.e("mplayer", ">>>error", e);
            }
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
        String d = String.valueOf(mediaPlayer.getDuration());
        Toast.makeText(this,"start!" + d, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        fixVideo();
    }
    private void fixVideo(){
    	String d = String.valueOf(mediaPlayer.getCurrentPosition());
        Toast.makeText(this,"position" + d, Toast.LENGTH_SHORT).show();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenH = dm.heightPixels;
        int screenW = dm.widthPixels;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.surfaceView.getLayoutParams();
        if(screenH > screenW){
            try {
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = screenW * this.videoHeight / this.videoWidth;
                this.surfaceView.setLayoutParams(params);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            }catch (Exception e){e.printStackTrace();}

        }else if(screenH < screenW){
            try{
                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                params.width = screenH * this.videoWidth / this.videoHeight;
                this.surfaceView.setLayoutParams(params);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }catch (Exception e){e.printStackTrace();}

        }
        this.surfaceHolder.setFixedSize(this.videoWidth, this.videoHeight);
    }
}
