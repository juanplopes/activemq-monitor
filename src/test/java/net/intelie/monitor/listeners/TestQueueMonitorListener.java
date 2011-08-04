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

        QueueMonitorListener listener = new QueueMonitorListener("company", notifier, 0);
        listener.notify(event);

        verify(notifier).send("[ERRO][company] error", "error");
    }

    public void testWillNotThrowExceptionIfCompanyIsNull() {
        Notifier notifier = mock(Notifier.class);
        Event event = mock(Event.class);
        when(event.getMessage()).thenReturn("error");

        QueueMonitorListener listener = new QueueMonitorListener(null, notifier, 0);
        listener.notify(event);

        verify(notifier).send("[ERRO][null] error", "error");
    }

    public void testWillNotNotifyTwiceIfInsideInterval() {
        Notifier notifier = mock(Notifier.class);
        Event event = mock(Event.class);
        when(event.getMessage()).thenReturn("error");

        QueueMonitorListener listener = new QueueMonitorListener("company", notifier, 15000);
        listener.notify(event, 0);
        listener.notify(event, 14000);
        listener.notify(event, 15000);

        verify(notifier, times(2)).send("[ERRO][company] error", "error");
    }

    public void testWillNotifyEveryTimeIfIntervalIsZero() {
        Notifier notifier = mock(Notifier.class);
        Event event = mock(Event.class);
        when(event.getMessage()).thenReturn("error");

        QueueMonitorListener listener = new QueueMonitorListener("company", notifier, 0);
        listener.notify(event, 0);
        listener.notify(event, 14000);
        listener.notify(event, 15000);

        verify(notifier, times(3)).send("[ERRO][company] error", "error");
    }
}
