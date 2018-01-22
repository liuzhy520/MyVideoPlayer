package liuzhy520.videoplayer;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import liuzhy520.videoplayer.BaseVideoPlayer.onCompleteInitializeListener;
import liuzhy520.videoplayer.old.util.MyVideoPlayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    private boolean islocked = false;
    private boolean isStart = false;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.use_widget_activity);
        init();
    }

    private void init(){
        VideoPlayer = (VideoPlayer) findViewById(R.id.base_video_player);
        TextView text = (TextView) findViewById(R.id.text);
        TextView text1 = (TextView) findViewById(R.id.text1);
        TextView text3 = (TextView) findViewById(R.id.text3);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                VideoPlayer.setFullScreenSwitcher();
                ArrayList<MyVideoPlayer.VideoInfo> path = new ArrayList<MyVideoPlayer.VideoInfo>();
                MyVideoPlayer.VideoInfo video = new MyVideoPlayer.VideoInfo();
//                video.sourceUrl = "http://v.ysbang.cn/data/test/test0.mp4";
                video.sourceUrl = "http://v.ysbang.cn/data/video/ad/normal.mp4";
                video.isAd = true;

//                path.add(video);
                video = new MyVideoPlayer.VideoInfo();
//                video.sourceUrl = "http://v.ysbang.cn/data/test/test0.mp4";
//                video.sourceUrl = "http://192.168.0.9/data/ts/index_1500.m3u8";
//              video.sourceUrl = "http://192.168.0.9/data/ts/index_1500.m3u8";
//                video.sourceUrl = "http://192.168.0.9/data/ts/2015rkb05/index.m3u8";
//                video.sourceUrl = "http://192.168.0.9/data/ts/2015rkb05/high.m3u8";
//                video.sourceUrl = "http://192.168.0.9/data/video/2015/rkb/2015rkb01/index.m3u8";
//                video.sourceUrl = "http://192.168.0.11//data/video/2013/ysfg/jichuban/2013jcysfg01/index.m3u8";
                video.sourceUrl = "http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8";


                video.isAd = false;
                path.add(video);
                VideoPlayer.stop();
                VideoPlayer.release();
                VideoPlayer.setDisplay(path);
                sendMSG(0, null);
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            while(isStart){
                                sendMSG(1, null);
                                sleep(1000);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(islocked){
//                    VideoPlayer.setScreenLock(false);
//                    islocked = false;
//                }else {
//                    VideoPlayer.setScreenLock(true);
//                    islocked = true;
//                }
                VideoPlayer.setFullScreenSwitcher();
                Toast.makeText(UseWidgetActivity.this, String.valueOf(VideoPlayer.getVideoInfo().isAd), Toast.LENGTH_SHORT).show();
                Log.e("duration", String.valueOf(VideoPlayer.getCurrentVideoDuration()));
            }
        });

        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = VideoPlayer.getCurrentVideoPosition();
                VideoPlayer.seekTo(current + 300000);
            }
        });

    }



    public void onStart(){
        super.onStart();
    }

    public void onResume(){
        super.onResume();
//        setContentView(R.layout.use_widget_activity);
        VideoPlayer.setOnCompleteInitializeListener(new onCompleteInitializeListener() {

            @Override
            public void onComplete(SurfaceHolder surfaceHolder) {
                // TODO Auto-generated method stub
                Toast.makeText(UseWidgetActivity.this, "complete", Toast.LENGTH_SHORT).show();

            }
        });

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
    public void sendMSG(int m,Object object) {
        Message msg = new Message();
        msg.what = m;
        msg.obj = object;
        UIHandle.sendMessage(msg);
    }
    private SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
    private Date date = new Date();
    private final Handler UIHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    isStart = true;
                    break;
                case 1:
                    int position = VideoPlayer.getCurrentVideoPosition();
                    date.setTime(position * 1000);
                    Log.e("time", String.valueOf(df.format(date)));
                    Log.e("current position", String.valueOf(VideoPlayer.getCurrentVideoPosition()));
                    break;
            }
        }
    };
}
