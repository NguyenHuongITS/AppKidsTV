package com.example.meowmeow.youtubekids.Interface;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MusicVideo {

    public MusicVideo(String title, String thumbnails,String idVideo) {
        Title = title;
        Thumbnails = thumbnails;
        IdVideo = idVideo;
    }

    String Title;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getThumbnails() {
        return Thumbnails;
    }

    public void setThumbnails(String thumbnails) {
        Thumbnails = thumbnails;
    }

    String Thumbnails;

    public String getIdVideo() {
        return IdVideo;
    }

    public void setIdVideo(String idVideo) {
        IdVideo = idVideo;
    }

    String IdVideo;
}
