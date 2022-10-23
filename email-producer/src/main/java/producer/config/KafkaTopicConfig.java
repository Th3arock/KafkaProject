package producer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
//    Map<String, String> conf = new HashMap<>();
//    @Bean
//    public NewTopic fileTopic() {
//        conf.put("max.message.bytes", "20971520");
//        conf.put("max.request.size", "20971520");
//        return TopicBuilder.name("files")
//                .configs(conf)
//                .build();
//    }


    @Bean
    public NewTopic emailTopic() {
        return TopicBuilder.name("studentsEmail")
                .partitions(3)
                .build();
    }
}