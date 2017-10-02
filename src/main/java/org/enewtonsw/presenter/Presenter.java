package org.enewtonsw.presenter;

import org.enewtonsw.model.Model;
import org.enewtonsw.view.View;

public class Presenter {
    public static final int SHORT_BREAK = 5 * 60 * 1000;
    public static final int WORK_TIME = 25 * 60 * 1000;
    public static final int LONG_BREAK = 15 * 60 * 1000;

    private View view;
    private Model model;

    public Presenter(View view, Model model) {
        this.view = view;
        this.model = model;
    }

    public void startTimer() {
        view.setTime(WORK_TIME);
    }

    public void timerExpired() {
        view.setMessage("Time Expired!");
    }

    public void takeABreak() {
        if (model.getBreakCount() < 4) {
            view.setTime(SHORT_BREAK);
            model.setBreakCount(model.getBreakCount() + 1);
        } else {
            view.setTime(LONG_BREAK);
            model.setBreakCount(0);
        }
    }

    public int getBreakCount() {
        return model.getBreakCount();
    }

    public void takeAShortBreak() {
        view.setTime(SHORT_BREAK);
    }

    public void takeALongBreak() {
        model.setBreakCount(0);
        view.setShortBreakIndicator(0);
        view.setTime(LONG_BREAK);
    }
}

