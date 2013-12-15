package see.fa.artifactpatcher.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import see.fa.artifactpatcher.ArtifactPatcherException;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    public static File createTempDirectory(String prefix, String suffix) {
        File tempDirectory = null;
        try {
            tempDirectory = File.createTempFile(prefix, suffix);
        } catch (IOException e) {
            throw new ArtifactPatcherException(String.format("Unable to create temp working directory at '%s'.", tempDirectory), e);
        }
        tempDirectory.delete();
        tempDirectory.mkdirs();
        tempDirectory.deleteOnExit();
        return tempDirectory;
    }


    public static void copyDirectory(File sourceDir, File targetDir, IOFileFilter ioFileFilter) {
        try {
            FileUtils.copyDirectory(sourceDir, targetDir, ioFileFilter);
        } catch (IOException e) {
            throw new ArtifactPatcherException(String.format("Unable to copy from '%s' to '%s'.", sourceDir, targetDir), e);
        }
    }

    public static String replaceExtension(String filePath, String newExtension) {
        if (filePath == null) {
            return null;
        }

        String baseName = new File(filePath).getName();
        int extensionIndex = baseName.lastIndexOf(".");
        String baseNameWithoutExtension = extensionIndex != -1 ? baseName.substring(0, extensionIndex) : baseName;
        return String.format("%s.%s", baseNameWithoutExtension, newExtension);
    }
}
