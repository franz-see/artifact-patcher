package see.fa.artifactpatcher;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import see.fa.artifactpatcher.models.*;
import see.fa.artifactpatcher.util.FileUtil;

import java.io.File;
import java.util.Collection;

import static org.apache.commons.io.filefilter.FileFilterUtils.notFileFilter;
import static see.fa.artifactpatcher.util.ArtifactDescriptionUtil.*;
import static see.fa.artifactpatcher.util.XMLUtil.writeXML;
import static see.fa.artifactpatcher.util.ZipUtil.unzip;
import static see.fa.artifactpatcher.util.ZipUtil.zip;

public class ArtifactPatcher {

    public static final String PATCH_ARTIFACT_DESCRIPTION_XML = "artifact-description.xml";

    private static final Logger LOGGER = LoggerFactory.getLogger(ArtifactPatcher.class);

    public void execute(DescribeArtifact args) {
        LOGGER.trace("Executing against {}.", args);

        File file = new File(args.getFile());
        ArtifactDescription artifactDescription = createArtifactDescription(file);
        File outputFile = new File(args.getOutput());
        writeXML(artifactDescription, outputFile);

        LOGGER.trace("Description saved in {}.", outputFile.getAbsolutePath());
    }

    public void execute(CreatePatch args) {
        LOGGER.trace("Executing against {}.", args);

        ArtifactDescription sourceArtifactDescription = readArtifactDescription(new File(args.getDescription()));
        ArtifactDescription destinationArtifactDescription = createArtifactDescription(new File(args.getFile()));

        @SuppressWarnings("unchecked") Collection<FileArtifactDescription> newFiles = CollectionUtils.subtract(destinationArtifactDescription.getFiles(), sourceArtifactDescription.getFiles());

        File workingDir = FileUtil.createTempDirectory("ArtifactPatcher", "CreatePatch");
        File sourceDir = new File(workingDir, "source");
        File destinationDir = new File(workingDir, "destination");

        unzip(new File(args.getFile()), sourceDir);

        FileUtil.copyDirectory(sourceDir, destinationDir, new NameFileFilter(toFileNames(newFiles)));
        writeXML(destinationArtifactDescription, new File(destinationDir, PATCH_ARTIFACT_DESCRIPTION_XML));

        zip(destinationDir, args.getOutput());

        LOGGER.trace("Patch saved in {}.", args.getOutput());
    }

    public void execute(ApplyPatch args) {
        LOGGER.trace("Executing against {}.", args);

        ArtifactDescription toBePatchedArtifactDescription = createArtifactDescription(new File(args.getFile()));

        File workingDir = FileUtil.createTempDirectory("ArtifactPatcher", "ApplyPatch");
        File explodedJarToBePatched = new File(workingDir, "to_be_patched");
        File patchJarDir = new File(workingDir, "patch_dir");
        File patchedJarDir = new File(workingDir, "patched_jar_dir");

        unzip(new File(args.getFile()), explodedJarToBePatched);
        unzip(new File(args.getPatch()), patchJarDir);

        ArtifactDescription patchArtifactDescription = readArtifactDescription(new File(patchJarDir, PATCH_ARTIFACT_DESCRIPTION_XML));

        @SuppressWarnings("unchecked") Collection<FileArtifactDescription> filesNotToCopy = CollectionUtils.subtract(toBePatchedArtifactDescription.getFiles(), patchArtifactDescription.getFiles());

        FileUtil.copyDirectory(explodedJarToBePatched, patchedJarDir, notFileFilter(new NameFileFilter(toFileNames(filesNotToCopy))));
        FileUtil.copyDirectory(patchJarDir, patchedJarDir, notFileFilter(new NameFileFilter(PATCH_ARTIFACT_DESCRIPTION_XML)));

        zip(patchedJarDir, args.getOutput());

        LOGGER.trace("Patched JAR saved in {}.", args.getOutput());
    }

}
