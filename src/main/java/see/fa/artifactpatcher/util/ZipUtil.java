package see.fa.artifactpatcher.util;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import see.fa.artifactpatcher.ArtifactPatcherException;

import java.io.File;

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
            throw new ArtifactPatcherException(String.format("Unable to create zip file '%s' from directory '%s'.", outputZipPath, destinationDir), e);
        }
    }
    
    public static void unzip(File file, File targetDir) {
        try {
            new ZipFile(file).extractAll(targetDir.getAbsolutePath());
        } catch (ZipException e) {
            throw new ArtifactPatcherException(String.format("Unable to extract zip file '%s' to directory '%s'.", file, targetDir.getAbsolutePath()), e);
        }
    }
}
