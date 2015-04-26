package example.videoplayer;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import example.videoplayer.BaseVideoPlayer.onCompleteInitializeListener;
import example.videoplayer.util.MyVideoPlayer;

import java.util.ArrayList;

/**
 * Created by Wayne on 2015/4/20.
 * video stream list:
 * http://live.3gv.ifeng.com/live/hongkong.m3u8
 * http://www.nasa.gov/multimedia/nasatv/NTV-Public-IPS.m3u8
 * http://46.61.226.21/hls/CH_PLAYBOY/variant.m3u8?version=2
 * 
 */
public class UseWidgetActivity extends Activity {
    private VideoPlayer VideoPlayer;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.use_widget_activity);
        init();
    }

    private void init(){
        VideoPlayer = (VideoPlayer) findViewById(R.id.base_video_player);
        TextView text = (TextView) findViewById(R.id.text);
        TextView text1 = (TextView) findViewById(R.id.text1);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                VideoPlayer.setFullScreenSwitcher();
                ArrayList<MyVideoPlayer.VideoInfo> path = new ArrayList<MyVideoPlayer.VideoInfo>();
                MyVideoPlayer.VideoInfo video = new MyVideoPlayer.VideoInfo();
                video.sourceUrl = "http://v.ysbang.cn/data/test/test0.mp4";
                video.isAd = true;

                path.add(video);
                video = new MyVideoPlayer.VideoInfo();
//                video.sourceUrl = "http://v.ysbang.cn/data/test/test0.mp4";
//                video.sourceUrl = "http://192.168.0.9/data/ts/index_1500.m3u8";
//              video.sourceUrl = "http://192.168.0.9/data/ts/index_1500.m3u8";
//                video.sourceUrl = "http://v.ysbang.cn//data/video/2015/rkb/2015rkb01.mp4";
                video.sourceUrl = "http://legendwing.com/videos/video4.mp4";

                video.isAd = false;
                path.add(video);
                VideoPlayer.stop();
                VideoPlayer.release();
                VideoPlayer.setDisplay(path);
            }
        });
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UseWidgetActivity.this, String.valueOf(VideoPlayer.isVideoPlaying()), Toast.LENGTH_SHORT).show();
                Log.e("duration", String.valueOf(VideoPlayer.getCurrentVideoDuration()));
            }
        });


    }

    public void onStart(){
        super.onStart();
    }

    public void onResume(){
        super.onResume();
//        setContentView(R.layout.use_widget_activity);
        VideoPlayer.setOnCompleteIntializeListener(new onCompleteInitializeListener(){

			@Override
			public void onComplete(SurfaceHolder surfaceHolder) {
				// TODO Auto-generated method stub
				Toast.makeText(UseWidgetActivity.this, "complete", Toast.LENGTH_SHORT).show();
				
			}});
    }

    public void onPause(){
        super.onPause();
        try {
            VideoPlayer.pause();
        }catch (Exception e) {e.printStackTrace();}
    }

    public void onStop(){
        super.onStop();
        try {
            VideoPlayer.stop();
        }catch (Exception e) {e.printStackTrace();}
    }

    public void onDestroy(){
        super.onDestroy();
        try {
            VideoPlayer.release();
        }catch (Exception e) {e.printStackTrace();}
    }

}
