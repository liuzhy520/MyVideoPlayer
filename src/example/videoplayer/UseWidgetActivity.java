package example.videoplayer;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import example.videoplayer.util.MyVideoPlayer;

import java.util.ArrayList;

/**
 * Created by Wayne on 2015/4/20.
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
                video.sourceUrl = "http://v.ysbang.cn//data/video/2015/rkb/2015rkb01.mp4";
                video.isAd = false;
                path.add(video);
//                path.add("http://live.3gv.ifeng.com/zixun.m3u8");
                VideoPlayer.stop();
                VideoPlayer.release();
                VideoPlayer.setDisplay(path);
            }
        });
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UseWidgetActivity.this, String.valueOf(VideoPlayer.getVideoInfo().isAd), Toast.LENGTH_SHORT).show();
                Log.e("duration", String.valueOf(VideoPlayer.getCurrentVideoDuration()));
            }
        });


    }

    public void onStart(){
        super.onStart();
    }

    public void onResume(){
        super.onResume();
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
