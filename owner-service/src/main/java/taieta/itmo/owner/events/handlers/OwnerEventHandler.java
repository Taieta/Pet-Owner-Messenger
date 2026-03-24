package taieta.itmo.owner.events.handlers;

import itmo.taieta.events.EventRequest;
import itmo.taieta.events.EventResponse;

public interface OwnerEventHandler {
    boolean canHandle(String event);
    void handle(EventRequest request, EventResponse response);
}
