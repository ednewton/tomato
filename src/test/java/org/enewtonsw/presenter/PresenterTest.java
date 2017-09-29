package org.enewtonsw.presenter;

import org.enewtonsw.model.Model;
import org.enewtonsw.view.View;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PresenterTest {
    @Mock
    private View view;

    @Test
    public void startTimer() throws Exception {
        Presenter presenter = new Presenter(view, new Model());
        presenter.startTimer();

        Mockito.verify(view).setTime(25 * 60 * 1000);
    }

    @Test
    public void timerExpired() throws Exception {
        Presenter presenter = new Presenter(view, new Model());
        presenter.timerExpired();

        Mockito.verify(view).setMessage("Time Expired!");
    }
}