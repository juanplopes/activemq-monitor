package net.intelie.monitor.events;

import java.util.List;

public class CompositeEvent extends Exception implements Event {
    List<Event> events;

    public CompositeEvent(List<Event> events) {
        super(MakeMessage(events));
        this.events = events;
    }

    static String MakeMessage(List<Event> list) {
        if (list.size() == 0) {
            return "[no events]";
        } else if (list.size() == 1) {
            return list.get(0).getMessage();
        } else {
            return list.get(0).getMessage() + " (" + (list.size()-1) + " more)";
        }
    }
}
