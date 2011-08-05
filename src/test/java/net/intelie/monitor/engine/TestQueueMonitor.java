package net.intelie.monitor.engine;

import junit.framework.TestCase;
import net.intelie.monitor.events.QueueNotFound;
import net.intelie.monitor.events.QueueStoppedConsuming;
import net.intelie.monitor.events.ServerUnavailable;

import static org.mockito.Mockito.*;


public class TestQueueMonitor extends TestCase {
    public void testWillNotNotifyIfReceivedOnlyOneEvent() throws QueueNotFound, ServerUnavailable, QueueStoppedConsuming {
        EngineChecker checker = mock(EngineChecker.class);
        when(checker.getDequeueCount("myQueue")).thenReturn(new Long(42));

        QueueMonitor monitor = new QueueMonitor("myQueue");
        monitor.check(checker);

        verify(checker, times(1)).getDequeueCount("myQueue");
    }

    public void testWillNotNotifyIfReceivedThreeDequeuesOfSameValue() throws QueueNotFound, QueueStoppedConsuming {
        EngineChecker checker = mock(EngineChecker.class);
        when(checker.getDequeueCount("myQueue")).thenReturn(new Long(42));

        QueueMonitor monitor = new QueueMonitor("myQueue");
        monitor.check(checker);
        monitor.check(checker);
        try {
            monitor.check(checker);
            fail("should've throw exception");
        } catch (QueueStoppedConsuming e) {
        }

        verify(checker, times(3)).getDequeueCount("myQueue");
    }

    public void testWillNotNotifyIfReceivedThreeDequeuesOfDifferentValues() throws QueueNotFound, QueueStoppedConsuming {
        EngineChecker checker = mock(EngineChecker.class);
        when(checker.getDequeueCount("myQueue")).thenReturn(new Long(42), new Long(43), new Long(44));

        QueueMonitor monitor = new QueueMonitor("myQueue");
        monitor.check(checker);
        monitor.check(checker);
        monitor.check(checker);

        verify(checker, times(3)).getDequeueCount("myQueue");
    }


    public void testWillBubbleUpIfQueueWasNotFound() throws QueueNotFound, QueueStoppedConsuming {
        EngineChecker checker = mock(EngineChecker.class);
        when(checker.getDequeueCount("myQueue")).thenThrow(new QueueNotFound("myQueue", new Exception()));

        QueueMonitor monitor = new QueueMonitor("myQueue");
        try {
            monitor.check(checker);
            fail("should've throw exception");
        } catch (QueueNotFound e) {
        }

        verify(checker, times(1)).getDequeueCount("myQueue");
    }
}

