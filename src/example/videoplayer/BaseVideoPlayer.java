package example.videoplayer;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.*;
import example.videoplayer.util.MyVideoPlayer;


import java.util.ArrayList;


/**
 * Created by Wayne on 2015/4/17.
 * This is a basic video widget that can be used directly or extended
 * In this class it will only implement:
 * 1. video IO/Streaming
 * 2. AD/Content preloading 
 * 3. P/L switch calculation
 *
 */
@SuppressWarnings("unused")
public abstract class BaseVideoPlayer extends RelativeLayout{
    protected Context context;
    protected RelativeLayout parentView;
    // widgets
    protected SurfaceView surfaceView;
    protected SurfaceHolder surfaceHolder;
    protected ProgressBar progressBar;
    protected ImageView maskImage;
    // values
    protected int videoWidth;
    protected int videoHeight;
    protected boolean isPause = false;
    protected boolean isLandscape = false;
    protected boolean isFullScreenClick = false;
    protected boolean hasActiveHolder = false;
    protected boolean isLoaded = false;
    protected MediaPlayer currentMediaPlayer = new MediaPlayer();
    private ArrayList<String> path = new ArrayList<String>();
    private MyVideoPlayer.VideoInfo currentVideoInfo;
    // VideoPlayer
    private MyVideoPlayer myVideoPlayer;
    /** listeners **/
    private onCompleteInitializeListener completeListener;
    /** handler **/
    private android.os.Handler mHandler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };
    public BaseVideoPlayer(Context context){
        super(context);
        this.context = context;
        init();
        initSurfaceHolder();
        currentVideoInfo = new MyVideoPlayer.VideoInfo();
    }
    public BaseVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
        initSurfaceHolder();
        currentVideoInfo = new MyVideoPlayer.VideoInfo();
    }

    /** fix UI here **/
    public abstract void fixLandscapeUI();
    public abstract void fixPortraitUI();

    /** do something in here when the video changed **/
    public abstract void onVideoChanged();

    /** do something in here when the video is finished **/
    public abstract void onVideoFinished();

    /** initialize the view  **/
    protected abstract void init();

    @SuppressWarnings("deprecation")
    public void setDisplay(final ArrayList<MyVideoPlayer.VideoInfo> path){
        try {
//            myVideoPlayer.currentPlayer.setDisplay(null);
            if(isVideoPlaying()){
                myVideoPlayer.stop();
                currentMediaPlayer.stop();
                currentMediaPlayer.reset();
                currentMediaPlayer.release();
                myVideoPlayer.releaseAll();
            }
        }catch (Exception e){e.printStackTrace();}
        isLoaded = false;
        playVideos(path);
    }

    private void initSurfaceHolder(){
        this.surfaceHolder = this.surfaceView.getHolder();
        this.surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        final boolean isCompleted = false;
        boolean isPrepared = false;
        BaseVideoPlayer.this.surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated( final SurfaceHolder surfaceHolder) {
                synchronized(this) {
                    hasActiveHolder = true;
                    ((Object)this).notifyAll();
                }
                if(completeListener != null){
                    completeListener.onComplete(surfaceHolder);
                }
            }

            @Override
            public void surfaceChanged(final SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                try {
                    if (currentMediaPlayer != null) {
//                        myVideoPlayer.releaseAll();
                        synchronized(this) {
                            hasActiveHolder = false;
                            ((Object)this).notifyAll();
                        }
                    }
                }catch (Exception e) {e.printStackTrace();}

            }
        });

        new OrientationEventListener(context){

            @Override
            public void onOrientationChanged(int rotation) {
                // TODO Auto-generated method stub
                if(isLoaded){
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
                }else {
                    ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    fixPortraitUI();
                }

            }
        }.enable();

    }

    public void playVideos(final ArrayList<MyVideoPlayer.VideoInfo> path){
        myVideoPlayer = new MyVideoPlayer(path);

        myVideoPlayer.loadFirstVideo(surfaceHolder, new MyVideoPlayer.onPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                isLoaded = true;
                synchronized(this) {
                    hasActiveHolder = true;
                    ((Object)this).notify();
                }
                currentVideoInfo = path.get(0);
                currentMediaPlayer = mediaPlayer;
                getCurrentVideoDuration();
                onVideoChanged();
                fixPLViews();
                try {
                    progressBar.setVisibility(GONE);
                    if (null != maskImage) {
                        maskImage.setVisibility(GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.v("BaseVideo", ">>>start! time:" + System.currentTimeMillis());
                Log.v("BaseVideo", ">>>video duration:" + myVideoPlayer.getCurrentVideoDuration());

            }
        }, new MyVideoPlayer.onVideoFinishListener() {
            @Override
            public void onFinish(MediaPlayer mediaPlayer) {
                progressBar.setVisibility(VISIBLE);
                if (null != maskImage) {
                    maskImage.setVisibility(VISIBLE);
                }
                mediaPlayer.setDisplay(null);
                mediaPlayer.stop();
                mediaPlayer.release();
                isLoaded = false;
                synchronized(this) {
                    hasActiveHolder = true;
                    ((Object)this).notify();
                }
                fixPLViews();
                myVideoPlayer.loadNextVideo(surfaceHolder, new MyVideoPlayer.onVideoFinishListener() {
                    @Override
                    public void onFinish(MediaPlayer mediaPlayer) {
                        onVideoFinished();
                        isLoaded = false;
                        synchronized(this) {
                            hasActiveHolder = true;
                            ((Object)this).notify();
                        }
                        fixPLViews();
                    }
                }, new MyVideoPlayer.onPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        progressBar.setVisibility(GONE);
                        if (null != maskImage) {
                            maskImage.setVisibility(GONE);
                        }
                        try {
//                            currentMediaPlayer.reset();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        myVideoPlayer.currentPlayer = mediaPlayer;
                        mediaPlayer.setDisplay(surfaceHolder);
                        mediaPlayer.start();
                        isLoaded = true;
                        synchronized(this) {
                            hasActiveHolder = true;
                            ((Object)this).notify();
                        }
                        currentVideoInfo = path.get(1);
                        currentMediaPlayer = mediaPlayer;
                        onVideoChanged();
                        fixPLViews();
                        Log.v("BaseVideo", ">>>video duration:" + myVideoPlayer.getCurrentVideoDuration());
                    }
                });

            }
        });
        myVideoPlayer.getCurrentPlayer().setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                if (i == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
                    Toast.makeText(context, "cannot play the video, please try again", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(context, String.valueOf(i), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    /** check is playing **/
    public boolean isVideoPlaying(){
        boolean isPlaying = false;
        try {
            isPlaying = currentMediaPlayer.isPlaying();
        }catch (Exception e){
//            e.printStackTrace();
        }
        return isPlaying;
    }

    /** get the information of the current video **/
    public MyVideoPlayer.VideoInfo getVideoInfo(){
        return currentVideoInfo;
    }

    /** this is a method for full screen button **/
    public void setFullScreenSwitcher(){
        if(isLandscape){
            ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            isFullScreenClick = true;
        }else {
            ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            isFullScreenClick = true;
        }
    }

    /** media player function widgets **/
    public void start() {
        try {
            this.currentMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        try {
            this.currentMediaPlayer.pause();
            Log.v("BaseVideo", ">>>pause! time:" + System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            this.currentMediaPlayer.stop();
            Log.v("BaseVideo", ">>>stop! time:" + System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void release() {
        try {
            currentMediaPlayer.release();
            Log.v("BaseVideo", ">>>release! time:" + System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void seekTo(int position) {
        try {
            this.currentMediaPlayer.seekTo(position);
            Log.v("BaseVideo", ">>>seek to:" + position + " time:" + System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getCurrentVideoPosition() {
        int position = 0;
        try {
            if(isVideoPlaying()){
                position = this.currentMediaPlayer.getCurrentPosition();
            }
            Log.v("BaseVideo", ">>>current position: " + position + " time:" + System.currentTimeMillis());
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return position;
    }

    public int getCurrentVideoDuration() {
        int duration = 0;
        try {
//            duration = myVideoPlayer.getCurrentVideoDuration();
            duration = currentMediaPlayer.getDuration();
        } catch (Exception e) {

            e.printStackTrace();
        }
        return duration;
    }

    /** fix screen size **/
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
        LinearLayout.LayoutParams parentParam = (LinearLayout.LayoutParams) this.parentView.getLayoutParams();
        if(isLoaded){
            try {
                this.videoWidth = this.myVideoPlayer.getVideoWidth();
                this.videoHeight = this.myVideoPlayer.getVideoHeight();
            }catch (Exception e) {e.printStackTrace();}
            if(screenH > screenW){  // portrait
                try {
                    /** video view **/
                    if(this.videoWidth !=0){
                        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        params.height = screenW * this.videoHeight / this.videoWidth;
                        this.surfaceView.setLayoutParams(params);
                    }
                    /** back to normal screen **/
                    ((Activity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    ((Activity)context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);


                }catch (Exception e){e.printStackTrace();}
                fixPortraitUI();

            }else if(screenH < screenW){  // landscape
                try{
                    /** video view **/
                    if(this.videoHeight != 0){
                        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                        params.width = screenH * this.videoWidth / this.videoHeight;
                        params.alignWithParent = true;
                        this.surfaceView.setLayoutParams(params);
                    }
                    /** full screen with correct video definition **/
                    ((Activity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                    ((Activity)context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


                }catch (Exception e){e.printStackTrace();}
                fixLandscapeUI();
            }
            try {
                this.surfaceHolder.setFixedSize(this.videoWidth, this.videoHeight);
            }catch (Exception e) {e.printStackTrace();}
        }


    }

    public interface onCompleteInitializeListener{
        public void onComplete(SurfaceHolder surfaceHolder);
    }

    public void setOnCompleteIntializeListener(onCompleteInitializeListener listener){
        this.completeListener = listener;
    }

    private void sendMessage(int what, Object obj){
        Message msg = new Message();
        msg.what = what;
        msg.obj = obj;
        mHandler.sendMessage(msg);
    }
}
