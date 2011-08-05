package net.intelie.monitor.engine;

import net.intelie.monitor.events.QueueNotFound;
import net.intelie.monitor.events.QueueStoppedConsuming;
import net.intelie.monitor.listeners.Listener;
import org.apache.log4j.Logger;

import java.util.LinkedList;

/**
 * Checks number of dequeued messages and alerts when a queue has stopped consumption.
 */
public class QueueMonitor {
    private static Logger logger = Logger.getLogger(QueueMonitor.class);

    private Listener listener;
    private String queueName;


    private LinkedList<Long> lastChecks = new LinkedList<Long>();
    private static final int EVENTS_BEFORE_NOTIFY = 3;

    public QueueMonitor(String queueName) {
        logger.info("Starting queue monitor for queue " + queueName);

        this.queueName = queueName;
    }

    public void check(EngineChecker checker) throws QueueNotFound, QueueStoppedConsuming {
        while (lastChecks.size() > EVENTS_BEFORE_NOTIFY)
            lastChecks.removeFirst();

        long last = checker.getDequeueCount(queueName);

        logger.info("Checked queue " + queueName + ": " + last);


        lastChecks.addLast(last);
        evaluate();
    }

    public void evaluate() throws QueueStoppedConsuming {
        if (lastChecks.size() != EVENTS_BEFORE_NOTIFY) return;

        boolean allEqual = true;
        for (Long check : lastChecks)
            allEqual &= check.equals(lastChecks.getFirst());

        if (allEqual)
            throw new QueueStoppedConsuming(queueName);
    }

}
