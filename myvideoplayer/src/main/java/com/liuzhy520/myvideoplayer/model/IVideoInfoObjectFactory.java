package com.liuzhy520.myvideoplayer.model;

import java.util.ArrayList;

/**
 * Created by Wayne on 2016/10/8.
 * this factory is used to generate the VideoInfo Object
 */

public interface IVideoInfoObjectFactory {
    /**
     * to generate a simple VideoInfo model
     * @param builder see {@link com.liuzhy520.myvideoplayer.model.VideoInfo.Builder}
     * @return VideoInfo
     */
    VideoInfo generateVideoInfo(VideoInfo.Builder builder);

    /**
     * to generate a playlist of the MyMediaPlayer see {@link com.liuzhy520.myvideoplayer.model.VideoInfo.Builder}
     * @param data can be any object
     * @return ArrayList<VideoInfo>
     */
    ArrayList<VideoInfo> generateVideoInfoList(Object data);
}
