package producer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import producer.kafka.EmailProducer;
import producer.model.Email;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class SendEmail {
    @Autowired
    private EmailProducer emailProducer;

//    @PostMapping("/upload/multipartfile")
//    public String uploadMultipartFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
//        kafkaFileProducer.uploadMultipartFile(multipartFile);
//        return "uploaded successfully";
//    }

    @PostMapping("/upload/sendEmail")
    public String sendEmail(@ModelAttribute Email email) throws IOException {
        emailProducer.sendEmail(email);
        return "email sent successfully";
    }
}