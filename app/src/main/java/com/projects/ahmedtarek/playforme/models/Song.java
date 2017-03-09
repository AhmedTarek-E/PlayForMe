package com.projects.ahmedtarek.playforme.models;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.Serializable;

/**
 * Created by Ahmed Tarek on 1/10/2017.
 *
 */
public class Song implements Serializable {
    private long id = -1;
    private String artist;
    private String displayName;
    private long duration;
    private String title;
    private int year;
    private String album;
    private long albumId;

    public Song(long id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getYear() {
        return year != 0 ? String.valueOf(year) : "";
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getId() {
        return id;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public static Uri getSongUri(long id) {
        return ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
    }

    public static String getPrettyDuration(long duration) {
        long seconds = duration/1000;
        int minutes = (int) (seconds/60);
        seconds -= minutes*60;
        int hours = 0;
        if (minutes > 60) {
            hours = minutes/60;
            minutes -= hours*60;
        }
        String min = String.valueOf(minutes);
        String sec;

        if (seconds < 10) {
            sec = "0" + String.valueOf(seconds);
        } else {
            sec = String.valueOf(seconds);
        }

        if (hours > 0) {
            if (minutes < 10) {
                min = "0" + String.valueOf(minutes);
            }
            return String.valueOf(hours) + ":" + min + ":" + sec;
        } else {
            return min + ":" + sec;
        }
    }

}
