package com.projects.ahmedtarek.playforme;

/**
 * Created by Ahmed Tarek on 1/26/2017.
 */

public class Utils {

    public static final String GENRE_MODE = "genre_mode";
    public static final String ALBUM_MODE = "album_mode";
    public static final String MODE_KEY = "mode_key";

    public static final String ACTION_CURRENT_PROGRESS = "ahmedtarek.ACTION_CURRENT_PROGRESS";
    public static final String ACTION_ACTIVITY_START = "ahmedtarek.ACTION_ACTIVITY_START";
    public static final String ACTION_COMMUNICATION_ACTIVITY_SERVICE = "ahmedtarek.ACTION_COMMUNICATION";


    // DetailedPlaylistMode
    public final static String PLAYLIST_MODE = "playlist_mode";
    public final static String NOW_PLAYLIST_MODE = "now_playlist_mode";

    public static String getTrimmedText(String text) {
        int trimLength = 15;
        if (text.length() > trimLength) {
            return text.substring(0, trimLength) + "...";
        }
        return text;
    }
}
