package see.fa.artifactpatcher.models;

import com.beust.jcommander.Parameter;
import org.apache.commons.lang.StringUtils;

import static org.apache.commons.io.FilenameUtils.getExtension;
import static see.fa.artifactpatcher.util.FileUtil.replaceExtension;

public class ApplyPatch {

    private String file;
    private String patch;
    private String output;

    @Parameter(names = {"-f", "--file"}, description = "The file that would be patched.", required = true)
    public void setFile(String file) {
        this.file = file;
    }

    @Parameter(names = {"-p", "--patch"}, description = "The patch to be applied to the file.", required = true)
    public void setPatch(String patch) {
        this.patch = patch;
    }

    @Parameter(names = {"-o", "--output"}, description = "The output filename of the patched jar.", required = false)
    public void setOutput(String output) {
        this.output = output;
    }

    public String getOutput() {
        if (output == null) {
            String extension = getExtension(file);
            String newExtension = StringUtils.isNotEmpty(extension) ? String.format("patched.%s", extension) : "patched";
            output = replaceExtension(file, newExtension);
        }
        return output;
    }

    public String getFile() {
        return file;
    }

    public String getPatch() {
        return patch;
    }
}
