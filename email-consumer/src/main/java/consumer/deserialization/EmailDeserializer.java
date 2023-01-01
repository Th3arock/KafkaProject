package consumer.deserialization;

import consumer.cast.DecodedMultipartFile;
import consumer.model.Email;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.web.multipart.MultipartFile;

import java.nio.ByteBuffer;

@Slf4j
public class EmailDeserializer implements Deserializer<Email> {

    private final static String ENCODING = "UTF8";
    private byte[] nameBytes;
    private byte[] contentByte;
    private byte[] attachmentByte;
    private byte[] originalFileNameByte;

    @Override
    public Email deserialize(String s, byte[] data) {
        return castToModelClassOneByOne(data);
    }

    public Email castToModelClassOneByOne(byte[] data) {

        if (data == null)
            return null;

        ByteBuffer buffer = ByteBuffer.wrap(data);

        getName(buffer);

        getContent(buffer);

        getAttachment(buffer);

        getOriginalFileName(buffer);

        return recreateModelContentsFromByte();
    }


    private void getName(ByteBuffer buffer) {
        int nameSize;
        nameSize = buffer.getInt();
        nameBytes = new byte[nameSize];
        buffer.get(nameBytes);
    }

    private void getContent(ByteBuffer buffer) {
        int contentSize;
        contentSize = buffer.getInt();
        contentByte = new byte[contentSize];
        buffer.get(contentByte);
    }

    private void getAttachment(ByteBuffer buffer) {
        int attachmentSize;
        attachmentSize = buffer.getInt();
        attachmentByte = new byte[attachmentSize];
        buffer.get(attachmentByte);
    }


    private void getOriginalFileName(ByteBuffer buffer) {
        int originalFileNameSize;
        originalFileNameSize = buffer.getInt();
        originalFileNameByte = new byte[originalFileNameSize];
        buffer.get(originalFileNameByte);
    }


    private Email recreateModelContentsFromByte() {
        try {
            String deserializedName = new String(nameBytes, ENCODING);
            String deserializedContent = new String(contentByte, ENCODING);
            String deserializedOriginalFileName = new String(originalFileNameByte, ENCODING);
            MultipartFile attachment = new DecodedMultipartFile(attachmentByte, deserializedOriginalFileName);

            return new Email(deserializedName, deserializedContent, attachment);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}