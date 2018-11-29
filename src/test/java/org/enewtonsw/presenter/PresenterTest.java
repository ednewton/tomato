package org.enewtonsw.presenter;

import org.enewtonsw.model.Model;
import org.enewtonsw.model.State;
import org.enewtonsw.view.View;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        assertEquals(State.WORKING, model.getCurrentState());
        verify(view).setTime(Presenter.WORK_TIME);
        verify(view).setMessage(Presenter.WORKING_MESSAGE);
    }

    @Test
    public void workTimerExpired() throws Exception {
        assertTimerExpired(State.WORKING);
    }

    public void assertTimerExpired(State working) {
        model.setCurrentState(working);

        presenter.timerExpired();

        verify(view).setMessage(String.format(Presenter.TIME_EXPIRED_MESSAGE, working));
        assertEquals(State.ALARMING, model.getCurrentState());
    }

    @Test
    public void shortBreakTimerExpired() throws Exception {
        assertTimerExpired(State.BREAKING);
    }

    @Test
    public void longBreakTimerExpired() throws Exception {
        assertTimerExpired(State.BREAKING);
    }

    @Test
    public void takeShortBreaks() throws Exception {
        for (int i = 1; i < Presenter.MAX_SHORT_BREAKS + 1; i++) {
            presenter.takeAShortBreak();

            assertEquals(State.BREAKING, model.getCurrentState());

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

        assertEquals(State.SNOOZING, model.getCurrentState());
        verify(view).setTime(Presenter.SNOOZE_TIME);
        verify(view).setMessage(Presenter.SNOOZING_MESSAGE);
    }

    @Test
    public void takeALongBreak() throws Exception {
        model.setBreakCount(3);

        presenter.takeALongBreak();

        assertEquals(State.BREAKING, model.getCurrentState());

        assertEquals(0, model.getBreakCount());
        verify(view).setShortBreakIndicator(0);
        verify(view).setTime(Presenter.LONG_BREAK);
        verify(view).setMessage(Presenter.LONG_BREAK_MESSAGE);
    }

    @Test
    public void testReset() throws Exception {
        model.setBreakCount(3);
        presenter.reset();

        assertEquals(State.IDLE, model.getCurrentState());
        assertEquals(0, model.getBreakCount());

        verify(view).reset();
        verify(view).setMessage(Presenter.RESET_MESSAGE);
    }

    @Test
    public void testAddMinute() throws Exception {
        when(view.getTime()).thenReturn(2333L);
        presenter.addMinute();

        verify(view).setTime(62333L);
    }

    @Test
    public void testCannotAddAnotherMinute() throws Exception {
        when(view.getTime()).thenReturn(Presenter.FIFTY_NINE_MINUTES_FIFTY_NINE_SECONDS);

        try {
            presenter.addMinute();
            fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void testUpperLimitAddMinute() throws Exception {
        when(view.getTime()).thenReturn(Presenter.FIFTY_EIGHT_MINUTES_FIFTY_NINE_SECONDS);

        presenter.addMinute();

        verify(view).setTime(Presenter.FIFTY_NINE_MINUTES_FIFTY_NINE_SECONDS);
    }

    @Test
    public void testCannotSubtractAnotherMinute() throws Exception {
        when(view.getTime()).thenReturn(Presenter.SIXTY_SECONDS - 1000L);

        try {
            presenter.subtractMinute();
            fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void testSubtractMinute() throws Exception {
        when(view.getTime()).thenReturn(Presenter.SIXTY_SECONDS);

        presenter.subtractMinute();

        verify(view).setTime(0);
    }

    @Test
    public void testAcknowledgeAfterWorkingTimerExpiredStartFirstShortBreak() {
        model.setCurrentState(State.WORKING);
        model.setBreakCount(0);

        presenter.acknowledge();

        assertEquals(State.BREAKING, model.getCurrentState());
        assertEquals(1, model.getBreakCount());
        verify(view).setTime(Presenter.SHORT_BREAK);
        verify(view).setShortBreakIndicator(1);
        verify(view).setMessage(Presenter.SHORT_BREAK_MESSAGE);
    }

    @Test
    public void testAcknowledgeAfterWorkingTimerExpiredStartSecondShortBreak() {
        model.setCurrentState(State.WORKING);
        model.setBreakCount(1);

        presenter.acknowledge();

        assertEquals(State.BREAKING, model.getCurrentState());
        assertEquals(2, model.getBreakCount());
        verify(view).setTime(Presenter.SHORT_BREAK);
        verify(view).setShortBreakIndicator(2);
        verify(view).setMessage(Presenter.SHORT_BREAK_MESSAGE);
    }

    @Test
    public void testAcknowledgeAfterWorkingTimerExpiredStartThirdShortBreak() {
        model.setCurrentState(State.WORKING);
        model.setBreakCount(2);

        presenter.acknowledge();

        assertEquals(State.BREAKING, model.getCurrentState());
        assertEquals(3, model.getBreakCount());
        verify(view).setTime(Presenter.SHORT_BREAK);
        verify(view).setShortBreakIndicator(3);
        verify(view).setMessage(Presenter.SHORT_BREAK_MESSAGE);
    }

    @Test
    public void testAcknowledgeAfterWorkingTimerExpiredStartFourthShortBreak() {
        model.setCurrentState(State.WORKING);
        model.setBreakCount(3);

        presenter.acknowledge();

        assertEquals(State.BREAKING, model.getCurrentState());
        assertEquals(4, model.getBreakCount());
        verify(view).setTime(Presenter.SHORT_BREAK);
        verify(view).setShortBreakIndicator(4);
        verify(view).setMessage(Presenter.SHORT_BREAK_MESSAGE);
    }

    @Test
    public void testAcknowledgeAfterWorkingTimerExpiredStartLongBreak() {
        model.setCurrentState(State.WORKING);
        model.setBreakCount(4);

        presenter.acknowledge();

        assertEquals(State.BREAKING, model.getCurrentState());
        assertEquals(0, model.getBreakCount());
        verify(view).setTime(Presenter.LONG_BREAK);
        verify(view).setShortBreakIndicator(0);
        verify(view).setMessage(Presenter.LONG_BREAK_MESSAGE);
    }

    @Test
    public void testAcknowledgeAfterShortBreakTimerExpiredStartWorking() {
        model.setCurrentState(State.BREAKING);

        presenter.acknowledge();

        assertEquals(State.WORKING, model.getCurrentState());
        verify(view).setTime(Presenter.WORK_TIME);
        verify(view).setMessage(Presenter.WORKING_MESSAGE);
    }
}