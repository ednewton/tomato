package org.enewtonsw.presenter;

import org.enewtonsw.model.Model;
import org.enewtonsw.model.State;
import org.enewtonsw.view.View;

import javax.sound.sampled.*;
import java.io.IOException;

public class Presenter {
    public static final String ACK_MESSAGE = "Acknowledged";
    static final int SHORT_BREAK = 5 * 60 * 1000;
    static final int SNOOZE_TIME = 5 * 60 * 1000;
    static final int WORK_TIME = 25 * 60 * 1000;
    static final int LONG_BREAK = 15 * 60 * 1000;
    static final long FIFTY_NINE_MINUTES_FIFTY_NINE_SECONDS = 60 * 60 * 1000L - 1000L;
    static final long FIFTY_EIGHT_MINUTES_FIFTY_NINE_SECONDS = 59 * 60 * 1000L - 1000L;
    static final String SHORT_BREAK_MESSAGE = "Taking a short break...";
    static final String WORKING_MESSAGE = "Working...";
    static final String TIME_EXPIRED_MESSAGE = "%s Time Expired!";
    static final String LONG_BREAK_MESSAGE = "Taking a long break...";
    static final int MAX_SHORT_BREAKS = 4;
    static final String RESET_MESSAGE = "Reset...";
    static final String SNOOZING_MESSAGE = "Snoozing...";
    static final long SIXTY_SECONDS = 60 * 1000L;
    private static final String AUDIO_FILE = "/audio/Ring01.wav";
    private View view;
    private Model model;
    private Clip clip;
    private AudioInputStream audioStream;

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
                    clip = AudioSystem.getClip();
                    audioStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(AUDIO_FILE));
                    clip.open(audioStream);
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        view.setMessage(String.format(TIME_EXPIRED_MESSAGE, model.getCurrentState()));
        view.setAcknowledgeButtonText(getNextStateName());
    }

    public void takeAShortBreak() {
        takeABreak();
    }

    public void takeALongBreak() {
        model.setBreakCount(Presenter.MAX_SHORT_BREAKS);
        takeABreak();
    }

    void takeABreak() {
        int breakCount = model.getBreakCount();
        int breakTime = SHORT_BREAK;
        String message = SHORT_BREAK_MESSAGE;

        if (breakCount == MAX_SHORT_BREAKS - 1) {
            breakCount++;
            view.disableShortBreakButton();
        } else if (breakCount < MAX_SHORT_BREAKS) {
            breakCount++;
        } else {
            breakCount = 0;
            breakTime = LONG_BREAK;
            message = LONG_BREAK_MESSAGE;
            view.enableShortBreakButton();
        }

        view.setTime(breakTime);
        view.setMessage(message);
        view.setShortBreakIndicator(breakCount);
        model.setBreakCount(breakCount);
        model.setCurrentState(State.BREAKING);
    }

    public void snooze() {
        view.setTime(SNOOZE_TIME);
        view.setMessage(SNOOZING_MESSAGE);
    }

    public void reset() {
        model.setCurrentState(State.IDLE);
        model.setBreakCount(0);
        view.reset();
        view.setMessage(RESET_MESSAGE);
        view.enableShortBreakButton();
    }

    public void stopAudio() {
        clip.stop();
        clip.flush();
        clip.close();
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
            takeABreak();
        } else if (model.getCurrentState() == State.BREAKING) {
            startWork();
        }
    }

    String getNextStateName() {
        if (model.getCurrentState() == State.WORKING) {
            return model.getBreakCount() < MAX_SHORT_BREAKS ? "Short Break" : "Long Break";
        } else {
            return "Work";
        }

    }
}

