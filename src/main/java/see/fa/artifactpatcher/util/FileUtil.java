package see.fa.artifactpatcher.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import see.fa.artifactpatcher.ArtifactPatcherException;
import see.fa.artifactpatcher.Errors;

import java.io.File;
import java.io.IOException;

import static see.fa.artifactpatcher.Errors.UNABLE_TO_COPY_DIRECTORY;
import static see.fa.artifactpatcher.Errors.UNABLE_TO_CREATE_TEMP_WORKING_DIR;

public class FileUtil {
    public static File createTempDirectory(String prefix, String suffix) {
        File tempDirectory = null;
        try {
            tempDirectory = File.createTempFile(prefix, suffix);
        } catch (IOException e) {
            throw new ArtifactPatcherException(String.format(UNABLE_TO_CREATE_TEMP_WORKING_DIR, tempDirectory), e);
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
            throw new ArtifactPatcherException(String.format(UNABLE_TO_COPY_DIRECTORY, sourceDir, targetDir), e);
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
