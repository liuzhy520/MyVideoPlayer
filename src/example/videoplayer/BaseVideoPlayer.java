package example.videoplayer;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by Wayne on 2015/4/17.
 * This is a basic video widget that can be used directly or extended
 * In this class it will only implement:
 * 1. video IO/Streaming
 * 2. AD/Content preloading 
 * 3. P/L switch calculation
 *  
 */
public abstract class BaseVideoPlayer extends RelativeLayout{
    protected Context context;
    protected RelativeLayout parentView;
    // widgets
    protected SurfaceView surfaceView;
    protected SurfaceHolder surfaceHolder;
    protected ProgressBar progressBar;
    // values
    private int videoWidth;
    private int videoHeight;
    private boolean isPause = false;
    private boolean isLandscape = false;
    private boolean isFullScreenClick = false;
    /**  **/
    public BaseVideoPlayer(Context context){
        super(context);
        this.context = context;
        init();
    }
    public BaseVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    /** fix UI here **/
    protected abstract void fixUI();

    protected void init(){
    	parentView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.base_video_view, this);
        surfaceView = (SurfaceView) parentView.findViewById(R.id.video_surface);
        progressBar = (ProgressBar) parentView.findViewById(R.id.progressBar);
    }

    protected void setDisplay(){
        this.surfaceHolder = this.surfaceView.getHolder();
        this.surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });
        new OrientationEventListener(context){

            @Override
            public void onOrientationChanged(int rotation) {
                // TODO Auto-generated method stub

                if(isLandscape && !isFullScreenClick){
                    if (((rotation >= 0) && (rotation <= 30)) || (rotation >= 330)) {
                        ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        isLandscape = false;
                    }
                }else if(!isFullScreenClick && !isLandscape){
                    if (((rotation >= 230) && (rotation <= 310))) {
                        ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        fixPLViews();
    }
    public void fixPLViews(){
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenH = dm.heightPixels;
        int screenW = dm.widthPixels;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.surfaceView.getLayoutParams();
        if(screenH > screenW){
            try {
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = screenW * this.videoHeight / this.videoWidth;
                this.surfaceView.setLayoutParams(params);
                ((Activity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                ((Activity)context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            }catch (Exception e){e.printStackTrace();}

        }else if(screenH < screenW){
            try{
                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                params.width = screenH * this.videoWidth / this.videoHeight;
                this.surfaceView.setLayoutParams(params);
                ((Activity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                ((Activity)context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }catch (Exception e){e.printStackTrace();}

        }
        try {
            this.surfaceHolder.setFixedSize(this.videoWidth, this.videoHeight);
        }catch (Exception e) {e.printStackTrace();}

    }

}
