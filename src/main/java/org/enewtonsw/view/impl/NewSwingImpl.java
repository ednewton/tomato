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

public class NewSwingImpl implements View {
    private Presenter presenter;
    private JPanel mainPanel;
    private JLabel timerLabel;
    private JPanel timerPanel;
    private JPanel buttonPanel;
    private JButton workButton;
    private JButton shortBreakButton;
    private JButton longBreakButton;
    private JButton resetButton;
    private JLabel shortBreakCountLabel;
    private JPanel gbPanel;
    private JPanel statusPanel;
    private JLabel statusMessage;
    private JPanel statusInnerPanel;
    private long timeLeft;
    private Timer timer;

    public NewSwingImpl() {
        workButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.startTimer();
            }
        });

        JFrame frame = new JFrame("Tomato");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setContentPane(mainPanel);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setTime(long time) {
        if (timer != null) {
            timer.stop();
            timer = null;
        }

        timeLeft = time;

        DateFormat sdf = new SimpleDateFormat("mm:ss");
        timerLabel.setText(sdf.format(timeLeft));

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft -= 1000;
                timerLabel.setText(sdf.format(timeLeft));

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

    }

    @Override
    public void setShortBreakIndicator(int count) {

    }

    private ImageIcon getShortBreakIndicatorImage(int i) {
        return new ImageIcon(getImage("image/sbc" +
                i +
                ".png"), "Short Break Count");
    }

    private Image getImage(final String pathAndFileName) {
        final URL url = Thread.currentThread().getContextClassLoader().getResource(pathAndFileName);
        return Toolkit.getDefaultToolkit().getImage(url);
    }
}
