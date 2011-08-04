package net.intelie.monitor.events;

public class ServerUnavailable extends Exception implements Event {
    public ServerUnavailable(String fullUrl, Throwable innerException) {
        super("Could not connect into '" + fullUrl + "' (" + innerException.getMessage() + ")", innerException);
    }
}
