package see.fa.artifactpatcher;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import see.fa.artifactpatcher.models.ApplyPatch;
import see.fa.artifactpatcher.models.CreateArtifactProfile;
import see.fa.artifactpatcher.models.CreatePatch;

import java.io.File;
import java.io.IOException;
import java.util.SortedMap;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static see.fa.artifactpatcher.test.ClasspathUtil.getAbsolutePathFromClasspath;
import static see.fa.artifactpatcher.test.ClasspathUtil.getAllFilesAndChecksums;
import static see.fa.artifactpatcher.test.ClasspathUtil.readFromClasspath;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ArtifactPatcherTest.Execute_CreateArtifactProfile.class,
        ArtifactPatcherTest.Execute_CreatePatch.class,
        ArtifactPatcherTest.Execute_ApplyPatch.class
})
public class ArtifactPatcherTest {

    public static abstract class AbstractArtifactPatcherTestBase {

        protected ArtifactPatcher artifactPatcher;

        @Before
        public void given() {
            artifactPatcher = new ArtifactPatcher();
        }

    }

    public static class Execute_CreateArtifactProfile extends AbstractArtifactPatcherTestBase {

        @Test
        public void should_unzip_jar_and_create_checksums_for_every_content() throws IOException {
            CreateArtifactProfile createArtifactProfile = new CreateArtifactProfile();
            createArtifactProfile.setFile(getAbsolutePathFromClasspath("test.jar"));
            createArtifactProfile.setOutput(getAbsolutePathFromClasspath("test.xml"));

            artifactPatcher.execute(createArtifactProfile);

            String actualOutput = FileUtils.readFileToString(new File(createArtifactProfile.getOutput()));
            String expectedOutput = readFromClasspath("expected-test.xml");

            assertEquals(expectedOutput, actualOutput);
        }

        @Test(expected = ArtifactPatcherException.class)
        public void given_problem_with_file_then_throw_exception() throws IOException {
            CreateArtifactProfile artifactProfile = new CreateArtifactProfile();
            artifactProfile.setFile("this does not exist");

            artifactPatcher.execute(artifactProfile);
        }
    }

    public static class Execute_CreatePatch extends AbstractArtifactPatcherTestBase {

        @Test
        public void create_artifact_patch_based_on_those_that_do_NOT_match() throws IOException, ZipException {
            CreatePatch createPatch = new CreatePatch();
            createPatch.setProfile(getAbsolutePathFromClasspath("profile.xml"));
            createPatch.setFile(getAbsolutePathFromClasspath("test2.jar"));
            createPatch.setOutput(getAbsolutePathFromClasspath("test2.jar.patch"));

            artifactPatcher.execute(createPatch);

            String actualPatchContents = getAbsolutePathFromClasspath("actual-test2-contents");
            FileUtils.deleteDirectory(new File(actualPatchContents));

            ZipFile zipFile = new ZipFile(createPatch.getOutput());
            zipFile.extractAll(actualPatchContents);

            SortedMap<String,String> actual = getAllFilesAndChecksums(actualPatchContents);
            SortedMap<String, String> expected = getAllFilesAndChecksums(getAbsolutePathFromClasspath("expected-test2-contents"));

            assertArrayEquals(expected.entrySet().toArray(), actual.entrySet().toArray());
        }
    }

    public static class Execute_ApplyPatch extends AbstractArtifactPatcherTestBase {

        @Test
        public void should_apply_patch_on_a_jar() throws ZipException, IOException {
            ApplyPatch applyPatch = new ApplyPatch();
            applyPatch.setFile(getAbsolutePathFromClasspath("test.jar"));
            applyPatch.setPatch(getAbsolutePathFromClasspath("test.patch"));
            applyPatch.setOutput(getAbsolutePathFromClasspath("actual-patched-test.jar"));

            artifactPatcher.execute(applyPatch);

            String actualPatchedContents = getAbsolutePathFromClasspath("actual-patched-test");

            ZipFile zipFile = new ZipFile(applyPatch.getOutput());
            zipFile.extractAll(actualPatchedContents);

            SortedMap<String,String> actual = getAllFilesAndChecksums(actualPatchedContents);
            SortedMap<String, String> expected = getAllFilesAndChecksums(getAbsolutePathFromClasspath("expected-patched-test"));

            assertArrayEquals(expected.entrySet().toArray(), actual.entrySet().toArray());
        }
    }
}
