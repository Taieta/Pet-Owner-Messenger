package itmo.taieta.events;

import lombok.Data;

import java.io.Serializable;

@Data
public class EventResponse implements Serializable {
    private String status;

    private Object data;

    private String correlationId;

    public EventResponse() {}

    public EventResponse(String status, Object data) {
        this.status = status;
        this.data = data;
    }
}
