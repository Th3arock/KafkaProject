package producer.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import producer.model.Email;

@Service
@Slf4j
public class EmailProducer {
    @Autowired
    private KafkaTemplate<String, Email> emailKafkaTemplate;

    public void sendEmail(Email email) {

        log.info(String.format("THE CONTENT OF EMAIL IS %s", email.toString()));

        emailKafkaTemplate.send("studentsEmail", email);
    }
}