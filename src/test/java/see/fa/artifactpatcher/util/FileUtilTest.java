package see.fa.artifactpatcher.util;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import see.fa.artifactpatcher.ArtifactPatcherException;

import java.io.File;
import java.io.IOException;
import java.util.SortedMap;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static see.fa.artifactpatcher.test.ClasspathUtil.getAbsolutePathFromClasspath;
import static see.fa.artifactpatcher.test.ClasspathUtil.getAllFilesAndChecksums;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        FileUtilTest.CreateTempDirectory.class
})
public class FileUtilTest {

    @RunWith(PowerMockRunner.class)
    @PrepareForTest(FileUtil.class)
    public static class CreateTempDirectory {

        public static final String DUMMY_PREFIX = "dummyPrefix";
        public static final String DUMMY_SUFFIX = "dummySuffix";

        @Test
        public void should_create_temporary_directory() {
            File tempDirectory = FileUtil.createTempDirectory(DUMMY_PREFIX, DUMMY_SUFFIX);

            assertTrue(tempDirectory.isDirectory());
            assertThat(tempDirectory.getAbsolutePath(), startsWith(System.getProperty("java.io.tmpdir")));
            assertThat(tempDirectory.getName(), allOf(
                    containsString(DUMMY_PREFIX),
                    containsString(DUMMY_SUFFIX)
            ));
        }

        @Test( expected = ArtifactPatcherException.class )
        public void given_cannot_create_temp_directory() throws IOException {
            mockStatic(File.class);
            when(File.createTempFile(anyString(), anyString())).thenThrow(new IOException("Dummy IOException that always gets thrown with File#createTempFile()"));

            FileUtil.createTempDirectory(DUMMY_PREFIX, DUMMY_SUFFIX);
        }
    }

    public static class CopyDirectory {

        @Test
        public void should_be_able_to_copy_file_from_source_to_destination() throws IOException {
            File sourceDir = new File(getAbsolutePathFromClasspath("source"));
            File targetDir = new File(getAbsolutePathFromClasspath("target"));

            FileUtil.copyDirectory(sourceDir, targetDir, FileFilterUtils.trueFileFilter());

            SortedMap<String,String> sourceFiles = getAllFilesAndChecksums(sourceDir);
            SortedMap<String,String> targetFiles = getAllFilesAndChecksums(targetDir);

            assertArrayEquals(sourceFiles.entrySet().toArray(), targetFiles.entrySet().toArray());
        }

        @Test( expected = ArtifactPatcherException.class )
        public void given_cannot_create_temp_directory() throws IOException {
            File sourceDir = new File(getAbsolutePathFromClasspath("source"));
            File targetDir = new File(getAbsolutePathFromClasspath("targetFile"));
            targetDir.delete();
            targetDir.createNewFile();

            assertTrue("[GUARD] 'Target dir' should exist", targetDir.exists());
            assertFalse("[GUARD] 'Target dir' should NOT be a directory.", targetDir.isDirectory());

            FileUtil.copyDirectory(sourceDir, targetDir, FileFilterUtils.trueFileFilter());
        }
    }
}
