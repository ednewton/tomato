package org.enewtonsw.presenter;

import org.enewtonsw.model.Model;
import org.enewtonsw.view.View;

public class Presenter {
    public static final int SHORT_BREAK = 5 * 60 * 1000;
    public static final int WORK_TIME = 25 * 60 * 1000;
    public static final int LONG_BREAK = 15 * 60 * 1000;
    public static final String SHORT_BREAK_MESSAGE = "Taking a short break...";
    public static final String WORKING_MESSAGE = "Working...";
    public static final String TIME_EXPIRED_MESSAGE = "Time Expired!";
    public static final String LONG_BREAK_MESSAGE = "Taking a long break...";
    public static final int MAX_SHORT_BREAKS = 4;

    private View view;
    private Model model;

    public Presenter(View view, Model model) {
        this.view = view;
        this.model = model;
    }

    public void startWork() {
        view.setTime(WORK_TIME);
        view.setMessage(WORKING_MESSAGE);
    }

    public void timerExpired() {
        view.setMessage(TIME_EXPIRED_MESSAGE);
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
}

