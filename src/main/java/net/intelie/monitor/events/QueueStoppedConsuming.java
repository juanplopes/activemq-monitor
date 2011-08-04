package net.intelie.monitor.events;

public class QueueStoppedConsuming extends Exception implements Event {
    public QueueStoppedConsuming(String queueName) {
        super("The queue " + queueName + " stopped consuming");
    }

}
