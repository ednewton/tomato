package org.enewtonsw.view.impl;

import org.enewtonsw.App;
import org.enewtonsw.presenter.Presenter;
import org.enewtonsw.view.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
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

        ImageIcon shortBreakCountImage = new ImageIcon(getImage("image/sbc0.png"), "Short Break Count");
        JLabel shortBreakCountLabel = new JLabel(shortBreakCountImage);

        JButton longBreakButton = new JButton("Long");
        longBreakButton.addActionListener((ActionEvent e) -> {
            presenter.takeALongBreak();
        });

        JPanel panel = new JPanel();
        panel.add(startWorkButton);
        panel.add(shortBreakButton);
        panel.add(shortBreakCountLabel);
        panel.add(longBreakButton);
        statusLabel = new JLabel("Status");
        panel.add(statusLabel);
        timerDisplay = new JLabel("00:00");
        panel.add(timerDisplay);

        JFrame frame = new JFrame("Passive MVP Swing");
        ((JPanel) frame.getContentPane()).setBackground(Color.white);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public static Image getImage(final String pathAndFileName) {
        final URL url = Thread.currentThread().getContextClassLoader().getResource(pathAndFileName);
        return Toolkit.getDefaultToolkit().getImage(url);
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
        switch (count) {
            case 0:

                break;
        }
    }
}
