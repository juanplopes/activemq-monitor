package net.intelie.monitor.events;

import java.util.List;

public class CompositeEvent extends BaseEvent {
    private List<Event> events;

    public CompositeEvent(List<Event> events) {
        super(MakeMessage(events), MakeDetails(events));
        this.events = events;
    }

    static String MakeMessage(List<Event> list) {
        if (list.size() == 0) {
            return "[no events]";
        } else if (list.size() == 1) {
            return list.get(0).getMessage();
        } else {
            return list.get(0).getMessage() + " (" + (list.size() - 1) + " more)";
        }
    }

    static String MakeDetails(List<Event> list) {
        if (list.size() == 0)
            return "[no events]";

        StringBuilder builder = new StringBuilder();
        for (Event e : list) {
            builder.append(e.getDetail());
            builder.append("\n");
        }
        return builder.toString();
    }

    public List<Event> getEvents() {
        return events;
    }
}
