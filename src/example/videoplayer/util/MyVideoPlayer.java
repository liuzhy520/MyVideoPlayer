package liuzhy520.videoplayer.util;

import android.annotation.TargetApi;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Handler;

/**
 * Created by Wayne on 2015/4/20. This is a core function class of the media
 * player. It will implement 1. basic video functions 2. multiple videos playing
 * with preloading
 */
public class MyVideoPlayer {
	/** players **/
	public MediaPlayer 		currentPlayer, // current Player at the front end
							nextPlayer, // next Player while preparing
							cachePlayer; // the Player in the cache waiting to play after it is
	// prepared

	/** surface holder **/
	private SurfaceHolder surfaceHolder;
	/** values **/
	private boolean isCompleted = false;
	private boolean isLoaded = false;
	private final static int IS_COMPLETE = 0X00;
	/** store videoPlayer list **/
	ArrayList<MediaPlayer> cachePlayerList;

	/** to store all incoming urls **/
	private ArrayList<VideoInfo> urlList;

	/** current video index **/
	public int currentVideoIndex;

	/** interfaces **/
	public onVideoFinishListener finishListener;
	private onPreparedListener preparedListener;
	private onLoadNextSuccessListener loadNextSuccessListener;

	/** handler **/
	private android.os.Handler mHandler = new android.os.Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case IS_COMPLETE:
					Log.e("message", "sent");
					if(isLoaded && isCompleted){
						playNext(surfaceHolder);
					}
					break;
				default:
					break;
			}
		}
	};

	public MyVideoPlayer(ArrayList<VideoInfo> paths) {
		/** Initialize values and objects **/
		cachePlayerList = new ArrayList<MediaPlayer>();
		currentPlayer = new MediaPlayer();
		
		urlList = new ArrayList<VideoInfo>();
		currentVideoIndex = 0;
		setVideos(paths);
	}

	public void setVideos(ArrayList<VideoInfo> paths) {
		try {
			this.urlList = paths;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadFirstVideo(final SurfaceHolder surfaceHolder,final onPreparedListener listener, final onVideoFinishListener finishListener) {
		this.surfaceHolder = surfaceHolder;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Log.v("BaseVideo",">>>start prepare:" + System.currentTimeMillis());

					MyVideoPlayer.this.currentPlayer.setDataSource(urlList.get(0).sourceUrl);
					MyVideoPlayer.this.currentPlayer.setDisplay(surfaceHolder);
					currentPlayer.setScreenOnWhilePlaying(true);
					MyVideoPlayer.this.currentPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
						@Override
						public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
//								Log.e("buffereing", String.valueOf(i) + "%");
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

		MyVideoPlayer.this.currentPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mediaPlayer) {
				onVideoComplete();
				finishListener.onFinish(mediaPlayer);

			}
		});

		MyVideoPlayer.this.currentPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mediaPlayer) {
				listener.onPrepared(currentPlayer);
			}
		});

	}

	/** using a arraylist to cache players to implement video preloading functionality **/
	public void loadMorePlayerThread(final SurfaceHolder surfaceHolder, final onPreparedListener listener, final onVideoFinishListener finishListener) {
		this.surfaceHolder = surfaceHolder;
		new Thread(new Runnable() {
			@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public void run() {
				try {
					for (int i = 1; i < urlList.size(); i++) {
						MyVideoPlayer.this.nextPlayer = new MediaPlayer();
						MyVideoPlayer.this.nextPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mediaPlayer) {
								finishListener.onFinish(mediaPlayer);
								onVideoComplete();
							}
						});


						MyVideoPlayer.this.nextPlayer.setDataSource(urlList.get(i).sourceUrl);
						
						try {
							MyVideoPlayer.this.nextPlayer.prepareAsync();
							cachePlayerList.add(nextPlayer);
							cachePlayerList.get(i-1).setDisplay(surfaceHolder);
							cachePlayerList.get(i-1).setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
								@Override
								public void onPrepared(MediaPlayer mediaPlayer) {
//									cachePlayer.setNextMediaPlayer(mediaPlayer);
//									cachePlayer = mediaPlayer;
									mediaPlayer.setDisplay(surfaceHolder);
									Log.e("prepared", String.valueOf(currentVideoIndex));
									isLoaded = true;
										try {
//											currentPlayer.reset();
											sendMessage(IS_COMPLETE,null);
											Log.e("VideoIsLoaded", String.valueOf(isLoaded));
											listener.onPrepared(mediaPlayer);
										} catch (Exception e) { e.printStackTrace();}

								}
							});
							cachePlayerList.get(i-1).setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
								@Override
								public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
									Log.e("buffereing", String.valueOf(i) + "%");
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

	/** load next video thread **/
	public void loadNextVideo(final SurfaceHolder surfaceHolder, final onVideoFinishListener onVideoFinishListener, final onPreparedListener onPreparedListener){
		nextPlayer = new MediaPlayer();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Log.v("BaseVideo",">>>start prepare:" + System.currentTimeMillis());
					MyVideoPlayer.this.nextPlayer.setDataSource(urlList.get(1).sourceUrl);
					MyVideoPlayer.this.nextPlayer.setDisplay(surfaceHolder);
					nextPlayer.setScreenOnWhilePlaying(true);
					MyVideoPlayer.this.nextPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
						@Override
						public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
							Log.e("buffereing", String.valueOf(i) + "%");
						}
					});
					MyVideoPlayer.this.nextPlayer.prepareAsync();
				} catch (IOException eio){
					eio.printStackTrace();
					Log.e("BaseVideo", ">>>video source error");
				}

				catch (Exception e) {
					e.printStackTrace();
					if(nextPlayer != null){
						MyVideoPlayer.this.nextPlayer.release();
					}

				}
			}
		}).start();

		MyVideoPlayer.this.nextPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mediaPlayer) {
//				onVideoComplete(mediaPlayer, surfaceHolder);
				onVideoFinishListener.onFinish(mediaPlayer);
			}
		});
		nextPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mediaPlayer) {
				nextPlayer.start();
				onPreparedListener.onPrepared(nextPlayer);
			}
		});
