package com.liuzhy520.myvideoplayer.model;

import android.content.res.AssetFileDescriptor;

import java.io.FileDescriptor;

/**
 * Created by Wayne on 2016/10/8.
 * this class is used to be a object to store the information of the video
 * internal use
 */

public class VideoInfo {
    public String sourceUrl = "";     // video source url
    public String title = "";         // video title
    public String desc = "";          // video description
    public int duration = 0;          // the total duration
    public boolean isAd = false;      // a flag to tell if the video is advertisement or not

    public AssetFileDescriptor assetFileDescriptor;  // if the file is from /asserts , use this

    /** play simple local decrypted file relatied **/
    public FileDescriptor fd;         /** can be used for local encrypted access, see {@link FileDescriptor} **/
    public long fileOffset = 0;       // the offset into the file where the data to be played starts, in bytes
    public long fileLength = 0;       // the length in bytes of the data to be played

    public static class Builder{
        private String sourceUrl = "";     // video source url
        private String title = "";         // video title
        private String desc = "";          // video description
        private int duration = 0;          // the total duration
        private boolean isAd = false;      // a flag to tell if the video is advertisement or not
        private AssetFileDescriptor assetFileDescriptor;  // if the file is from /asserts , use this
        private FileDescriptor fd;         // /** can be used for local encrypted access, see {@link FileDescriptor} **/
        public long fileOffset = 0;       // the offset into the file where the data to be played starts, in bytes
        public long fileLength = 0;       // the length in bytes of the data to be played

        public Builder setFileOffset(long fileOffset) {
            this.fileOffset = fileOffset;
            return this;
        }

        public Builder setFileLength(long fileLength) {
            this.fileLength = fileLength;
            return this;
        }

        public Builder setFileDescriptor(FileDescriptor fd) {
            this.fd = fd;
            return this;
        }

        public Builder setAssetFileDescriptor(AssetFileDescriptor assetFileDescriptor) {
            this.assetFileDescriptor = assetFileDescriptor;
            return this;
        }

        public Builder setSourceUrl(String url){
            this.sourceUrl = url;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDesc(String desc) {
            this.desc = desc;
            return this;
        }

        public Builder setDuration(int duration) {
            this.duration = duration * 1000;
            return this;
        }

        public Builder setAd(boolean ad) {
            isAd = ad;
            return this;
        }

        public VideoInfo build(){
            VideoInfo videoInfo = new VideoInfo();
            videoInfo.sourceUrl = this.sourceUrl;
            videoInfo.title = this.title;
            videoInfo.desc = this.desc;
            videoInfo.duration = this.duration;
            videoInfo.isAd = this.isAd;
            videoInfo.assetFileDescriptor = this.assetFileDescriptor;
            videoInfo.fd = this.fd;
            videoInfo.fileLength = this.fileLength;
            videoInfo.fileOffset = this.fileOffset;
            return videoInfo;
        }
    }
}

