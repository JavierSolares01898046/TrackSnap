package com.example.tracksnap;

import java.util.List;

public class HelperClass {
    String email, username, password, bio, genre, rating, duration;
//    List<String> genre;
//    List<String> rating;
//    List<String> duration;

    public HelperClass() {
    }

    public HelperClass(String email, String username, String password, String bio, String genre, String rating, String duration) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.bio = bio;
        this.genre = genre;
        this.rating = rating;
        this.duration = duration;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
