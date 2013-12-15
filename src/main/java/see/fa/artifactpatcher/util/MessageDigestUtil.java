package see.fa.artifactpatcher.util;

import see.fa.artifactpatcher.ArtifactPatcherException;
import see.fa.artifactpatcher.Errors;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static see.fa.artifactpatcher.Errors.UNABLE_TO_CREATE_MESSAGE_DIGEST;

public class MessageDigestUtil {
    public static MessageDigest create(String algorithmName) {
        try {
            return MessageDigest.getInstance(algorithmName);
        } catch (NoSuchAlgorithmException e) {
            throw new ArtifactPatcherException(String.format(UNABLE_TO_CREATE_MESSAGE_DIGEST, algorithmName), e);
        }
    }
}
