package see.fa.artifactpatcher.models;

public class ApplyPatch {

    private String file;
    private String patch;
    private String output;

    public void setFile(String file) {
        this.file = file;
    }

    public void setPatch(String patch) {
        this.patch = patch;
    }


    public void setOutput(String output) {
        this.output = output;
    }

    public String getOutput() {
        return output;
    }

    public String getFile() {
        return file;
    }

    public String getPatch() {
        return patch;
    }
}
