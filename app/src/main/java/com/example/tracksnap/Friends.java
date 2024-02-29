package com.example.tracksnap;

public class Friends {
    private String username;
    private int avatarImg;

    public Friends(String username, int avatarImg) {
        this.username = username;
        this.avatarImg = avatarImg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAvatarImg() {
        return avatarImg;
    }

    public void setAvatarImg(int avatarImg) {
        this.avatarImg = avatarImg;
    }
}
