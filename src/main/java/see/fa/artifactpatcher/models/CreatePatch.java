package see.fa.artifactpatcher.models;

import java.io.File;

import static see.fa.artifactpatcher.util.FileUtil.replaceExtension;

public class CreatePatch {
    private String profile;
    private String file;
    private String output;

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getFile() {
        return file;
    }

    public String getOutput() {
        if (output == null) {
            output = replaceExtension(file, "patch");
        }
        return output;
    }

    public String getProfile() {
        return profile;
    }
}
