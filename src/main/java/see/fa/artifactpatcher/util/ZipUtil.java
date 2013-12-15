package see.fa.artifactpatcher.util;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import see.fa.artifactpatcher.ArtifactPatcherException;
import see.fa.artifactpatcher.Errors;

import java.io.File;

import static see.fa.artifactpatcher.Errors.UNABLE_TO_UNZIP;
import static see.fa.artifactpatcher.Errors.UNABLE_TO_ZIP;

public class ZipUtil {
    
    public static void zip(File destinationDir, String outputZipPath) {
        try {
            ZipFile outputZip = new ZipFile(outputZipPath);
            for (File file : destinationDir.listFiles()) {
                if (file.isDirectory()) {
                    outputZip.addFolder(file, new ZipParameters());
                } else {
                    outputZip.addFile(file, new ZipParameters());
                }
            }
        } catch (ZipException e) {
            throw new ArtifactPatcherException(String.format(UNABLE_TO_ZIP, outputZipPath, destinationDir), e);
        }
    }
    
    public static void unzip(File file, File targetDir) {
        try {
            new ZipFile(file).extractAll(targetDir.getAbsolutePath());
        } catch (ZipException e) {
            throw new ArtifactPatcherException(String.format(UNABLE_TO_UNZIP, file, targetDir.getAbsolutePath()), e);
        }
    }
}
