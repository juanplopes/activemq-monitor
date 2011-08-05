package net.intelie.monitor.events;

public class QueueStoppedConsuming extends BaseEvent {
    public QueueStoppedConsuming(String queueName) {
        super("The queue '" + queueName + "' stopped consuming.", "The queue '" + queueName + "' stopped consuming.");
    }

}
