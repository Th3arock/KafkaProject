package producer.serialization;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;
import producer.model.Email;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@Slf4j
@Data
@Getter
@Setter
public class EmailSerializer implements Serializer<Email> {
    @Override
    public byte[] serialize(String s, Email email) {
        try {
            return castToByteOneByOne(email);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public byte[] castToByteOneByOne(Email email) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        byte[] nameByte = email.getName().getBytes();
        byte[] contentByte = email.getContent().getBytes();
        byte[] fileByte = email.getAttachment().getBytes();
        byte[] originalFileNameByte = email.getAttachment().getOriginalFilename().getBytes();

        dos.writeInt(nameByte.length);
        dos.write(nameByte);
        dos.writeInt(contentByte.length);
        dos.write(contentByte);
        dos.writeInt(fileByte.length);
        dos.write(fileByte);
        dos.writeInt(originalFileNameByte.length);
        dos.write(originalFileNameByte);

        return bos.toByteArray();
    }
}
