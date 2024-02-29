package com.example.tracksnap;

import java.util.List;

public class MovieModelClass {

    private String title;
    private String overview;
    private List<String> genre;
    private String duration;
    private String release;
    private double vote;
    private String rating;
    private String image;

    public MovieModelClass(String title, String overview, List<String> genre, String duration, String release, double vote, String rating, String image) {
        this.title = title;
        this.overview = overview;
        this.genre = genre;
        this.duration = duration;
        this.release = release;
        this.vote = vote;
        this.rating = rating;
        this.image = image;

    }

    public MovieModelClass(){

    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public List<String> getGenre() {
        return genre;
    }

    public String getDuration() {
        return duration;
    }

    public String getRelease() {
        return release;
    }

    public double getVote() {
        return vote;
    }

    public String getRating() {
        return rating;
    }

    public String getImage() {
        return image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public void setVote(double vote) {
        this.vote = vote;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
