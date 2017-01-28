package com.projects.ahmedtarek.playforme.playerside;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;

import com.projects.ahmedtarek.playforme.models.Album;
import com.projects.ahmedtarek.playforme.models.Song;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Ahmed Tarek on 1/22/2017.
 */
public class MediaBrowserHelper {
    public static final int MEDIA_PLAYBACK_REQUEST = 1;
    public static final String MEDIA_GENRE_ROOT_ID = "GENRE";
    public static final String MEDIA_ALBUMS_ROOT_ID = "ALBUM";
    public static final String MEDIA_ARTISTS_ROOT_ID = "ARTIST";

    public static final String MEDIA_ALBUM_CONTENT_ID = "ALBUM_CONTENT";
    public static final String MEDIA_GENRE_CONTENT_ID = "GENRE_CONTENT";

    public static final String MEDIA_PLAYING_ID = "MEDIA_PLAYING";

    public static final String MEDIA_CONTENT_KEY = "MEDIA_CONTENT_KEY";

    public static final String[] SONG_PROJECTION = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.YEAR,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DISPLAY_NAME
    };

    public static final String[] ALBUM_PROJECTION = {
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.ALBUM_ART,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS,
            MediaStore.Audio.Albums.FIRST_YEAR
    };

    // song_projection_index
    public static int ID_INDEX = 0;
    public static int TITLE_INDEX = 1;
    public static int ARTIST_INDEX = 2;
    public static int ALBUM_INDEX = 3;
    public static int YEAR_INDEX = 4;
    public static int DURATION_INDEX = 5;
    public static int DISPLAY_NAME_INDEX = 6;

    //album_projection_index
    public static int ALBUM_ID_INDEX = 0;
    public static int ALBUM_NAME_INDEX = 1;
    public static int ALBUM_ARTIST_INDEX = 2;
    public static int ALBUM_ART_INDEX = 3;
    public static int NUMBER_OF_SONGS_INDEX = 4;
    public static int FIRST_YEAR_INDEX = 5;

    private Context context;

    public MediaBrowserHelper(Context context) {
        this.context = context;
    }

    public List<MediaBrowserCompat.MediaItem> getListOfGenres()  {
        Uri uri = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;
        Cursor genreCursor = query(uri, null, null, null, null);

        if (genreCursor != null && genreCursor.moveToFirst()) {
            List<MediaBrowserCompat.MediaItem> genreList = new ArrayList<>();
            do {
                int nameIdx = genreCursor.getColumnIndex(MediaStore.Audio.Genres.NAME);
                int IDIdx = genreCursor.getColumnIndex(MediaStore.Audio.Genres._ID);
                String genreName = genreCursor.getString(nameIdx);

                if (genreName.length() == 0)
                    continue;

                MediaBrowserCompat.MediaItem mediaItem = new MediaBrowserCompat.MediaItem(
                        new MediaDescriptionCompat.Builder()
                                .setTitle(genreName)
                                .setMediaId(genreCursor.getString(IDIdx))
                                .build(),
                        MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
                );
                genreList.add(mediaItem);
            } while (genreCursor.moveToNext());
            genreCursor.close();
            return genreList;
        }
        return null;
    }

    public List<MediaBrowserCompat.MediaItem> getSongsOfGenre(String genreId) {
        Uri uri = MediaStore.Audio.Genres.Members.getContentUri("external", Long.parseLong(genreId));
        Cursor cursor = query(uri,
                SONG_PROJECTION,
                MediaStore.Audio.Media.IS_MUSIC + " != 0",
                null,
                MediaStore.Audio.Media.DATE_ADDED + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            return buildSongsList(cursor);
        }
        return null;
    }

    public List<MediaBrowserCompat.MediaItem> getListOfAlbums() {
        Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor cursor = query(
                uri,
                ALBUM_PROJECTION,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            List<MediaBrowserCompat.MediaItem> albumList = new ArrayList<>();
            do {
                Album album = new Album(cursor.getLong(ALBUM_ID_INDEX));
                album.setAlbumName(cursor.getString(ALBUM_NAME_INDEX));
                album.setArtist(cursor.getString(ALBUM_ARTIST_INDEX));
                album.setAlbumArt(cursor.getString(ALBUM_ART_INDEX));
                album.setNumberOfSongs(cursor.getInt(NUMBER_OF_SONGS_INDEX));
                album.setYear(cursor.getInt(FIRST_YEAR_INDEX));

                Bundle extras = new Bundle();
                extras.putSerializable(MEDIA_ALBUMS_ROOT_ID, album);

                MediaBrowserCompat.MediaItem mediaItem = new MediaBrowserCompat.MediaItem(
                        new MediaDescriptionCompat.Builder()
                                .setTitle(album.getAlbumName())
                                .setExtras(extras)
                                .setMediaId(String.valueOf(album.getId()))
                                .build(),
                        MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
                );
                albumList.add(mediaItem);
            } while (cursor.moveToNext());
            cursor.close();
            return albumList;
        }
        return null;
    }

    public List<MediaBrowserCompat.MediaItem> getSongsOfAlbum(String albumId) {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = query(
                uri,
                SONG_PROJECTION,
                MediaStore.Audio.Media.ALBUM_ID + " = ?",
                new String[] {albumId},
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            return buildSongsList(cursor);
        }
        return null;
    }

    private List<MediaBrowserCompat.MediaItem> buildSongsList(Cursor cursor) {
        List<MediaBrowserCompat.MediaItem> songsList = new ArrayList<>();
        do {
            Song song = new Song(cursor.getLong(ID_INDEX));
            song.setTitle(cursor.getString(TITLE_INDEX));
            song.setArtist(cursor.getString(ARTIST_INDEX));
            song.setAlbum(cursor.getString(ALBUM_INDEX));
            song.setYear(cursor.getInt(YEAR_INDEX));
            song.setDuration(cursor.getLong(DURATION_INDEX));
            song.setDisplayName(cursor.getString(DISPLAY_NAME_INDEX));

            Bundle extras = new Bundle();
            extras.putSerializable(MEDIA_CONTENT_KEY, song);

            MediaBrowserCompat.MediaItem mediaItem = new MediaBrowserCompat.MediaItem(
                    new MediaDescriptionCompat.Builder()
                            .setTitle(song.getTitle())
                            .setExtras(extras)
                            .setMediaId(String.valueOf(song.getId()))
                            .build(),
                    MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
            );

            songsList.add(mediaItem);
        } while (cursor.moveToNext());
        cursor.close();
        return songsList;
    }

    private Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        ContentResolver contentResolver = context.getContentResolver();
        return contentResolver.query(uri, projection, selection, selectionArgs, sortOrder);
    }
}
