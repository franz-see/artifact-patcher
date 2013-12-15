package see.fa.artifactpatcher;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import see.fa.artifactpatcher.models.*;
import see.fa.artifactpatcher.util.FileUtil;

import java.io.File;
import java.util.Collection;

import static see.fa.artifactpatcher.util.ArtifactProfileUtil.*;
import static see.fa.artifactpatcher.util.XMLUtil.writeXML;
import static see.fa.artifactpatcher.util.ZipUtil.unzip;
import static see.fa.artifactpatcher.util.ZipUtil.zip;

public class ArtifactPatcher {

    public void execute(CreateArtifactProfile args) {
        File file = new File(args.getFile());
        ArtifactProfile artifactProfile = createArtifactProfile(file);
        writeXML(artifactProfile, new File(args.getOutput()));
    }

    public void execute(CreatePatch args) {
        ArtifactProfile sourceArtifactProfile = readArtifactProfile(new File(args.getProfile()));
        ArtifactProfile destinationArtifactProfile = createArtifactProfile(new File(args.getFile()));

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

}
