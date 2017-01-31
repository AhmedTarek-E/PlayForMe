package com.projects.ahmedtarek.playforme;

/**
 * Created by Ahmed Tarek on 1/26/2017.
 */

public class Utils {

    public static final String GENRE_MODE = "genre_mode";
    public static final String ALBUM_MODE = "album_mode";
    public static final String MODE_KEY = "mode_key";


    // DetailedPlaylistMode
    public final static String PLAYLIST_MODE = "playlist_mode";

    public static String getTrimmedText(String text) {
        int trimLength = 15;
        if (text.length() > trimLength) {
            return text.substring(0, trimLength) + "...";
        }
        return text;
    }
}
