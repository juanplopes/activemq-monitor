package net.intelie.monitor.events;

public class ServerUnavailable extends BaseEvent {
    public ServerUnavailable(String fullUrl, Throwable innerException) {
        super("Could not connect into '" + fullUrl + "'",
                "Could not connect into '" + fullUrl + "' (" + innerException.getMessage() + ")",
                innerException);
    }
}
