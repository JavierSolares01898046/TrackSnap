// MovieModelClass.java
package com.example.tracksnap;

import java.util.List;

public class MovieModelClass {

    private String id;
    private String title;
    private String overview;
    private String releaseDate;
    private double voteAverage;
    private String image;
    private String username;

    private List<String> genreList;

    public MovieModelClass(String id, String title, String overview, String releaseDate, double voteAverage, String image, List<String> genreList) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.image = image;
        this.genreList = genreList;
        this.username = username;
    }

    public MovieModelClass() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setUsername(String user_name) {
        this.username = user_name;
    }
    public String getUsername() { return username; }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getGenreList() {
        return genreList;
    }

    public void setGenreList(List<String> genreList) {
        this.genreList = genreList;
    }
}
