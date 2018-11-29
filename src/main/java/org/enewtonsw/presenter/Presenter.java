package org.enewtonsw.presenter;

import org.enewtonsw.model.Model;
import org.enewtonsw.model.State;
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

    public static final long FIFTY_NINE_MINUTES_FIFTY_NINE_SECONDS = 60 * 60 * 1000L - 1000L;
    public static final long FIFTY_EIGHT_MINUTES_FIFTY_NINE_SECONDS = 59 * 60 * 1000L - 1000L;

    public static final String SHORT_BREAK_MESSAGE = "Taking a short break...";
    public static final String WORKING_MESSAGE = "Working...";
    public static final String TIME_EXPIRED_MESSAGE = "%s Time Expired!";
    public static final String LONG_BREAK_MESSAGE = "Taking a long break...";
    public static final int MAX_SHORT_BREAKS = 4;
    public static final String RESET_MESSAGE = "Reset...";
    public static final String SNOOZING_MESSAGE = "Snoozing...";
    public static final String ACK_MESSAGE = "Acknowledged";
    public static final long SIXTY_SECONDS = 60 * 1000L;
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
        model.setCurrentState(State.WORKING);
        view.setTime(WORK_TIME);
        view.setMessage(WORKING_MESSAGE);
    }

    public void timerExpired() {
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

        view.setMessage(String.format(TIME_EXPIRED_MESSAGE, model.getCurrentState()));
        model.setCurrentState(State.ALARMING);
    }

    public void takeAShortBreak() {
        int breakCount = model.getBreakCount();
        if (breakCount < MAX_SHORT_BREAKS) {
            breakCount++;
        } else {
            breakCount = 0;
        }

        model.setBreakCount(breakCount);
        model.setCurrentState(State.BREAKING);
        view.setShortBreakIndicator(breakCount);
        view.setTime(SHORT_BREAK);
        view.setMessage(SHORT_BREAK_MESSAGE);
    }

    public void takeALongBreak() {
        model.setCurrentState(State.BREAKING);
        model.setBreakCount(0);
        view.setShortBreakIndicator(0);
        view.setTime(LONG_BREAK);
        view.setMessage(LONG_BREAK_MESSAGE);
    }

    public void snooze() {
        model.setCurrentState(State.SNOOZING);
        view.setTime(SNOOZE_TIME);
        view.setMessage(SNOOZING_MESSAGE);
    }

    public void reset() {
        model.setCurrentState(State.IDLE);
        model.setBreakCount(0);
        view.reset();
        view.setMessage(RESET_MESSAGE);
    }

    public void stopAudio() {
        AudioPlayer.player.stop(loop);
    }

    public void addMinute() {
        long timeLeft = view.getTime();

        if (timeLeft > FIFTY_EIGHT_MINUTES_FIFTY_NINE_SECONDS)
            throw new IllegalStateException();

        view.setTime(SIXTY_SECONDS + timeLeft);
    }

    public void subtractMinute() {
        long timeLeft = view.getTime();

        if (timeLeft < SIXTY_SECONDS)
            throw new IllegalStateException();

        view.setTime(timeLeft - SIXTY_SECONDS);
    }

    public void acknowledge() {
        if (model.getCurrentState() == State.WORKING) {
            int breakCount = model.getBreakCount();
            if (breakCount < MAX_SHORT_BREAKS) {
                takeAShortBreak();
            } else {
                takeALongBreak();
            }
        } else if (model.getCurrentState() == State.BREAKING) {
            startWork();
        }
    }
}

