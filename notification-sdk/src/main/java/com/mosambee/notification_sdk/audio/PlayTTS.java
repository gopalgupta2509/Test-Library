package com.mosambee.notification_sdk.audio;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.util.Log;

import com.mosambee.notification_sdk.backup_data.TRACEOld;

public class PlayTTS extends AsyncTask<Void, Void, Void> {
    private static final String LOG_TAG = PlayTTS.class.getSimpleName();
    Context context;
    private static MediaPlayer mediaPlayer;
    int[] soundIDs;
    int idx = 1;
    int mCompleted = 0;

    public PlayTTS(final Context context, final int[] soundIDs) {
        this.context = context;
        this.soundIDs = soundIDs;
        mediaPlayer = MyMediaPlayer.getInstance();
        TRACEOld.i("mediaPlayergetInstance::::::::::: " + mediaPlayer);
        TRACEOld.i("NOTI:::AUDIO:::LENGTH::: " + soundIDs.length + " :: " + soundIDs.getClass().getName());
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer = mediaPlayer.create(context, soundIDs[0]);
        TRACEOld.i("mediaPlayer::::::::::: " + mediaPlayer);
        setNextMediaForMediaPlayer(mediaPlayer);
    }

    public void setNextMediaForMediaPlayer(MediaPlayer player) {
        try {

            player.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mCompleted++;
                    mp.reset();
                    if (mCompleted < soundIDs.length) {
                        try {
                            AssetFileDescriptor afd = context.getResources().openRawResourceFd(soundIDs[mCompleted]);
                            if (afd != null) {
                                mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                                afd.close();
                                mp.prepare();
                                mp.start();
                            }
                        } catch (Exception ex) {
                            TRACEOld.e(ex);
                        }

                        Log.i(LOG_TAG, "Inside_if_mCompleted < tracks.length:::::::::::");
                    } else {
                        mCompleted = 0;
                        /*Completed audio*/
                    }
                }
            });
            player.start();
        } catch (Exception exception) {
            TRACEOld.i("SOUND_Exception::::::::::::" + exception.getMessage());
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            //mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            TRACEOld.i("PLAY TTS EXCEPTION:::01 " + e);
        } catch (SecurityException e) {
            TRACEOld.i("PLAY TTS EXCEPTION:::02 " + e);
        } catch (IllegalStateException e) {
            TRACEOld.i("PLAY TTS EXCEPTION:::03 " + e);
        }

        return null;
    }
}