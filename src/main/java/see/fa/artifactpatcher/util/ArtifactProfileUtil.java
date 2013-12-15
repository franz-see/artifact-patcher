package see.fa.artifactpatcher.util;

import org.apache.commons.io.IOUtils;
import see.fa.artifactpatcher.ArtifactPatcherException;
import see.fa.artifactpatcher.Errors;
import see.fa.artifactpatcher.models.ArtifactProfile;
import see.fa.artifactpatcher.models.FileArtifactProfile;

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

public class ArtifactProfileUtil {
    
    public static List<String> toFileNames(Collection<FileArtifactProfile> fileArtifactProfiles) {
        List<String> fileNames = new LinkedList<String>();
        for (FileArtifactProfile fileArtifactProfile : fileArtifactProfiles) {
            fileNames.add(fileArtifactProfile.getName());
        }
        return fileNames;
    }

    public static ArtifactProfile readArtifactProfile(File file) {
        ArtifactProfile artifactProfile = new ArtifactProfile();
        readXML(file, artifactProfile);
        return artifactProfile;
    }

    public static ArtifactProfile createArtifactProfile(File file) {
        ZipInputStream zipInputStream = null;
        try {
            zipInputStream = new ZipInputStream(new FileInputStream(file));

            ArtifactProfile artifactProfile = new ArtifactProfile();

            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while(zipEntry != null) {
                artifactProfile.getFiles().add(new FileArtifactProfile(zipEntry.getName(), shasum(zipInputStream)));

                zipEntry = zipInputStream.getNextEntry();
            }
            return artifactProfile;
        } catch (IOException e) {
            throw new ArtifactPatcherException(String.format(UNABLE_TO_READ_ARTIFACT, new File(".").getAbsolutePath()), e);
        } finally {
            IOUtils.closeQuietly(zipInputStream);
        }
    }
}
