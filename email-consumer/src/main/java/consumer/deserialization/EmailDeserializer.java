package consumer.deserialization;

import consumer.cast.DecodedMultipartFile;
import consumer.model.Email;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.web.multipart.MultipartFile;

import java.nio.ByteBuffer;

@Slf4j
@Data
@Getter
@Setter
public class EmailDeserializer implements Deserializer<Email> {

    private String encoding = "UTF8";

    @Override
    public Email deserialize(String s, byte[] data) {
        return castToModelClassOneByOne(data);
    }

    public Email castToModelClassOneByOne(byte[] data) {
        int nameSize;
        int contentSize;
        int attachmentSize;
        int originalFileNameSize;

        if (data == null)
            return null;

        ByteBuffer buffer = ByteBuffer.wrap(data);

        nameSize = buffer.getInt();
        byte[] nameBytes = new byte[nameSize];
        buffer.get(nameBytes);

        contentSize = buffer.getInt();
        byte[] contentByte = new byte[contentSize];
        buffer.get(contentByte);

        attachmentSize = buffer.getInt();
        byte[] attachmentByte = new byte[attachmentSize];
        buffer.get(attachmentByte);

        originalFileNameSize = buffer.getInt();
        byte[] originalFileNameByte = new byte[originalFileNameSize];
        buffer.get(originalFileNameByte);

        try {
            String deserializedName = new String(nameBytes, encoding);
            String deserializedContent = new String(contentByte, encoding);
            String deserializedOriginalFileName = new String(originalFileNameByte, encoding);
            MultipartFile attachment = new DecodedMultipartFile(attachmentByte, deserializedOriginalFileName);

            return new Email(deserializedName, deserializedContent, attachment);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}