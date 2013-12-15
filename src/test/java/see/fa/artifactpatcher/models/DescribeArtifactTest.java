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
        DescribeArtifactTest.GetOutput.class
})
public class DescribeArtifactTest {

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
                    {"Given output is explicit, then return output.", "dummy-file.jar", "dummy-output.xml", "dummy-output.xml"},
                    {"Given output does NOT exist and file exists, then derive output from file.", "dummy-file.jar", null, "dummy-file.xml"},
                    {"Given output does NOT exist and file exists but has NO extensions name, then derive output from file.", "no extension name", null, "no extension name.xml"},
                    {"Given file and output both does NOT exist, then return null.", null, null, null}
            });
        }

        @Test
        public void should_resolve_effective_output() {
            DescribeArtifact describeArtifact = new DescribeArtifact();
            describeArtifact.setFile(file);
            describeArtifact.setOutput(output);

            String actualOutput = describeArtifact.getOutput();

            assertEquals(message, expectedOutput, actualOutput);
        }
    }
}
