package see.fa.artifactpatcher.models;

import com.beust.jcommander.Parameter;

import java.io.File;

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
        if (output == null && file != null) {
            String baseName = new File(file).getName();
            int extensionStart = baseName.lastIndexOf(".");
            String baseNameWithoutExtension = extensionStart != -1 ? baseName.substring(0, extensionStart) : baseName;
            output = String.format("%s.xml", baseNameWithoutExtension);
        }
        return output;
    }

    public String getFile() {
        return file;
    }
}
