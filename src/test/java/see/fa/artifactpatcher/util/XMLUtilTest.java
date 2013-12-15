package see.fa.artifactpatcher.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import see.fa.artifactpatcher.ArtifactPatcherException;
import see.fa.artifactpatcher.models.ArtifactDescription;
import see.fa.artifactpatcher.models.FileArtifactDescription;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;
import static see.fa.artifactpatcher.test.ClasspathUtil.getAbsolutePathFromClasspath;
import static see.fa.artifactpatcher.test.ClasspathUtil.readFromClasspath;

@RunWith(Suite.class)
@Suite.SuiteClasses(
        XMLUtilTest.WriteXML.class
)
public class XMLUtilTest {

    public static class WriteXML {
        @Test
        public void should_properly_convert_to_xml() throws IOException {
            ArtifactDescription artifactDescription = new ArtifactDescription();
            artifactDescription.getFiles().add(new FileArtifactDescription("dummy1", "checksum1"));
            artifactDescription.getFiles().add(new FileArtifactDescription("dummy2", "checksum2"));

            File outputFile = new File(getAbsolutePathFromClasspath("actual-dummy.txt"));

            XMLUtil.writeXML(artifactDescription, outputFile);

            assertEquals(readFromClasspath("expected-dummy.xml"), readFromClasspath("actual-dummy.txt"));
        }

        @SuppressWarnings("all")
        @Test(expected = ArtifactPatcherException.class)
        public void given_non_existent_file() throws IOException {
            File outputFile = new File(getAbsolutePathFromClasspath("non-existent dummy file"));
            outputFile.delete();
            assertFalse("[GUARD] File should NOT exist.", outputFile.exists());

            outputFile.mkdirs();
            assertTrue("[GUARD] File should be a directory so that it cannot be written bytes.", outputFile.isDirectory());

            XMLUtil.writeXML(new ArtifactDescription(), outputFile);
        }

    }
}