//		currentPlayer = nextPlayer;
	}

	private void onVideoComplete() {
			this.isCompleted = true;
			sendMessage(IS_COMPLETE, null);
			currentVideoIndex++;
		Log.e("VideoComplete", String.valueOf(isCompleted));
	}

	private void playNext(final SurfaceHolder surfaceHolder){
		try {
			currentPlayer.setDisplay(null);
			Log.e("listener complete top", String.valueOf(currentVideoIndex));
			if (currentVideoIndex < urlList.size() - 1) {
				if(currentVideoIndex < cachePlayerList.size()){
					try{
//						currentPlayer = new MediaPlayer();
						currentPlayer = cachePlayerList.get(currentVideoIndex);
						currentPlayer.setDisplay(surfaceHolder);
						currentPlayer.start();
						isCompleted = false;
						isLoaded = false;
//						currentVideoIndex++;
						Log.e("listener complete", String.valueOf(currentVideoIndex));
					}catch (Exception e) {e.printStackTrace();}

				}

				currentPlayer.setScreenOnWhilePlaying(true);


			} else {
				if (finishListener != null) {
					finishListener.onFinish(currentPlayer);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}


		
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
		public void onFinish(MediaPlayer mediaPlayer);
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
				currentPlayer = null;
			}
			if (nextPlayer != null) {
				nextPlayer.release();
				nextPlayer = null;
			}
			if (cachePlayer != null) {
				cachePlayer.release();
				nextPlayer = null;
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

	private void sendMessage(int what, Object obj){
		Message msg = new Message();
		msg.what = what;
		msg.obj = obj;
		mHandler.sendMessage(msg);
	}
	public static class VideoInfo{
		public String sourceUrl = "";
		public String title = "";
		public String desc = "";
		public int duration = 0;
		public boolean isAd = false;
	}
}
