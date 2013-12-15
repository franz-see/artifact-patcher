package see.fa.artifactpatcher;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import see.fa.artifactpatcher.models.*;
import see.fa.artifactpatcher.util.FileUtil;

import java.io.File;
import java.util.Collection;

import static org.apache.commons.io.filefilter.FileFilterUtils.notFileFilter;
import static see.fa.artifactpatcher.util.ArtifactProfileUtil.*;
import static see.fa.artifactpatcher.util.XMLUtil.writeXML;
import static see.fa.artifactpatcher.util.ZipUtil.unzip;
import static see.fa.artifactpatcher.util.ZipUtil.zip;

public class ArtifactPatcher {

    public static final String PATCH_ARTIFACT_PROFILE_XML = "artifact-profile.xml";

    public void execute(CreateArtifactProfile args) {
        File file = new File(args.getFile());
        ArtifactProfile artifactProfile = createArtifactProfile(file);
        writeXML(artifactProfile, new File(args.getOutput()));
    }

    public void execute(CreatePatch args) {
        ArtifactProfile sourceArtifactProfile = readArtifactProfile(new File(args.getProfile()));
        ArtifactProfile destinationArtifactProfile = createArtifactProfile(new File(args.getFile()));

        @SuppressWarnings("unchecked") Collection<FileArtifactProfile> newFiles = CollectionUtils.subtract(destinationArtifactProfile.getFiles(), sourceArtifactProfile.getFiles());

        File workingDir = FileUtil.createTempDirectory("ArtifactPatcher", "CreatePatch");
        File sourceDir = new File(workingDir, "source");
        File destinationDir = new File(workingDir, "destination");

        unzip(new File(args.getFile()), sourceDir);

        FileUtil.copyDirectory(sourceDir, destinationDir, new NameFileFilter(toFileNames(newFiles)));
        writeXML(destinationArtifactProfile, new File(destinationDir, PATCH_ARTIFACT_PROFILE_XML));

        zip(destinationDir, args.getOutput());
    }

    public void execute(ApplyPatch args) {
        ArtifactProfile toBePatchedArtifactProfile = createArtifactProfile(new File(args.getFile()));

        File workingDir = FileUtil.createTempDirectory("ArtifactPatcher", "ApplyPatch");
        File explodedJarToBePatched = new File(workingDir, "to_be_patched");
        File patchJarDir = new File(workingDir, "patch_dir");
        File patchedJarDir = new File(workingDir, "patched_jar_dir");

        unzip(new File(args.getFile()), explodedJarToBePatched);
        unzip(new File(args.getPatch()), patchJarDir);

        ArtifactProfile patchArtifactProfile = readArtifactProfile(new File(patchJarDir, PATCH_ARTIFACT_PROFILE_XML));

        @SuppressWarnings("unchecked") Collection<FileArtifactProfile> filesNotToCopy = CollectionUtils.subtract(toBePatchedArtifactProfile.getFiles(), patchArtifactProfile.getFiles());

        FileUtil.copyDirectory(explodedJarToBePatched, patchedJarDir, notFileFilter(new NameFileFilter(toFileNames(filesNotToCopy))));
        FileUtil.copyDirectory(patchJarDir, patchedJarDir, notFileFilter(new NameFileFilter(PATCH_ARTIFACT_PROFILE_XML)));

        zip(patchedJarDir, args.getOutput());
    }

}
