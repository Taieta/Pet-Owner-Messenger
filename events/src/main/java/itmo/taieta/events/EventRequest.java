package itmo.taieta.events;

import lombok.Data;

import java.io.Serializable;

@Data
public class EventRequest implements Serializable {
    private String operation;

    private Object data;

    private String correlationId;

    private String replyTo;

    public EventRequest() {}

    public EventRequest(String operation, Object data) {
        this.operation = operation;
        this.data = data;
    }
}
