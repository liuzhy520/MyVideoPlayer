package example.videoplayer.util;

import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.ArrayList;

/**
 * Created by Wayne on 2015/4/20.
 */
public class VideoPlayer {
    /** players **/
    private MediaPlayer     currentPlayer,  // current Player at the front end
                             nextPlayer,     // next Player while preparing
                             cachePlayer;    // the Player in the cache waiting to play after it is prepared

    /** values **/
    /** store videoPlayer list **/
    ArrayList<MediaPlayer> cachePlayerList;

    /** to store all incoming urls **/
    private ArrayList<String> urlList;

    /** current video index **/
    private int currentVideoIndex;

    /** interfaces **/
    private onVideoFinishListener finishListener;
    private onPreparedListener preparedListener;


    public VideoPlayer(ArrayList<String> paths){
        /** Initialize values and objects **/
        cachePlayerList = new ArrayList<MediaPlayer>();
        currentPlayer = new MediaPlayer();
        urlList = new ArrayList<String>();
        currentVideoIndex = 0;
        setVideos(paths);
    }

    public void setVideos(ArrayList<String> paths){
        try{
            this.urlList = paths;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void playVideos(SurfaceHolder surfaceHolder){
            loadFirstVideo(surfaceHolder);
            loadMorePlayerThread(surfaceHolder);
    }

    private void loadFirstVideo(final SurfaceHolder surfaceHolder){
        try {
            this.currentPlayer.setDataSource(urlList.get(0));
            this.currentPlayer.setDisplay(surfaceHolder);
            this.currentPlayer.prepare();
            this.currentPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

                }
            });
            this.currentPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    VideoPlayer.this.currentPlayer.start();
                    if(preparedListener != null){
                        preparedListener.onPrepared(mediaPlayer);
                        Log.v("MyPlayer", ">>>start playing videos");

                    }
                }
            });
            this.currentPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    onVideoComplete(mediaPlayer, surfaceHolder);
                }
            });
        }catch (Exception e) { e.printStackTrace();}
    }

    private void loadMorePlayerThread(SurfaceHolder surfaceHolder){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    for(int i = 1; i < urlList.size(); i++){

                    }
                }catch (Exception e) { e.printStackTrace(); }
            }
        });
    }

    private void onVideoComplete(MediaPlayer mediaPlayer, SurfaceHolder surfaceHolder){
        try{
            mediaPlayer.setDisplay(null);
            if(currentVideoIndex <= cachePlayerList.size()){
                currentPlayer = cachePlayerList.get(currentVideoIndex);
                currentPlayer.setDisplay(surfaceHolder);
                currentVideoIndex ++;
            }else {
                if(finishListener != null){
                    finishListener.onFinish();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public interface onPreparedListener {
        public void onPrepared(MediaPlayer mediaPlayer);
    }

    public void setOnPreparedListener(onPreparedListener listener) {
        this.preparedListener = listener;
    }

    public interface onVideoFinishListener{
        public void onFinish();
    }

    public void setOnVideoFinishListener(onVideoFinishListener listener){
        this.finishListener = listener;
    }

    public void start(){
        try{
            currentPlayer.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void pause(){
        try{
            currentPlayer.pause();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stop(){
        try{
            currentPlayer.stop();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void releaseAll(){
        try{
            if(currentPlayer != null){
                if(currentPlayer.isPlaying()){
                    currentPlayer.stop();
                }
                currentPlayer.release();
            }
            if(nextPlayer != null){
                nextPlayer.release();
            }
            if(cachePlayer != null){
                cachePlayer.release();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void seekTo(int position){
        try{
            currentPlayer.seekTo(position);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int getCurrentVideoPosition(){
        int position = 0;
        try{
            position = currentPlayer.getCurrentPosition();
        }catch (Exception e){
            e.printStackTrace();
        }
        return position;
    }

    public int getCurrentVideoDuration(){
        int duration = 0;
        try{
            duration = currentPlayer.getDuration();
        }catch (Exception e){
            e.printStackTrace();
        }
        return duration;
    }
}
