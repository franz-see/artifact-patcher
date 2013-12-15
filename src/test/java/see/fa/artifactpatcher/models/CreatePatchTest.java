package see.fa.artifactpatcher.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Suite;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CreatePatchTest.GetOutput.class
})
public class CreatePatchTest {

    @RunWith(Parameterized.class)
    public static class GetOutput {
        private final String message;
        private final String file;
        private final String output;
        private final String expectedOutput;

        public GetOutput(String message, String file, String output, String expectedOutput) {
            this.message = message;
            this.file = file;
            this.output = output;
            this.expectedOutput = expectedOutput;
        }

        @Parameterized.Parameters(name = "Test #{index} : {0}")
        public static List<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    {"Given output is explicit, then return output.", "dummy-file.jar", "dummy-output.patch", "dummy-output.patch"},
                    {"Given output does NOT exist and file exists, then derive output from file.", "dummy-file.jar", null, "dummy-file.patch"},
                    {"Given output does NOT exist and file exists but has NO extensions name, then derive output from file.", "no extension name", null, "no extension name.patch"},
                    {"Given file and output both does NOT exist, then return null.", null, null, null}
            });
        }

        @Test
        public void should_resolve_effective_output() {
            CreatePatch createPatch = new CreatePatch();
            createPatch.setFile(file);
            createPatch.setOutput(output);

            String actualOutput = createPatch.getOutput();

            assertEquals(message, expectedOutput, actualOutput);
        }
    }
}
