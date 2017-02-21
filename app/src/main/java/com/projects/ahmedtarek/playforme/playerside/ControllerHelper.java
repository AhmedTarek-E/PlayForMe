package com.projects.ahmedtarek.playforme.playerside;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.session.MediaController;
import android.preference.PreferenceManager;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v7.app.NotificationCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.view.KeyEventCompat;
import android.view.KeyEvent;

import com.projects.ahmedtarek.playforme.activities.PlayMusicActivity;

/**
 *
 * Created by Ahmed Tarek on 1/26/2017.
 */

public class ControllerHelper {
    private static final String REPEAT_KEY = "repeat_key";
    public static final String COMMUNICATION_KEY = "communication_key";

    public static final int REPEAT_FLAG = 1;
    public static final int SHUFFLE_FLAG = 2;

    private static MediaControllerCompat mediaController;

    public static MediaControllerCompat getMediaController() {
        return mediaController;
    }

    public static void setMediaController(MediaControllerCompat mediaController) {
        ControllerHelper.mediaController = mediaController;
    }

    public static NotificationCompat.Builder buildMediaNotification(
            Context context, MediaSessionCompat mediaSession) {

        MediaControllerCompat mediaController = mediaSession.getController();
        MediaMetadataCompat metadata = mediaController.getMetadata();
        MediaDescriptionCompat mediaDescription = metadata.getDescription();

        /*TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder
                .addParentStack(PlayMusicActivity.class)
                .addNextIntent(new Intent(context, PlayMusicActivity.class));*/

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder
                .setContentTitle(mediaDescription.getTitle())
                .setContentText(metadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST))
                .setSubText(mediaDescription.getDescription())
                .setTicker(mediaDescription.getTitle())
                .setLargeIcon(mediaDescription.getIconBitmap())
                .setContentIntent(
                        //stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
                        ControllerHelper.getMediaController().getSessionActivity()
                )
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDeleteIntent(getActionIntent(context, KeyEvent.KEYCODE_MEDIA_STOP));
        return builder;
    }

    public static PendingIntent getActionIntent(Context context, int mediaKeyEvent) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        intent.setPackage(context.getPackageName());
        intent.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, mediaKeyEvent));

        return PendingIntent.getBroadcast(context, mediaKeyEvent, intent, 0);
    }

    public static void notify(Context context, int notificationID, NotificationCompat.Builder builder) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, builder.build());
    }

    public static void cancelNotification(Context context, int notificationID) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationID);
    }

    public static void setRepeat(Context context, boolean repeat) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit()
                .putBoolean(REPEAT_KEY, repeat)
                .apply();
    }

    public static boolean isRepeated(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(REPEAT_KEY, false);
    }
}
