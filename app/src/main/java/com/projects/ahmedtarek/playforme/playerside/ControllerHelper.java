package com.projects.ahmedtarek.playforme.playerside;

import android.support.v4.media.session.MediaControllerCompat;

/**
 * Created by Ahmed Tarek on 1/26/2017.
 */

public class ControllerHelper {
    private static MediaControllerCompat mediaController;

    public static MediaControllerCompat getMediaController() {
        return mediaController;
    }

    public static void setMediaController(MediaControllerCompat mediaController) {
        ControllerHelper.mediaController = mediaController;
    }
}
