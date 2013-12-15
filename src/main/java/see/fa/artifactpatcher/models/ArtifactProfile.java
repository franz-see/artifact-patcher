package see.fa.artifactpatcher.models;

import java.util.LinkedList;
import java.util.List;

public class ArtifactProfile {

    private List<FileArtifactProfile> files = new LinkedList<FileArtifactProfile>();

    public List<FileArtifactProfile> getFiles() {
        return files;
    }
}
