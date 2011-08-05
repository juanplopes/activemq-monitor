package net.intelie.monitor.engine;

import junit.framework.TestCase;
import net.intelie.monitor.events.*;

import static org.mockito.Mockito.*;

public class TestQueueCollection extends TestCase {
    public void testEmptyQueueNamesWillCauseNoUpdateToBeDone() throws CompositeEvent, QueueNotFound, ServerUnavailable {
        EngineChecker checker = mock(EngineChecker.class);

        QueueCollection collection = new QueueCollection(new String[0]);
        collection.checkAll(checker);

        verify(checker, times(0)).getDequeueCount(anyString());
    }

    public void testSingleQueueWillCauseOneOfEachMethodToBeCalled() throws CompositeEvent, QueueNotFound, ServerUnavailable {
        EngineChecker checker = mock(EngineChecker.class);

        QueueCollection collection = new QueueCollection(new String[]{"abc"});
        collection.checkAll(checker);

        verify(checker, times(1)).getDequeueCount("abc");
    }

    public void testTwoQueuesWillCauseTwoCalls() throws CompositeEvent, QueueNotFound, ServerUnavailable {
        EngineChecker checker = mock(EngineChecker.class);

        QueueCollection collection = new QueueCollection(new String[]{"abc", "qwe"});
        collection.checkAll(checker);

        verify(checker, times(1)).getDequeueCount("abc");
        verify(checker, times(1)).getDequeueCount("qwe");
    }

    public void testWillContinueToRunEvenIfOneOfThemThrowsEvent() throws CompositeEvent, QueueNotFound, ServerUnavailable {
        EngineChecker checker = mock(EngineChecker.class);
        when(checker.getDequeueCount("abc")).thenThrow(new QueueNotFound("abc", new Exception()));

        try {
            QueueCollection collection = new QueueCollection(new String[]{"abc", "qwe"});
            collection.checkAll(checker);
            fail("should throw exception'");

        } catch (CompositeEvent e) {
            assertEquals(1, e.getEvents().size());
            assertEquals(QueueNotFound.class, e.getEvents().get(0).getClass());
        }

        verify(checker, times(1)).getDequeueCount("abc");
        verify(checker, times(1)).getDequeueCount("qwe");
    }

     public void testWillContinueToRunEvenIfOneOfThemThrowsRuntimeException() throws CompositeEvent, QueueNotFound, ServerUnavailable {
        EngineChecker checker = mock(EngineChecker.class);
        when(checker.getDequeueCount("abc")).thenThrow(new RuntimeException("abc"));

        try {
            QueueCollection collection = new QueueCollection(new String[]{"abc", "qwe"});
            collection.checkAll(checker);
            fail("should throw exception'");
        } catch (CompositeEvent e) {
            assertEquals(1, e.getEvents().size());
            Event event = e.getEvents().get(0);
            assertEquals(UnhandledEvent.class, event.getClass());
            assertEquals("Unhandled exception: java.lang.RuntimeException", event.getMessage());
        }

        verify(checker, times(1)).getDequeueCount("abc");
        verify(checker, times(1)).getDequeueCount("qwe");
    }

    public void testWillCollectAllErrorsWhenThrowByMultipleQueues() throws CompositeEvent, QueueNotFound, ServerUnavailable {
        EngineChecker checker = mock(EngineChecker.class);
        when(checker.getDequeueCount("abc")).thenThrow(new RuntimeException("abc"));
        when(checker.getDequeueCount("qwe")).thenThrow(new QueueNotFound("qwe", new Exception()));

        try {
            QueueCollection collection = new QueueCollection(new String[]{"abc", "qwe", "zxc"});
            collection.checkAll(checker);
            fail("should throw exception'");
        } catch (CompositeEvent e) {
            assertEquals(2, e.getEvents().size());
            assertEquals(UnhandledEvent.class, e.getEvents().get(0).getClass());
            assertEquals(QueueNotFound.class, e.getEvents().get(1).getClass());
        }

        verify(checker, times(1)).getDequeueCount("abc");
        verify(checker, times(1)).getDequeueCount("qwe");
        verify(checker, times(1)).getDequeueCount("zxc");
    }
}
