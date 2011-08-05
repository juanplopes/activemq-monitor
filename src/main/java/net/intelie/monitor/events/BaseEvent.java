package net.intelie.monitor.events;

public class BaseEvent extends Exception implements Event {
    String detail;

    public BaseEvent(String message, String detail, Throwable t) {
        super(message, t);
        this.detail = detail;
    }

    public BaseEvent(String message, String detail) {
        super(message);
        this.detail = detail;
    }

    @Override
    public String getDetail() {
        return detail;
    }
}
