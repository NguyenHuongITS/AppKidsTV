package com.example.meowmeow.youtubekids.Interface;

public class SearchVideo {

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

    public String getIdVideo() {
        return IdVideo;
    }

    public void setIdVideo(String idVideo) {
        IdVideo = idVideo;
    }

    String Thumbnails;
    String IdVideo;

    public SearchVideo(String title, String thumbnails, String idVideo) {
        this.Title = title;
        this.Thumbnails = thumbnails;
        this.IdVideo = idVideo;
    }
}




