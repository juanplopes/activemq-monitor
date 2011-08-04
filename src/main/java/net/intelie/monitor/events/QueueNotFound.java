package net.intelie.monitor.events;

public class QueueNotFound extends Exception implements Event {
    public QueueNotFound(String queueName, Throwable innerException) {
        super("Could not find queue: " + queueName + " (" + innerException.getMessage() + ").", innerException);
    }
}
