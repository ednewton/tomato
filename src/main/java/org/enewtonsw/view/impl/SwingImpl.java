package org.enewtonsw.view.impl;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.enewtonsw.presenter.Presenter;
import org.enewtonsw.view.View;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class SwingImpl implements View {
    private static final String TIMER_EXPIRED_MESSAGE = "The timer has expired!";
    private static final String TIMER_EXPIRED_TITLE = "Timer Expired";
    private static final String SNOOZE_BUTTON_TEXT = "Snooze for 5 minutes";
    private static final String SHORT_BREAK_COUNT_IMAGE_DESC = "Short Break Count";
    private static final int SNOOZE = 1;
    private final JFrame frame;
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
    private JLabel versionLabel;
    private JPanel incrDecrMinutePanel;
    private JPanel incrDecrButtonPanel;
    private JButton addMinuteButton;
    private JButton subtractMinuteButton;
    private long timeLeft;
    private Timer timer;
    private String acknowledgementButtonText;

    public SwingImpl() {
        workButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.startWork();
            }
        });

        shortBreakButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.takeAShortBreak();
            }
        });

        longBreakButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.takeALongBreak();
            }
        });

        addMinuteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    presenter.addMinute();
                } catch (Exception e1) {
                    // ignore
                }
            }
        });

        subtractMinuteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    presenter.subtractMinute();
                } catch (Exception e1) {
                    // ignore
                }
            }
        });

        ImageIcon shortBreakCountImage = getShortBreakIndicatorImage(0);
        shortBreakCountLabel.setIcon(shortBreakCountImage);

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.reset();
            }
        });

        frame = new JFrame("Tomato");
        frame.setAlwaysOnTop(true);
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
    public void setMessage(String message) {
        statusMessage.setText(message);
    }

    @Override
    public void setShortBreakIndicator(int count) {
        shortBreakCountLabel.setIcon(getShortBreakIndicatorImage(count));
    }

    @Override
    public void reset() {
        setTime(0);
        setShortBreakIndicator(0);
    }

    @Override
    public void disableShortBreakButton() {
        shortBreakButton.setEnabled(false);
    }

    @Override
    public void enableShortBreakButton() {
        shortBreakButton.setEnabled(true);
    }

    @Override
    public void setVersion(String version) {
        versionLabel.setText("v" + version);
    }

    @Override
    public long getTime() {
        return timeLeft;
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

        if (timeLeft == 0)
            return;

        long startTimeInMillis = System.currentTimeMillis();

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long elapsedMillis = System.currentTimeMillis() - startTimeInMillis;

                timeLeft = time - elapsedMillis;

                if (timeLeft < 0) {
                    timeLeft = 0;
                }

                timerLabel.setText(sdf.format(timeLeft));

                if (timeLeft == 0) {
                    timer.stop();

                    presenter.timerExpired();
                    int ackOrSnooze = showDialog();

                    if (ackOrSnooze == SNOOZE) {
                        presenter.snooze();
                    } else {
                        setMessage(Presenter.ACK_MESSAGE);
                        presenter.acknowledge();
                    }

                    presenter.stopAudio();
                }
            }
        });

        timer.start();
    }

    @Override
    public void setAcknowledgeButtonText(String text) {
        acknowledgementButtonText = text;
    }

    private int showDialog() {
        Object[] options = {acknowledgementButtonText,
                SNOOZE_BUTTON_TEXT};

        return JOptionPane.showOptionDialog(frame,
                TIMER_EXPIRED_MESSAGE,
                TIMER_EXPIRED_TITLE,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[0]); //default button title
    }

    private ImageIcon getShortBreakIndicatorImage(int i) {
        return new ImageIcon(getImage("image/sbc" +
                i +
                ".png"), SHORT_BREAK_COUNT_IMAGE_DESC);
    }

    private Image getImage(final String pathAndFileName) {
        final URL url = Thread.currentThread().getContextClassLoader().getResource(pathAndFileName);
        return Toolkit.getDefaultToolkit().getImage(url);
    }

}
