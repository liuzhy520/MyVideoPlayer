package example.videoplayer;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by Wayne on 2015/4/17.
 * This is a basic video widget that can be used directly or extended
 */
public abstract class BaseVideoPlayer extends RelativeLayout{
    protected Context context;
    protected RelativeLayout parentView;
    // widgets
    protected MediaPlayer mediaPlayer;
    protected SurfaceView surfaceView;
    protected SurfaceHolder surfaceHolder;
    protected ProgressBar progressBar;
    protected ImageView playBtn;
    protected ImageView fullScreenBtn; 
    // values
    private int videoWidth;
    private int videoHeight;
    
    /**  **/
    public BaseVideoPlayer(Context context){
        super(context);
        this.context = context;
    }
    public BaseVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    /** fix UI here **/
    protected abstract void fixUI();

    protected void init(){
    	parentView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.base_video_view, this);
    }

    public void prepare(String path){
    	try {
    		this.mediaPlayer.setDataSource(path);
			this.mediaPlayer.prepare();
			
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("VideoPlayer", ">>>illegal error");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("VideoPlayer", ">>>source io error");
		}
    	
    }
    
    public void play(){
    	
    }

    public void pause(){
    	
    }
    
    public void seekTo(){
    	
    }
    
    public void stop(){
    	
    }
}
