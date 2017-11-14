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
    public void startWork() throws Exception {
        presenter.startWork();

        verify(view).setTime(Presenter.WORK_TIME);
        verify(view).setMessage(Presenter.WORKING_MESSAGE);
    }

    @Test
    public void workTimerExpired() throws Exception {
        presenter.timerExpired("Work");

        verify(view).setMessage(String.format(Presenter.TIME_EXPIRED_MESSAGE, "Work"));
    }

    @Test
    public void shortBreakTimerExpired() throws Exception {
        presenter.timerExpired("Short Break");

        verify(view).setMessage(String.format(Presenter.TIME_EXPIRED_MESSAGE, "Short Break"));
    }

    @Test
    public void takeShortBreaks() throws Exception {
        for (int i = 1; i < Presenter.MAX_SHORT_BREAKS + 1; i++) {
            presenter.takeAShortBreak();

            assertEquals(i, model.getBreakCount());
            verify(view).setShortBreakIndicator(i);
            verify(view).setTime(Presenter.SHORT_BREAK);
            verify(view).setMessage(Presenter.SHORT_BREAK_MESSAGE);

            Mockito.reset(view);
        }

        presenter.takeAShortBreak();
        assertEquals(0, model.getBreakCount());

        verify(view).setShortBreakIndicator(0);
        verify(view).setTime(Presenter.SHORT_BREAK);
    }

    @Test
    public void testSnooze() throws Exception {
        presenter.snooze();

        verify(view).setTime(Presenter.SNOOZE_TIME);
        verify(view).setMessage(Presenter.SNOOZING_MESSAGE);
    }

    @Test
    public void takeALongBreak() throws Exception {
        model.setBreakCount(3);

        presenter.takeALongBreak();

        assertEquals(0, model.getBreakCount());
        verify(view).setShortBreakIndicator(0);
        verify(view).setTime(Presenter.LONG_BREAK);
        verify(view).setMessage(Presenter.LONG_BREAK_MESSAGE);
    }

    @Test
    public void testReset() throws Exception {
        model.setBreakCount(3);
        presenter.reset();
        assertEquals(0, model.getBreakCount());

        verify(view).reset();
        verify(view).setMessage(Presenter.RESET_MESSAGE);
    }
}