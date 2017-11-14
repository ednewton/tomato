package org.enewtonsw.presenter;

import org.enewtonsw.model.Model;
import org.enewtonsw.view.View;
import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

import java.io.IOException;

public class Presenter {
    public static final int SHORT_BREAK = 5 * 60 * 1000;
    public static final int SNOOZE_TIME = 5 * 60 * 1000;
    public static final int WORK_TIME = 25 * 60 * 1000;
    public static final int LONG_BREAK = 15 * 60 * 1000;
    public static final String SHORT_BREAK_MESSAGE = "Taking a short break...";
    public static final String WORKING_MESSAGE = "Working...";
    public static final String TIME_EXPIRED_MESSAGE = "%s Time Expired!";
    public static final String LONG_BREAK_MESSAGE = "Taking a long break...";
    public static final int MAX_SHORT_BREAKS = 4;
    public static final String RESET_MESSAGE = "Reset...";
    public static final String SNOOZING_MESSAGE = "Snoozing...";
    public static final String ACK_MESSAGE = "Acknowledged";
    private static final String AUDIO_FILE = "/audio/Ring01.wav";
    private View view;
    private Model model;
    private AudioStream audioStream;
    private ContinuousAudioDataStream loop;

    public Presenter(View view, Model model) {
        this.view = view;
        this.model = model;
        this.view.setVersion(model.getVersion());
    }

    public void startWork() {
        view.setTime(WORK_TIME);
        view.setMessage(WORKING_MESSAGE);
    }

    public void timerExpired(String activity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    audioStream = new AudioStream(getClass().getResourceAsStream(AUDIO_FILE));
                    AudioData audioData = audioStream.getData();
                    loop = new ContinuousAudioDataStream(audioData);
                    AudioPlayer.player.start(loop);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        view.setMessage(String.format(TIME_EXPIRED_MESSAGE, activity));
    }

    public void takeAShortBreak() {
        int newBreakCount = model.getBreakCount();
        if (newBreakCount < MAX_SHORT_BREAKS)
            newBreakCount = newBreakCount + 1;
        else
            newBreakCount = 0;

        model.setBreakCount(newBreakCount);
        view.setShortBreakIndicator(newBreakCount);
        view.setTime(SHORT_BREAK);
        view.setMessage(SHORT_BREAK_MESSAGE);
    }

    public void takeALongBreak() {
        model.setBreakCount(0);
        view.setShortBreakIndicator(0);
        view.setTime(LONG_BREAK);
        view.setMessage(LONG_BREAK_MESSAGE);
    }

    public void snooze() {
        view.setTime(SNOOZE_TIME);
        view.setMessage(SNOOZING_MESSAGE);
    }

    public void reset() {
        model.setBreakCount(0);
        view.reset();
        view.setMessage(RESET_MESSAGE);
    }

    public void stopAudio() {
        AudioPlayer.player.stop(loop);
    }
}

