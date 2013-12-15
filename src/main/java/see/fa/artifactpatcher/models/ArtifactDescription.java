package see.fa.artifactpatcher.models;

import java.util.LinkedList;
import java.util.List;

public class ArtifactDescription {

    private List<FileArtifactDescription> files = new LinkedList<FileArtifactDescription>();

    public List<FileArtifactDescription> getFiles() {
        return files;
    }
}
