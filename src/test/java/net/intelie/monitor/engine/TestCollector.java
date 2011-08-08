package net.intelie.monitor.engine;

import junit.framework.TestCase;
import net.intelie.monitor.events.CompositeEvent;
import net.intelie.monitor.events.Event;
import net.intelie.monitor.events.ServerUnavailable;
import net.intelie.monitor.listeners.Listener;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class TestCollector extends TestCase {
    public void testWillCallQueueCollectionCheckAll() throws CompositeEvent, ServerUnavailable {
        QueueCollection collection = mock(QueueCollection.class);
        Listener listener = mock(Listener.class);
        EngineChecker engine = mock(EngineChecker.class);

        Collector collector = new Collector(engine, listener, collection);
        collector.checkAll();

        verify(collection).checkAll(engine);
        verify(engine).connect();
        verify(engine).disconnect();
        verifyZeroInteractions(listener);
        verifyNoMoreInteractions(engine);
    }

    public void testWillNotifyListenerIfCollectionThrowsException() throws CompositeEvent, ServerUnavailable {
        QueueCollection collection = mock(QueueCollection.class);
        Listener listener = mock(Listener.class);
        EngineChecker engine = mock(EngineChecker.class);

        doThrow(new CompositeEvent(new ArrayList<Event>())).when(collection).checkAll(engine);

        Collector collector = new Collector(engine, listener, collection);
        collector.checkAll();

        verify(collection).checkAll(engine);
        verify(engine).connect();
        verify(engine).disconnect();
        verify(listener).notify(any(CompositeEvent.class));
        verifyNoMoreInteractions(engine);
    }

    public void testWillNotThrowExceptionIfListenerThrowsException() throws CompositeEvent, ServerUnavailable {
        QueueCollection collection = mock(QueueCollection.class);
        Listener listener = mock(Listener.class);
        EngineChecker engine = mock(EngineChecker.class);

        doThrow(new RuntimeException()).when(listener).notify(any(Event.class));
        doThrow(new ServerUnavailable("test", new Exception())).when(engine).connect();

        Collector collector = new Collector(engine, listener, collection);
        collector.checkAll();

        verify(engine).connect();
        verify(engine).disconnect();
        verify(listener).notify(any(ServerUnavailable.class));
    }

    public void testWillNotifyListenerIfServerIsUnavailable() throws CompositeEvent, ServerUnavailable {
        QueueCollection collection = mock(QueueCollection.class);
        Listener listener = mock(Listener.class);
        EngineChecker engine = mock(EngineChecker.class);
        doThrow(new ServerUnavailable("asd", new Exception())).when(engine).connect();

        Collector collector = new Collector(engine, listener, collection);
        collector.checkAll();

        verify(engine).connect();
        verify(engine).disconnect();
        verify(listener).notify(any(ServerUnavailable.class));
        verifyNoMoreInteractions(engine);
        verifyZeroInteractions(collection);
    }
}
