package org.enewtonsw.view;

import org.enewtonsw.presenter.Presenter;

public interface View {
    void setPresenter(Presenter pres);

    void setMessage(String message);

    void setShortBreakIndicator(int count);

    void reset();

    void disableShortBreakButton();

    void enableShortBreakButton();

    void setVersion(String version);

    long getTime();

    void setTime(long time);

    void setAcknowledgeButtonText(String text);
}
