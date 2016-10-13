package com.liuzhy520.myvideoplayer.core;

/**
 * Created by Wayne on 2016/10/4.
 * this is the player interface
 */

public interface IPlayer {

    /**
     * start to play the video
     * Starts or resumes playback. If playback had previously been paused,
     * playback will continue from where it was paused. If playback had
     * been stopped, or never started before, playback will start at the beginning.
     */
    void start();

    /**
     * pause the video
     * Stops playback after playback has been stopped or paused.
     */
    void pause();

    /**
     * stop the video
     * Pauses playback. Call start() to resume.
     */
    void stop();

    /**
     * release and clear the video object
     */
    void release();

    /**
     * Seeks to specified time position
     * @param position the position of the whole video length
     */
    void seekTo(int position);

    /**
     * Gets the current playback position.
     * @return the current position in milliseconds
     */
    int getCurrentVideoPosition();

    /**
     * Gets the duration of the file.
     * @return the duration in milliseconds, if no duration is available
     *         (for example, if streaming live content), -1 is returned.
     */
    int getCurrentVideoDuration();

    /**
     * Checks whether the MediaPlayer is playing.
     * @return true if currently playing, false otherwise
     * @throws IllegalStateException if the internal player engine has not been
     * initialized or has been released.
     */
    boolean isVideoPlaying();
}
