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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        timerPanel = new JPanel();
        timerPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.add(timerPanel, BorderLayout.CENTER);
        timerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Timer"));
        timerLabel = new JLabel();
        Font timerLabelFont = this.$$$getFont$$$(null, -1, 72, timerLabel.getFont());
        if (timerLabelFont != null) timerLabel.setFont(timerLabelFont);
        timerLabel.setText("00:00");
        timerLabel.setVerticalAlignment(0);
        timerPanel.add(timerLabel, BorderLayout.CENTER);
        versionLabel = new JLabel();
        versionLabel.setHorizontalAlignment(4);
        versionLabel.setText("Label");
        timerPanel.add(versionLabel, BorderLayout.SOUTH);
        incrDecrMinutePanel = new JPanel();
        incrDecrMinutePanel.setLayout(new GridBagLayout());
        timerPanel.add(incrDecrMinutePanel, BorderLayout.EAST);
        incrDecrButtonPanel = new JPanel();
        incrDecrButtonPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 5, 5, 5), -1, -1));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        incrDecrMinutePanel.add(incrDecrButtonPanel, gbc);
        incrDecrButtonPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Minute", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, -1, -1, incrDecrButtonPanel.getFont())));
        addMinuteButton = new JButton();
        addMinuteButton.setText("+");
        addMinuteButton.setToolTipText("Add a minute to the timer");
        incrDecrButtonPanel.add(addMinuteButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(1, 1), null, null, 0, false));
        subtractMinuteButton = new JButton();
        subtractMinuteButton.setText("-");
        subtractMinuteButton.setToolTipText("Subtract a minute from the timer");
        incrDecrButtonPanel.add(subtractMinuteButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(1, 1), null, null, 0, false));
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(buttonPanel, BorderLayout.WEST);
        buttonPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Controls"));
        gbPanel = new JPanel();
        gbPanel.setLayout(new GridBagLayout());
        buttonPanel.add(gbPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        workButton = new JButton();
        workButton.setText("Work");
        workButton.setToolTipText("Start a work session");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbPanel.add(workButton, gbc);
        shortBreakButton = new JButton();
        shortBreakButton.setText("Short Break");
        shortBreakButton.setToolTipText("Take a short break");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbPanel.add(shortBreakButton, gbc);
        longBreakButton = new JButton();
        longBreakButton.setText("Long Break");
        longBreakButton.setToolTipText("Take a long break");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbPanel.add(longBreakButton, gbc);
        resetButton = new JButton();
        resetButton.setText("Reset");
        resetButton.setToolTipText("Reset the timer");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbPanel.add(resetButton, gbc);
        shortBreakCountLabel = new JLabel();
        shortBreakCountLabel.setText("");
        shortBreakCountLabel.setToolTipText("How many short breaks you've taken");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbPanel.add(shortBreakCountLabel, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbPanel.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbPanel.add(spacer2, gbc);
        statusPanel = new JPanel();
        statusPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setBorder(BorderFactory.createTitledBorder("Status"));
        statusInnerPanel = new JPanel();
        statusInnerPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 5, 0, 0), -1, -1));
        statusPanel.add(statusInnerPanel, BorderLayout.WEST);
        statusMessage = new JLabel();
        statusMessage.setHorizontalAlignment(2);
        statusMessage.setHorizontalTextPosition(11);
        statusMessage.setText("Idle");
        statusInnerPanel.add(statusMessage, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
