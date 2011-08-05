package net.intelie.monitor.events;

public class QueueNotFound extends BaseEvent {
    public QueueNotFound(String queueName, Throwable innerException) {
        super("Could not find queue: '" + queueName + "'",
                "Could not find queue: '" + queueName + "' ( " + innerException.getMessage() + ").",
                innerException);
    }
}
