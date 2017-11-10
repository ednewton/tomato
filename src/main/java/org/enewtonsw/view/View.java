package org.enewtonsw.view;

import org.enewtonsw.presenter.Presenter;

public interface View {
    void setPresenter(Presenter pres);

    void setTime(long time);

    void setMessage(String message);

    void setShortBreakIndicator(int count);

    void reset();

    void setVersion(String version);
}
