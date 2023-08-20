package com.mosambee.notification_sdk.audio;

import android.app.Activity;
import android.media.MediaPlayer;

import com.mosambee.notification_sdk.backup_data.TRACEOld;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;



public class EnglishUtils {
    int CRORE = 10000000;
    int LAKH = 100000;
    int THOUSAND = 1000;
    int HUNDRED = 100;

    MediaPlayer mPlayer;
    Activity activity;
    List<String> audioQueue;
    private static Queue<SoundModel> soundModelList = new PriorityQueue<>();

    public EnglishUtils(Activity activity) {
        this.activity = activity;
        audioQueue = new CopyOnWriteArrayList<>();
    }

    public void playAudio(String amount){
        SoundModel model = new SoundModel();
        model.setSoundAmount(amount);
        String generateId = String.valueOf(createID());
        model.setAutoGenSoundId(generateId);
        soundModelList.add(model);

    }

    public void textToSpeechEn(String samt) {
        try {
            if (samt.equalsIgnoreCase("confirmationCode")) {
                osl_tts_play("eotp");
            } else if (samt.equalsIgnoreCase("confirmation")) {
                osl_tts_play("eauth_success");
            }
            {
                String[] samtArr = new String[0];

                int amt;

                int paise;

                if (samt.contains(".")) {
                    samtArr = samt.split("\\.");
                    amt = Integer.parseInt(samtArr[0]);

                    paise = Integer.parseInt(samtArr[1]);
                } else {

                    amt = Integer.parseInt(samt);

                    paise = 0;

                }

                int m;


                if (amt >= CRORE) {    // //If it is more than Ten million,
                    m = amt / CRORE;
                    sdk_tts_play_lt(m);    //  play the number of more than Ten million first
                    sdk_tts_play_unit(3);    //plus the  "Crore"
                    amt = amt % CRORE;        // Remove more than one million of the amount
                }

                if (amt >= LAKH) {    // //If it is more than One hundred thousand,
                    m = amt / LAKH;
                    sdk_tts_play_lt(m);    //  play the number of more than One hundred thousand first
                    sdk_tts_play_unit(2);    //plus the LAKH
                    amt = amt % LAKH;        // Remove more than One hundred thousand of the amount
                }

                // Determine if it is more than a thousand
                if (amt >= THOUSAND) {
                    m = amt / THOUSAND;
                    sdk_tts_play_lt(m);        // play a part of a thousand or more
                    sdk_tts_play_unit(1);            // play a thousand
                    amt = amt % THOUSAND;
                    TRACEOld.i("INSIDE:::TTS:::AMT_THOUDAND+:: " + amt + " :: " + m);
                }

                sdk_tts_play_lt(amt);            // Play numbers below a thousand
                TRACEOld.i("INSIDE:::TTS:::AMT_AFTER:::THOUDAND+:: " + amt);

                audioQueue.add("erupees");

                if (paise > 0) {

                    sdk_tts_play_lt(paise);            // Play numbers below a thousand

                    audioQueue.add("paise");
                }

                audioQueue.add("received");
            }


        } catch (NumberFormatException e) {

        }

        osl_tts_play(audioQueue);

    }


    void sdk_tts_play_lt(int amt) {
        int m;
        m = amt / 100;
        if (m > 0) {
            sdk_tts_play_num(m);
            sdk_tts_play_unit(0);        // hundred
        }

        amt = amt % 100;
        if ((amt > 20)) { //  greater than 20
//            APP_TRACE("English Language and play the amount greater than 20\r\n");
            m = amt / 10;
            sdk_tts_play_num(m * 10);    // Tens first digits
            amt = amt % 10;
        }
        if (amt > 0)
            sdk_tts_play_num(amt);   // Less than 20 direct play
    }


    void sdk_tts_play_unit(int index) {

        String filename = "unit" + index;
        audioQueue.add(filename);
        //osl_tts_play(filename);
    }

    // Play the numbers 0-19 20-90
    void sdk_tts_play_num(int num) {

        String filename = "enum" + num;
        //osl_tts_play(filename);
        audioQueue.add(filename);
    }

    private void osl_tts_play(String filename) {
        TRACEOld.i("CALL_osl_tts_play:::::::::::::");

        TRACEOld.i("FILENAME:::1 " + filename);


        MediaPlayer mPlayer = MediaPlayer.create(activity, activity.getResources().getIdentifier(filename,
                "raw", activity.getPackageName()));

        if (!mPlayer.isPlaying()) {
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    TRACEOld.i("INSIDE:::RELEASE::: " + filename);
                    mp.release();
                }
            });
        }

    }

    private synchronized void osl_tts_play(List<String> audioQueue) {
        TRACEOld.i("CALL_osl_tts_play:::::::::::::");
        int[] soundIDs = new int[audioQueue.size()];
        for (int i = 0; i < audioQueue.size(); i++) {
            soundIDs[i] = activity.getResources().getIdentifier(audioQueue.get(i), "raw", activity.getPackageName());
            TRACEOld.i("soundIDs:::::::::::" + soundIDs[i]);
        }
        TRACEOld.i("soundIDs:::::::::::" + soundIDs);
        PlayTTS playAudio = new PlayTTS(activity.getApplicationContext(), soundIDs);
        playAudio.execute();

    }

    public static int createID() {
        Date now = new Date();
        String newDate = new StringBuilder(new SimpleDateFormat("ddHHmmssSS", Locale.US).format(now)).deleteCharAt(6).toString();
        int id = Integer.parseInt(newDate);
        return id;
    }

}