package com.example.meowmeow.youtubekids.Interface;

public class ExploreVideo {

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    String Title;

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

    public ExploreVideo(String title, String thumbnails,String idVideo) {
        Title = title;
        Thumbnails = thumbnails;
        IdVideo = idVideo;
    }

}
