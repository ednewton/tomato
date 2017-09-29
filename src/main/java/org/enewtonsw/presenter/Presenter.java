package org.enewtonsw.presenter;

import org.enewtonsw.model.Model;
import org.enewtonsw.view.View;

public class Presenter {

    private View view;
    private Model model;

    public Presenter(View view, Model model) {
        this.view = view;
        this.model = model;
    }

    public void startTimer() {
        view.setTime(25 * 60 * 1000);
    }

    public void timerExpired() {
        view.setMessage("Time Expired!");
    }
}

