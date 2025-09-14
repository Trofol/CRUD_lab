package notificationservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Data
@Component
@ConfigurationProperties(prefix = "kafka")
public class KafkaConfigProperties {
    private String topic;
    private String groupId;
}
