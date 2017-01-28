package com.projects.ahmedtarek.playforme.models;

import java.io.Serializable;

/**
 * Created by Ahmed Tarek on 1/16/2017.
 */
public class Album implements Serializable{
    private String albumName;
    private String artist;
    private String albumArt;
    private long id;
    private int year;
    private int numberOfSongs;

    public Album(long id) {
        this.id = id;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public String getYear() {
        return year != 0 ? String.valueOf(year) : "";
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    public void setNumberOfSongs(int numberOfSongs) {
        this.numberOfSongs = numberOfSongs;
    }

    public long getId() {
        return id;
    }
}
