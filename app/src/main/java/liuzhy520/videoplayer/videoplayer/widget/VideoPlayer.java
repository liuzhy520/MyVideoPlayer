package liuzhy520.videoplayer.videoplayer.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import liuzhy520.videoplayer.R;

/**
 * Created by Wayne on 2015/4/17.
 */
public class VideoPlayer extends BaseVideoPlayer {

    public VideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void fixLandscapeUI() {
        Toast.makeText(context, "Landscapte", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fixPortraitUI() {
//        Toast.makeText(context, "Portrait", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onVideoChanged() {
        Toast.makeText(context, "changed & duration:" + getCurrentVideoDuration(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onVideoFinished() {
        Toast.makeText(context, "finished", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void init(){
        parentView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.base_video_view, this);
        surfaceView = (SurfaceView) parentView.findViewById(R.id.video_surface);
        progressBar = (ProgressBar) parentView.findViewById(R.id.base_video_progressBar);
        surfaceView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isPause){
                    pause();
                    isPause = true;
                }else{
                    start();
                    isPause = false;
                }
            }
        });
        Log.v("BaseVideo", ">>>created! time:" + System.currentTimeMillis());
        this.setOnBufferingUpdateListener(new onBufferingUpdateListener() {
            @Override
            public void onUpdate(int i) {
                Log.e("buffer", String.valueOf(i));
            }
        });
    }



}
