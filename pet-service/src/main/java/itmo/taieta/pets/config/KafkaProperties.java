package itmo.taieta.pets.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {

    private String bootstrapServers;
    private String groupId;
    private PetProperties owner = new PetProperties();

    @Data
    public static class PetProperties {
        private String requestTopic;
        private String responseTopic;
    }
}
