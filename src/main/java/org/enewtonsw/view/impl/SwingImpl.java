package org.enewtonsw.view.impl;

import org.enewtonsw.presenter.Presenter;
import org.enewtonsw.view.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class SwingImpl implements View {
    private Presenter presenter;
    private long timeLeft;
    private Timer timer;
    private JLabel timerDisplay;
    private JLabel statusLabel;

    public SwingImpl() {
        createUI();
    }

    private void createUI() {
        JButton startWorkButton = new JButton("Work");
        startWorkButton.addActionListener((ActionEvent e) -> {
            presenter.startTimer();
        });

        JButton shortBreakButton = new JButton("Short");
        shortBreakButton.addActionListener((ActionEvent e) -> {
            presenter.takeAShortBreak();
        });

        JButton longBreakButton = new JButton("Long");
        longBreakButton.addActionListener((ActionEvent e) -> {
            presenter.takeALongBreak();
        });

        JPanel panel = new JPanel(new GridLayout(3,1));
        panel.add(startWorkButton);
        panel.add(shortBreakButton);
        panel.add(longBreakButton);
        statusLabel = new JLabel("Status");
        panel.add(statusLabel);
        timerDisplay = new JLabel("00:00");
        panel.add(timerDisplay);

        JFrame frame = new JFrame("Passive MVP Swing");
        ((JPanel) frame.getContentPane()).setBackground(Color.white);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    @Override
    public void setPresenter(Presenter pres) {
        presenter = pres;
    }

    @Override
    public void setTime(long time) {
        if (timer != null) {
            timer.stop();
            timer = null;
        }

        timeLeft = time;

        DateFormat sdf = new SimpleDateFormat("mm:ss");
        timerDisplay.setText(sdf.format(timeLeft));

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft -= 1000;
                timerDisplay.setText(sdf.format(timeLeft));

                if (timeLeft <= 0) {
                    timer.stop();
                    presenter.timerExpired();
                }
            }
        });

        timer.start();
    }

    @Override
    public void setMessage(String message) {
        statusLabel.setText(message);
    }
}
