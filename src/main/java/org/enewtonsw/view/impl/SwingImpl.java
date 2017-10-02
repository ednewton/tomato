package org.enewtonsw.view.impl;

import org.enewtonsw.presenter.Presenter;
import org.enewtonsw.view.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class SwingImpl implements View {
    private Presenter presenter;
    private long timeLeft;
    private Timer timer;
    private JLabel timerDisplay;
    private JLabel statusLabel;
    private JLabel shortBreakCountLabel;

    public SwingImpl() {
        createUI();
    }

    public static Image getImage(final String pathAndFileName) {
        final URL url = Thread.currentThread().getContextClassLoader().getResource(pathAndFileName);
        return Toolkit.getDefaultToolkit().getImage(url);
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

        ImageIcon shortBreakCountImage = getShortBreakIndicatorImage(0);
        shortBreakCountLabel = new JLabel(shortBreakCountImage);

        JButton longBreakButton = new JButton("Long");
        longBreakButton.addActionListener((ActionEvent e) -> {
            presenter.takeALongBreak();
        });

        JPanel timerPanel = new JPanel();
        timerDisplay = new JLabel("00:00");
        timerPanel.add(timerDisplay);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1));
        buttonPanel.add(startWorkButton);
        buttonPanel.add(shortBreakButton);

        JPanel shortBreakCountPanel = new JPanel();
        shortBreakCountPanel.setLayout(new BorderLayout());
        shortBreakCountPanel.add(shortBreakCountLabel, BorderLayout.CENTER);
        buttonPanel.add(shortBreakCountPanel);
        buttonPanel.add(longBreakButton);
        statusLabel = new JLabel("Status");
        buttonPanel.add(statusLabel);

        JFrame frame = new JFrame("Passive MVP Swing");
        ((JPanel) frame.getContentPane()).setBackground(Color.white);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(timerPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setSize(200, 300);
        frame.setResizable(false);
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

    @Override
    public void setShortBreakIndicator(int count) {
        shortBreakCountLabel.setIcon(getShortBreakIndicatorImage(count));
    }

    private ImageIcon getShortBreakIndicatorImage(int i) {
        return new ImageIcon(getImage("image/sbc" +
                i +
                ".png"), "Short Break Count");
    }
}
