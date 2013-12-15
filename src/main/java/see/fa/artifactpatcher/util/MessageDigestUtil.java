package see.fa.artifactpatcher.util;

import see.fa.artifactpatcher.ArtifactPatcherException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestUtil {
    public static MessageDigest create(String algorithmName) {
        try {
            return MessageDigest.getInstance(algorithmName);
        } catch (NoSuchAlgorithmException e) {
            throw new ArtifactPatcherException("Unable to create ArtifactPatcher.", e);
        }
    }
}
