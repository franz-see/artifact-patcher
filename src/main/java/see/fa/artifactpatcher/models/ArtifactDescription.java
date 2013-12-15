package see.fa.artifactpatcher.models;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.LinkedList;
import java.util.List;

public class ArtifactDescription {

    private List<FileArtifactDescription> files = new LinkedList<FileArtifactDescription>();

    public List<FileArtifactDescription> getFiles() {
        return files;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("files", getFiles())
                .toString();
    }
}
