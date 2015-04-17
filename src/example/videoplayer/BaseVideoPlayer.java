package example.videoplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
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
    protected RelativeLayout parent;
    // widgets
    protected MediaPlayer mediaPlayer;
    protected SurfaceView surfaceView;
    protected SurfaceHolder surfaceHolder;
    protected ProgressBar progressBar;
    // values
    private int videoWidth;
    private int videoHeight;

    public BaseVideoPlayer(Context context){
        super(context);

    }
    public BaseVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /** fix UI here **/
    protected abstract void fixUI();

    protected void init(){
        parent = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.base_video_view, this);
    }



}
