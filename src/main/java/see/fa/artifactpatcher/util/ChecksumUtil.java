package see.fa.artifactpatcher.util;

import org.apache.commons.io.IOUtils;
import see.fa.artifactpatcher.ArtifactPatcherException;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Formatter;

public class ChecksumUtil {

    private static final MessageDigest SHASUM_MESSAGE_DIGEST = MessageDigestUtil.create("SHA");

    public static String shasum(InputStream inputStream) {
        byte[] data;
        try {
            data = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new ArtifactPatcherException("Unable to read input stream to generate checksum on.", e);
        }
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
