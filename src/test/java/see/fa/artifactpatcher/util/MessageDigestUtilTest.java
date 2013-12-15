package see.fa.artifactpatcher.util;

import org.junit.Test;
import see.fa.artifactpatcher.ArtifactPatcherException;
import see.fa.artifactpatcher.util.MessageDigestUtil;

public class MessageDigestUtilTest {

    @Test(expected = ArtifactPatcherException.class)
    public void given_unknown_algorithm() {
        MessageDigestUtil.create("non-existent algorithm");
    }

    @Test
    public void given_known_algorithm() {
        MessageDigestUtil.create("SHA-1");
    }
}
