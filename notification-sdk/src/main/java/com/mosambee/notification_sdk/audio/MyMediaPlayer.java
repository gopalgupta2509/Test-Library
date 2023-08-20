package com.mosambee.notification_sdk.audio;

import android.media.MediaPlayer;

public class MyMediaPlayer {
    private volatile static MediaPlayer instance;
    public static MediaPlayer getInstance() {

        if (instance == null) {
            synchronized (MyMediaPlayer.class) {
                if (instance == null)
                    instance = new MediaPlayer();
            }
        }
        return instance;
    }
}