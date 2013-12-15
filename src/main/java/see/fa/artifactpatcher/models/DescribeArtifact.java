package see.fa.artifactpatcher.models;

import com.beust.jcommander.Parameter;

import java.io.File;

import static see.fa.artifactpatcher.models.ParameterNames.*;
import static see.fa.artifactpatcher.util.FileUtil.replaceExtension;

public class DescribeArtifact {

    private String file;
    private String output;

    @Parameter(names = {FILE_SHORT, FILE_LONG}, description = "The file to create a description from.", required = true)
    public void setFile(String file) {
        this.file = file;
    }

    @Parameter(names = {OUTPUT_SHORT, OUTPUT_LONG, DESCRIPTION_SHORT, DESCRIPTION_LONG}, description = "The filename of the output description.", required = false)
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
