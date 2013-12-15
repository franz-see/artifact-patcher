package see.fa.artifactpatcher.util;

import org.apache.commons.io.IOUtils;
import see.fa.artifactpatcher.ArtifactPatcherException;
import see.fa.artifactpatcher.Errors;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Formatter;

import static see.fa.artifactpatcher.Errors.UNABLE_TO_READ_INPUT_STREAM_FOR_CHECKSUM;

public class ChecksumUtil {

    private static final MessageDigest SHASUM_MESSAGE_DIGEST = MessageDigestUtil.create("SHA");

    public static String shasum(InputStream inputStream) {
        byte[] data;
        try {
            data = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new ArtifactPatcherException(UNABLE_TO_READ_INPUT_STREAM_FOR_CHECKSUM, e);
        }
        return shasum(data);
    }

    public static String shasum(byte[] data) {
        return byteArray2Hex(SHASUM_MESSAGE_DIGEST.digest(data));
    }

    private static String byteArray2Hex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }
}
