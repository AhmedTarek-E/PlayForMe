package com.projects.ahmedtarek.playforme.playerside;

import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;

/**
 *
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

    public static NotificationCompat.Builder buildMediaNotification(Context context,
                                                                    MediaSessionCompat mediaSession) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        return builder;
    }
}
