package see.fa.artifactpatcher;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import see.fa.artifactpatcher.models.*;
import see.fa.artifactpatcher.util.FileUtil;
import see.fa.artifactpatcher.util.XMLUtil;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static see.fa.artifactpatcher.util.ChecksumUtil.shasum;
import static see.fa.artifactpatcher.util.XMLUtil.readXML;
import static see.fa.artifactpatcher.util.XMLUtil.writeXML;
import static see.fa.artifactpatcher.util.ZipUtil.unzip;
import static see.fa.artifactpatcher.util.ZipUtil.zip;

public class ArtifactPatcher {

    public void execute(CreateArtifactProfile args) {
        File file = new File(args.getFile());
        ArtifactProfile artifactProfile = toArtifactProfiles(file);
        writeXML(artifactProfile, new File(args.getOutput()));
    }

    public void execute(CreatePatch args) {
        ArtifactProfile sourceArtifactProfile = readArtifactProfile(new File(args.getProfile()));
        ArtifactProfile destinationArtifactProfile = toArtifactProfiles(new File(args.getFile()));

        @SuppressWarnings("unchecked") Collection<FileArtifactProfile> newFiles = CollectionUtils.subtract(destinationArtifactProfile.getFiles(), sourceArtifactProfile.getFiles());

        File workingDir = FileUtil.createTempDirectory("ArtifactPatcher", "d");
        File sourceDir = new File(workingDir, "source");
        File destinationDir = new File(workingDir, "destination");

        unzip(new File(args.getFile()), sourceDir.getAbsolutePath());

        FileUtil.copyDirectory(sourceDir, destinationDir, new NameFileFilter(toFileNames(newFiles)));
        writeXML(destinationArtifactProfile, new File(destinationDir, "artifact-profile.xml"));

        zip(destinationDir, args.getOutput());
    }

    public void execute(ApplyPatch args) {

    }

    private List<String> toFileNames(Collection<FileArtifactProfile> fileArtifactProfiles) {
        List<String> fileNames = new LinkedList<String>();
        for (FileArtifactProfile fileArtifactProfile : fileArtifactProfiles) {
            fileNames.add(fileArtifactProfile.getName());
        }
        return fileNames;
    }

    private ArtifactProfile readArtifactProfile(File file) {
        ArtifactProfile artifactProfile = new ArtifactProfile();
        readXML(file, artifactProfile);
        return artifactProfile;
    }

    private ArtifactProfile toArtifactProfiles(File file) {
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
            throw new ArtifactPatcherException(String.format("Unable to read JAR/EAR/WAR//ZIP from dir '%s'.", new File(".").getAbsolutePath()), e);
        } finally {
            IOUtils.closeQuietly(zipInputStream);
        }
    }

}
