package org.enewtonsw.view.impl;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
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
    public static final String TIMER_EXPIRED_MESSAGE = "The timer has expired!";
    public static final String TIMER_EXPIRED_TITLE = "Timer Expired";
    public static final String ACK_BUTTON_TEXT = "Acknowledge";
    public static final String SNOOZE_BUTTON_TEXT = "Snooze for 5 minutes";
    public static final String SHORT_BREAK_COUNT_IMAGE_DESC = "Short Break Count";
    public static final int SNOOZE = 1;
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
    private long timeLeft;
    private Timer timer;

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
        frame.setAlwaysOnTop(true);
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

        if (timeLeft == 0)
            return;

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft -= 1000;
                timerLabel.setText(sdf.format(timeLeft));

                if (timeLeft <= 0) {
                    timer.stop();

                    presenter.timerExpired();
                    int ackOrSnooze = showDialog();

                    if (ackOrSnooze == SNOOZE) {
                        setTime(Presenter.SNOOZE_TIME);
                    }

                    presenter.stopAudio();
                }
            }
        });

        timer.start();
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

    private int showDialog() {
        Object[] options = {ACK_BUTTON_TEXT,
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
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(buttonPanel, BorderLayout.WEST);
        buttonPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Controls"));
        gbPanel = new JPanel();
        gbPanel.setLayout(new GridBagLayout());
        buttonPanel.add(gbPanel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        workButton = new JButton();
        workButton.setText("Work");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbPanel.add(workButton, gbc);
        shortBreakButton = new JButton();
        shortBreakButton.setText("Short Break");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbPanel.add(shortBreakButton, gbc);
        longBreakButton = new JButton();
        longBreakButton.setText("Long Break");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbPanel.add(longBreakButton, gbc);
        resetButton = new JButton();
        resetButton.setText("Reset");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbPanel.add(resetButton, gbc);
        shortBreakCountLabel = new JLabel();
        shortBreakCountLabel.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbPanel.add(shortBreakCountLabel, gbc);
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
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
