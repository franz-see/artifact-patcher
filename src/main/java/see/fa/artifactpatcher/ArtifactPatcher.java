package see.fa.artifactpatcher;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import see.fa.artifactpatcher.models.*;
import see.fa.artifactpatcher.util.XMLUtil;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static see.fa.artifactpatcher.util.ChecksumUtil.shasum;

public class ArtifactPatcher {

    public void execute(CreateArtifactProfile args) {
        File file = new File(args.getFile());
        ArtifactProfile artifactProfile = toArtifactProfiles(file);
        XMLUtil.write(artifactProfile, new File(args.getOutput()));
    }

    public void execute(CreatePatch args) {
        ArtifactProfile sourceArtifactProfile = readArtifactProfile(new File(args.getProfile()));
        ArtifactProfile destinationArtifactProfile = toArtifactProfiles(new File(args.getFile()));

        @SuppressWarnings("unchecked") Collection<FileArtifactProfile> newFiles = CollectionUtils.subtract(destinationArtifactProfile.getFiles(), sourceArtifactProfile.getFiles());

        File workingDir = null;
        try {
            workingDir = File.createTempFile("ArtifactPatcher", "d");
            workingDir.delete();
            workingDir.mkdirs();
            workingDir.deleteOnExit();
        } catch (IOException e) {
            throw new ArtifactPatcherException(String.format("Unable to create temp working directory at '%s'.", workingDir), e);
        }

        File sourceDir = new File(workingDir, "source");
        File destinationDir = new File(workingDir, "destination");

        unzip(new File(args.getFile()), sourceDir.getAbsolutePath());

        try {
            FileUtils.copyDirectory(sourceDir, destinationDir, new NameFileFilter(toFileNames(newFiles)));
        } catch (IOException e) {
            throw new ArtifactPatcherException(String.format("Unable to copy from '%s' to '%s'.", sourceDir, destinationDir), e);
        }

        XMLUtil.write(destinationArtifactProfile, new File(destinationDir, "artifact-profile.xml"));

        try {
            ZipFile outputZip = new ZipFile(args.getOutput());
            for (File file : destinationDir.listFiles()) {
                if (file.isDirectory()) {
                    outputZip.addFolder(file, new ZipParameters());
                } else {
                    outputZip.addFile(file, new ZipParameters());
                }
            }
        } catch (ZipException e) {
            throw new ArtifactPatcherException(String.format("Unable to create zip file '%s'.", args.getOutput()), e);
        }
    }

    private List<String> toFileNames(Collection<FileArtifactProfile> fileArtifactProfiles) {
        List<String> fileNames = new LinkedList<String>();
        for (FileArtifactProfile fileArtifactProfile : fileArtifactProfiles) {
            fileNames.add(fileArtifactProfile.getName());
        }
        return fileNames;
    }

    private void unzip(File file, String explodedDir) {
        try {
            new ZipFile(file).extractAll(explodedDir);
        } catch (ZipException e) {
            throw new ArtifactPatcherException(String.format("Unable to extract zip file '%s' to directory '%s'.", file, explodedDir), e);
        }
    }

    public void execute(ApplyPatch args) {

    }

    private ArtifactProfile readArtifactProfile(File file) {
        ArtifactProfile artifactProfile = new ArtifactProfile();
        XMLUtil.read(file, artifactProfile);
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
