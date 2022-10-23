package consumer.deserialization;

import consumer.cast.DecodedMultipartFile;
import consumer.model.Email;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.web.multipart.MultipartFile;

import java.nio.ByteBuffer;
import java.util.Map;

@Slf4j
@Data
@Getter
@Setter
public class EmailDeserializer implements Deserializer<Email> {

    private String encoding = "UTF8";

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public Email deserialize(String s, byte[] data) {
        int nameSize;
        int fileSize;
        int originalFileNameSize;

        if (data == null)
            return null;

        ByteBuffer buffer = ByteBuffer.wrap(data);

        nameSize = buffer.getInt();
        byte[] nameBytes = new byte[nameSize];
        buffer.get(nameBytes);

        fileSize = buffer.getInt();
        byte[] fileByte = new byte[fileSize];
        buffer.get(fileByte);

        originalFileNameSize = buffer.getInt();
        byte[] originalFileNameByte = new byte[originalFileNameSize];
        buffer.get(originalFileNameByte);


        try {
            String deserializedName = new String(nameBytes, encoding);
            String deserializedOriginalFileName = new String(originalFileNameByte, encoding);
            MultipartFile file = new DecodedMultipartFile(fileByte, deserializedOriginalFileName);

            return new Email(deserializedName, file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Email deserialize(String topic, Headers headers, byte[] data) {
        return Deserializer.super.deserialize(topic, headers, data);
    }

    @Override
    public void close() {
        Deserializer.super.close();
    }
}