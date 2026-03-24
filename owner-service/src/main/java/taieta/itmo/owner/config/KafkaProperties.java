package taieta.itmo.owner.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {

    private String bootstrapServers;
    private String groupId;
    private OwnerProperties owner = new OwnerProperties();

    @Data
    public static class OwnerProperties {
        private String requestTopic;
        private String responseTopic;
    }
}
