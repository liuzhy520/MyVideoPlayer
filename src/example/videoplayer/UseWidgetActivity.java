package example.videoplayer;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
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
        ArrayList<String> path = new ArrayList<String>();
        path.add("http://v.ysbang.cn/data/test/test0.mp4");
//        path.add("http://v.ysbang.cn//data/video/2015/rkb/2015rkb01.mp4");
//        path.add("http://v.ysbang.cn/data/test/test0.mp4");
        path.add("http://live.3gv.ifeng.com/live/hongkong.m3u8");
        VideoPlayer.setDisplay(path);
        TextView text = (TextView) findViewById(R.id.text);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoPlayer.setFullScreenSwitcher();
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
