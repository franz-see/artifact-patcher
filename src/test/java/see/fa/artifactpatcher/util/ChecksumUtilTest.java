package see.fa.artifactpatcher.util;

import org.junit.Test;
import see.fa.artifactpatcher.ArtifactPatcherException;
import see.fa.artifactpatcher.util.ChecksumUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class ChecksumUtilTest {

    @Test
    public void should_generate_shasum() {
        InputStream input = new ByteArrayInputStream("0123456789abcdefghijklmnopqrstuvwxyz".getBytes());
        String checksum = ChecksumUtil.shasum(input);
        assertEquals("a26704c04fc5f10db5aab58468035531cc542485", checksum);
    }

    @Test(expected = ArtifactPatcherException.class)
    public void given_unreadable_InputStream() {
        InputStream input = new ByteArrayInputStream(new byte[0]) {
            @Override
            public int read(byte[] bytes) throws IOException {
                throw new IOException("This exception always gets thrown");
            }
        };
        String checksum = ChecksumUtil.shasum(input);
        assertEquals("a26704c04fc5f10db5aab58468035531cc542485", checksum);
    }
}
