package see.fa.artifactpatcher.models;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class ArtifactDescription {

    private SortedSet<FileArtifactDescription> files = new TreeSet<FileArtifactDescription>();

    public SortedSet<FileArtifactDescription> getFiles() {
        return files;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("files", getFiles())
                .toString();
    }
}
