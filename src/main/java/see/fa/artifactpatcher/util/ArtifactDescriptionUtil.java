package see.fa.artifactpatcher.util;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import see.fa.artifactpatcher.ArtifactPatcherException;
import see.fa.artifactpatcher.models.ArtifactDescription;
import see.fa.artifactpatcher.models.FileArtifactDescription;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static see.fa.artifactpatcher.Errors.UNABLE_TO_READ_ARTIFACT;
import static see.fa.artifactpatcher.util.ChecksumUtil.shasum;
import static see.fa.artifactpatcher.util.XMLUtil.readXML;

public class ArtifactDescriptionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArtifactDescriptionUtil.class);

    public static List<String> toFileNames(Collection<FileArtifactDescription> fileArtifactDescriptions) {
        List<String> fileNames = new LinkedList<String>();
        for (FileArtifactDescription fileArtifactDescription : fileArtifactDescriptions) {
            fileNames.add(fileArtifactDescription.getName());
        }
        return fileNames;
    }

    public static ArtifactDescription readArtifactDescription(File file) {
        ArtifactDescription artifactDescription = new ArtifactDescription();
        readXML(file, artifactDescription);
        return artifactDescription;
    }

    public static ArtifactDescription createArtifactDescription(File file) {
        ZipInputStream zipInputStream = null;
        try {
            zipInputStream = new ZipInputStream(new FileInputStream(file));

            ArtifactDescription artifactDescription = new ArtifactDescription();

            ZipEntry zipEntry = zipInputStream.getNextEntry();
            int counter = 1;
            while(zipEntry != null) {
                FileArtifactDescription fileArtifactDescription = new FileArtifactDescription(zipEntry.getName(), shasum(zipInputStream));
                LOGGER.trace("Processing ZipEntry #{} : {}", counter++, fileArtifactDescription);
                artifactDescription.getFiles().add(fileArtifactDescription);

                zipEntry = zipInputStream.getNextEntry();
            }
            return artifactDescription;
        } catch (IOException e) {
            throw new ArtifactPatcherException(String.format(UNABLE_TO_READ_ARTIFACT, file.getAbsolutePath()), e);
        } finally {
            IOUtils.closeQuietly(zipInputStream);
        }
    }
}
