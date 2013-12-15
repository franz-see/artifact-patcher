package see.fa.artifactpatcher.models;

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
        return output;
    }

    public String getProfile() {
        return profile;
    }
}
