package itmo.taieta.pets.events.handlers;

import itmo.taieta.events.EventRequest;
import itmo.taieta.events.EventResponse;

public interface PetEventHandler {
    boolean canHandle(String event);
    void handle(EventRequest request, EventResponse response);
}
