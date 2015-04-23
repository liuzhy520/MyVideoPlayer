package example.videoplayer.util;

import android.annotation.TargetApi;
import android.media.MediaPlayer;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Wayne on 2015/4/20. This is a core function class of the media
 * player. It will implement 1. basic video functions 2. multiple videos playing
 * with preloading
 */
public class MyVideoPlayer {
	/** players **/
	private MediaPlayer 	currentPlayer, // current Player at the front end
							nextPlayer, // next Player while preparing
							cachePlayer; // the Player in the cache waiting to play after it is
	// prepared

	/** values **/
	private boolean isLoaded = false;
	private Loaded syncFlag = new Loaded();
	/** store videoPlayer list **/
	ArrayList<MediaPlayer> cachePlayerList;

	/** to store all incoming urls **/
	private ArrayList<String> urlList;

	/** current video index **/
	private int currentVideoIndex;

	/** interfaces **/
	private onVideoFinishListener finishListener;
	private onPreparedListener preparedListener;
	private onLoadNextSuccessListener loadNextSuccessListener;

	public MyVideoPlayer(ArrayList<String> paths) {
		/** Initialize values and objects **/
		cachePlayerList = new ArrayList<MediaPlayer>();
		currentPlayer = new MediaPlayer();
		currentPlayer.setScreenOnWhilePlaying(true);
		urlList = new ArrayList<String>();
		currentVideoIndex = 0;
		setVideos(paths);
	}

	public void setVideos(ArrayList<String> paths) {
		try {
			this.urlList = paths;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadFirstVideo(final SurfaceHolder surfaceHolder,final onPreparedListener listener) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Log.v("BaseVideo",">>>start prepare:" + System.currentTimeMillis());

					MyVideoPlayer.this.currentPlayer.setDataSource(urlList.get(0));
					MyVideoPlayer.this.currentPlayer.setDisplay(surfaceHolder);

					MyVideoPlayer.this.currentPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
						@Override
						public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

						}
					});

					MyVideoPlayer.this.currentPlayer.prepareAsync();
					cachePlayer = currentPlayer;
				} catch (IOException eio){
					eio.printStackTrace();
					Log.e("BaseVideo", ">>>video source error");
				}

				catch (Exception e) {
					e.printStackTrace();
					if(currentPlayer != null){
						MyVideoPlayer.this.currentPlayer.release();
					}

				}
			}
		}).start();
		MyVideoPlayer.this.currentPlayer
				.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mediaPlayer) {
						onVideoComplete(mediaPlayer, surfaceHolder);
					}
				});

		MyVideoPlayer.this.currentPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mediaPlayer) {
				listener.onPrepared(mediaPlayer);
			}
		});

	}

	public void loadMorePlayerThread(final SurfaceHolder surfaceHolder) {
		new Thread(new Runnable() {
			@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public void run() {
				try {
					for (int i = 1; i < urlList.size(); i++) {

						MyVideoPlayer.this.nextPlayer = new MediaPlayer();
						MyVideoPlayer.this.nextPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(
									MediaPlayer mediaPlayer) {
								onVideoComplete(mediaPlayer,surfaceHolder);
							}
						});


						MyVideoPlayer.this.nextPlayer.setDataSource(urlList.get(i));
						cachePlayerList.add(nextPlayer);
						try {
							cachePlayerList.get(i-1).prepareAsync();
							cachePlayerList.get(i-1).setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
								@Override
								public void onPrepared(MediaPlayer mediaPlayer) {
									cachePlayer.setNextMediaPlayer(mediaPlayer);
									cachePlayer = mediaPlayer;
									Log.e("prepared", String.valueOf(currentVideoIndex));
									isLoaded = true;
//									synchronized (syncFlag){
//										syncFlag.isLoaded = true;
//										syncFlag.notifyAll();
//									}
								}
							});

						}catch (Exception e) {e.printStackTrace();}


					}
				} catch (Exception e) {
					e.printStackTrace();
					if(nextPlayer != null){
						MyVideoPlayer.this.nextPlayer.release();
					}

				}
			}
		}).start();
	}

	private void onVideoComplete(MediaPlayer mediaPlayer, final SurfaceHolder surfaceHolder) {
		try {
			mediaPlayer.setDisplay(null);
			currentPlayer.setDisplay(null);
//			mediaPlayer.release();
			Log.e("listener complete top", String.valueOf(currentVideoIndex));
			if (currentVideoIndex < urlList.size() - 1) {
				currentPlayer = cachePlayerList.get(currentVideoIndex);
//				synchronized (syncFlag) {
//					while (!syncFlag.isLoaded){
////						try {
//						syncFlag.wait(1000);
////						} catch (Exception e) {e.printStackTrace();}
//					}
//				}
				currentPlayer.setDisplay(surfaceHolder);

				Log.e("listener complete", String.valueOf(currentVideoIndex));

			} else {
				if (finishListener != null) {
					finishListener.onFinish();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		currentVideoIndex++;
		isLoaded = false;
		syncFlag.isLoaded = false;
	}

	public MediaPlayer getCurrentPlayer() {
		return currentPlayer;
	}

	public int getVideoWidth() {
		try {
			return this.currentPlayer.getVideoWidth();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int getVideoHeight() {
		try {
			return this.currentPlayer.getVideoHeight();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public interface onPreparedListener {
		public void onPrepared(MediaPlayer mediaPlayer);
	}

	public void setOnPreparedListener(onPreparedListener listener) {
		this.preparedListener = listener;
	}

	public interface onVideoFinishListener {
		public void onFinish();
	}

	public void setOnVideoFinishListener(onVideoFinishListener listener) {
		this.finishListener = listener;
	}

	public interface onLoadNextSuccessListener {
		public void onLoaded();
	}

	public void setOnLoadNextSuccessListener(onLoadNextSuccessListener listener) {
		this.loadNextSuccessListener = listener;
	}

	public void start() {
		try {
			currentPlayer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void pause() {
		try {
			currentPlayer.pause();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			if(currentPlayer.isPlaying()){
				currentPlayer.stop();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void releaseAll() {
		try {
			if (currentPlayer != null) {
//				if (currentPlayer.isPlaying()) {
//					currentPlayer.stop();
//				}
				currentPlayer.release();
			}
			if (nextPlayer != null) {
				nextPlayer.release();
			}
			if (cachePlayer != null) {
				cachePlayer.release();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void seekTo(int position) {
		try {
			currentPlayer.seekTo(position);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getCurrentVideoPosition() {
		int position = 0;
		try {
			position = currentPlayer.getCurrentPosition();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return position;
	}

	public int getCurrentVideoDuration() {
		int duration = 0;
		try {
			duration = currentPlayer.getDuration();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return duration;
	}

	private class Loaded{
		public boolean isLoaded = false;
	}

}
