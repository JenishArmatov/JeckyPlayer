package com.armatov.music.util;

import android.app.Activity;
import android.content.Intent;
import android.media.audiofx.AudioEffect;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import com.armatov.music.App;
import com.armatov.music.R;
import com.armatov.music.helper.MusicPlayerRemote;
import com.armatov.music.model.Genre;
import com.armatov.music.model.Playlist;
import com.armatov.music.ui.activities.AlbumDetailActivity;
import com.armatov.music.ui.activities.ArtistDetailActivity;
import com.armatov.music.ui.activities.ChangeVisualizationActivity;
import com.armatov.music.ui.activities.GenreDetailActivity;
import com.armatov.music.ui.activities.PlaylistDetailActivity;
import com.armatov.music.visualizermusicplayer.Equalizer;

public class NavigationUtil {

    public static void goToArtist(@NonNull final Activity activity, final long artistId, @Nullable Pair... sharedElements) {
        final Intent intent = new Intent(activity, ArtistDetailActivity.class);
        intent.putExtra(ArtistDetailActivity.EXTRA_ARTIST_ID, artistId);

        //noinspection unchecked
        if (sharedElements != null && sharedElements.length > 0) {
            activity.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedElements).toBundle());
        } else {
            activity.startActivity(intent);
        }
    }

    public static void goToAlbum(@NonNull final Activity activity, final long albumId, @Nullable Pair... sharedElements) {
        final Intent intent = new Intent(activity, AlbumDetailActivity.class);
        intent.putExtra(AlbumDetailActivity.EXTRA_ALBUM_ID, albumId);

        //noinspection unchecked
        if (sharedElements != null && sharedElements.length > 0) {
            activity.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedElements).toBundle());
        } else {
            activity.startActivity(intent);
        }
    }

    public static void goToGenre(@NonNull final Activity activity, final Genre genre, @Nullable Pair... sharedElements) {
        final Intent intent = new Intent(activity, GenreDetailActivity.class);
        intent.putExtra(GenreDetailActivity.EXTRA_GENRE, genre);

        activity.startActivity(intent);
    }

    public static void goToPlaylist(@NonNull final Activity activity, final Playlist playlist, @Nullable Pair... sharedElements) {
        final Intent intent = new Intent(activity, PlaylistDetailActivity.class);
        intent.putExtra(PlaylistDetailActivity.EXTRA_PLAYLIST, playlist);

        activity.startActivity(intent);
    }

    public static void openEqualizer(@NonNull final Activity activity) {
        final int sessionId = MusicPlayerRemote.getAudioSessionId();
        if (sessionId == AudioEffect.ERROR_BAD_VALUE) {
            Toast.makeText(activity, activity.getResources().getString(R.string.no_audio_ID), Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(App.getInstance(), Equalizer.class);
            activity.startActivity(intent);
        }
    }
    public static void changeVisualizer(@NonNull final Activity activity) {
        final int sessionId = MusicPlayerRemote.getAudioSessionId();
        if (sessionId == AudioEffect.ERROR_BAD_VALUE) {
            Toast.makeText(activity, activity.getResources().getString(R.string.no_audio_ID), Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(activity, ChangeVisualizationActivity.class);
            activity.startActivity(intent);
        }
    }
}
