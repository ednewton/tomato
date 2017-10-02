package org.enewtonsw.presenter;

import org.enewtonsw.model.Model;
import org.enewtonsw.view.View;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PresenterTest {

    @Mock
    private View view;
    private Presenter presenter;
    private Model model;

    @Before
    public void setUp() throws Exception {
        model = new Model();
        presenter = new Presenter(view, model);
    }

    @Test
    public void startTimer() throws Exception {
        presenter.startTimer();

        verify(view).setTime(Presenter.WORK_TIME);
    }

    @Test
    public void timerExpired() throws Exception {
        presenter.timerExpired();

        verify(view).setMessage("Time Expired!");
    }

    @Test
    public void takeAShortBreak() throws Exception {
        presenter.takeAShortBreak();

        verify(view).setTime(Presenter.SHORT_BREAK);
    }

    @Test
    public void takeALongBreak() throws Exception {
        model.setBreakCount(3);

        presenter.takeALongBreak();

        assertEquals(0, model.getBreakCount());
        verify(view).setShortBreakIndicator(0);
        verify(view).setTime(Presenter.LONG_BREAK);
    }
}