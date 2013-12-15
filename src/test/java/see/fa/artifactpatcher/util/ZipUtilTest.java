package see.fa.artifactpatcher.util;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.SortedMap;

import static org.junit.Assert.assertArrayEquals;
import static see.fa.artifactpatcher.test.ClasspathUtil.getAbsolutePathFromClasspath;
import static see.fa.artifactpatcher.test.ClasspathUtil.getAllFilesAndChecksums;

public class ZipUtilTest {

    @Test
    public void should_be_able_to_unzip_what_was_zipped() throws IOException {
        String sourcePath = getAbsolutePathFromClasspath("source");
        String jar = getAbsolutePathFromClasspath("source.jar");
        ZipUtil.zip(new File(sourcePath), jar);
        String explodedJar = getAbsolutePathFromClasspath("source2");
        ZipUtil.unzip(new File(jar), new File(explodedJar));

        SortedMap<String,String> actual = getAllFilesAndChecksums(sourcePath);
        SortedMap<String, String> expected = getAllFilesAndChecksums(explodedJar);

        assertArrayEquals(expected.entrySet().toArray(), actual.entrySet().toArray());
    }
}
