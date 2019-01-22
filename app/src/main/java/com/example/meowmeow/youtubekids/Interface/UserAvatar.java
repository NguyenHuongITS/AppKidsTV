package com.example.meowmeow.youtubekids.Interface;

public class UserAvatar {

    public int getAvatar() {
        return Avatar;
    }

    public void setAvatar(int avatar) {
        Avatar = avatar;
    }

    private int Avatar;
    public UserAvatar(int avatar) {
        this.Avatar  = avatar;
    }
}
