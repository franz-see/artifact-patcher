package see.fa.artifactpatcher.models;

import com.beust.jcommander.Parameter;

import java.io.File;

import static see.fa.artifactpatcher.util.FileUtil.replaceExtension;

public class CreateArtifactProfile {

    private String file;
    private String output;

    @Parameter(names = {"-f", "--file"}, description = "The file to create a profile from.", required = true)
    public void setFile(String file) {
        this.file = file;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getOutput() {
        if (output == null) {
            output = replaceExtension(file, "xml");
        }
        return output;
    }

    public String getFile() {
        return file;
    }
}
