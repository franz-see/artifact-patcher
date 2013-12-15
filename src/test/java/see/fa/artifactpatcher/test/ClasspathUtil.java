package see.fa.artifactpatcher.test;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import see.fa.artifactpatcher.util.ChecksumUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.apache.commons.io.filefilter.TrueFileFilter.TRUE;

public class ClasspathUtil {

    public static File getFileFromClasspath(String path) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(path);
        return url != null ? new File(url.getFile()) : null;
    }

    @SuppressWarnings("all")
    public static String getAbsolutePathFromClasspath(String path) {
        URL currentPath = Thread.currentThread().getContextClassLoader().getResource(".");
        return new File(currentPath.getFile(), path).getAbsolutePath();
    }

    public static String readFromClasspath(String path) throws IOException {
        File file = getFileFromClasspath(path);
        return file != null ? FileUtils.readFileToString(file) : null;
    }

    public static SortedMap<String, String> getAllFilesAndChecksums(String dirPath) throws IOException {
        return getAllFilesAndChecksums(new File(dirPath));
    }

    public static SortedMap<String, String> getAllFilesAndChecksums(File directory) throws IOException {
        SortedMap<String, String> allFilesAndCheckums = new TreeMap<String, String>();

        Iterator<File> fileIterator = FileUtils.iterateFiles(directory, TRUE, TRUE);
        while(fileIterator.hasNext()) {
            File file = fileIterator.next();
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
                allFilesAndCheckums.put(file.getName(), ChecksumUtil.shasum(inputStream));
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
        }

        return allFilesAndCheckums;
    }
}
