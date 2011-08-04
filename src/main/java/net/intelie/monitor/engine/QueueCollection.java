package net.intelie.monitor.engine;

import net.intelie.monitor.events.CompositeEvent;
import net.intelie.monitor.events.Event;
import net.intelie.monitor.events.QueueNotFound;
import net.intelie.monitor.events.QueueStoppedConsuming;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * Gets information from ActiveMQ through JMX frequently and uses rules to determine if
 * is necessary to alert about some behaviour.
 */
public class QueueCollection {
    static Logger logger = Logger.getLogger(QueueCollection.class);

    List<QueueMonitor> monitors;

    public QueueCollection(String[] monitoredQueues) {
        LinkedList<QueueMonitor> monitors = new LinkedList<QueueMonitor>();

        for (String queueName : monitoredQueues) {
            monitors.add(new QueueMonitor(queueName));
        }
    }

    public void checkAll(EngineChecker engineChecker) throws CompositeEvent {
        LinkedList<Event> events = new LinkedList<Event>();
        for (QueueMonitor monitor : monitors)
            try {
                monitor.check(engineChecker);
            } catch (QueueNotFound e) {
                events.add(e);
            } catch (QueueStoppedConsuming e) {
                events.add(e);
            }

        if (!events.isEmpty())
            throw new CompositeEvent(events);
    }


}
