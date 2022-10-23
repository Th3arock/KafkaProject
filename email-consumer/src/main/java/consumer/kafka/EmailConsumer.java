package consumer.kafka;

import consumer.model.Email;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class EmailConsumer {
    private static final String uploadFolderPath = "C:/Users/e.gholami/Desktop/consume from kafka";


    @KafkaListener(topics = "studentsEmail")
    public void consumeFile(Email email , Acknowledgment acknowledgment) throws IOException {
        byte[] byteArr = email.getFile().getBytes();
        System.out.println(email.getFile().getOriginalFilename());
        System.out.println(email.getName());
        Path path = Paths.get(uploadFolderPath, email.getFile().getOriginalFilename());
        try {
            //saving the file for testing
            Files.write(path, byteArr);
            // This method called when the consumer consume the offset successfully ,
            // and after that manual-immediate will commit the offset
            acknowledgment.acknowledge();
        } catch (IOException exception) {
            exception.getMessage();
        }
    }
}
