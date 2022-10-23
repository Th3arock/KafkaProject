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

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            byte[] nameByte = email.getName().getBytes();
            byte[] fileByte = email.getFile().getBytes();
            byte[] originalFileNameByte = email.getFile().getOriginalFilename().getBytes();

            dos.writeInt(nameByte.length);
            dos.write(nameByte);
            dos.writeInt(fileByte.length);
            dos.write(fileByte);
            dos.writeInt(originalFileNameByte.length);
            dos.write(originalFileNameByte);

            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}