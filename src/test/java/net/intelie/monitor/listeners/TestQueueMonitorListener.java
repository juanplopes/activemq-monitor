package net.intelie.monitor.listeners;

import junit.framework.TestCase;
import net.intelie.monitor.events.Event;
import net.intelie.monitor.notifiers.Notifier;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

public class TestQueueMonitorListener extends TestCase {
    public void testSendingEventAndNotifying() {
        Notifier notifier = mock(Notifier.class);
        Event event = mock(Event.class);
        when(event.getMessage()).thenReturn("error");
        when(event.getDetail()).thenReturn("error2");

        QueueMonitorListener listener = new QueueMonitorListener("company", notifier, 0);
        listener.notify(event);

        verify(notifier).send("[ERRO][company] error", "error2");
    }

    public void testWillNotThrowExceptionIfCompanyIsNull() {
        Notifier notifier = mock(Notifier.class);
        Event event = mock(Event.class);
        when(event.getMessage()).thenReturn("error");
        when(event.getDetail()).thenReturn("error2");

        QueueMonitorListener listener = new QueueMonitorListener(null, notifier, 0);
        listener.notify(event);

        verify(notifier).send("[ERRO][null] error", "error2");
    }

    public void testWillNotNotifyTwiceIfInsideInterval() {
        Notifier notifier = mock(Notifier.class);
        Event event = mock(Event.class);
        when(event.getMessage()).thenReturn("error");
        when(event.getDetail()).thenReturn("error2");

        QueueMonitorListener listener = new QueueMonitorListener("company", notifier, 15);
        listener.notify(event, 0);
        listener.notify(event, 14*60*1000);
        listener.notify(event, 15*60*1000);

        verify(notifier, times(2)).send("[ERRO][company] error", "error2");
    }

    public void testWillNotifyEveryTimeIfIntervalIsZero() {
        Notifier notifier = mock(Notifier.class);
        Event event = mock(Event.class);
        when(event.getMessage()).thenReturn("error");
        when(event.getDetail()).thenReturn("error2");

        QueueMonitorListener listener = new QueueMonitorListener("company", notifier, 0);
        listener.notify(event, 0);
        listener.notify(event, 14*60*1000);
        listener.notify(event, 15*60*1000);

        verify(notifier, times(3)).send("[ERRO][company] error", "error2");
    }
}
