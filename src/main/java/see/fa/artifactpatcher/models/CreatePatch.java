package see.fa.artifactpatcher.models;

import com.beust.jcommander.Parameter;

import static see.fa.artifactpatcher.models.ParameterNames.*;
import static see.fa.artifactpatcher.util.FileUtil.replaceExtension;

public class CreatePatch {
    private String profile;
    private String file;
    private String output;

    @Parameter(names = {DESCRIPTION_SHORT, DESCRIPTION_LONG}, description = "The profile of the file that would be patched with the output.", required = true)
    public void setProfile(String profile) {
        this.profile = profile;
    }

    @Parameter(names = {FILE_SHORT, FILE_LONG}, description = "The file that the patch would result to after applying it to the source of the profile.", required = true)
    public void setFile(String file) {
        this.file = file;
    }

    @Parameter(names = {OUTPUT_SHORT, OUTPUT_LONG, PATCH_SHORT, PATCH_LONG}, description = "The output patch file.", required = false)
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
