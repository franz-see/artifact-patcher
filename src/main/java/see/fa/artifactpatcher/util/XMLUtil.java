package see.fa.artifactpatcher.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import org.apache.commons.io.IOUtils;
import see.fa.artifactpatcher.ArtifactPatcherException;
import see.fa.artifactpatcher.Errors;
import see.fa.artifactpatcher.models.ArtifactProfile;
import see.fa.artifactpatcher.models.FileArtifactProfile;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import static see.fa.artifactpatcher.Errors.UNABLE_TO_WRITE_ARTIFACT_DESCRIPTION;

public class XMLUtil {

    private static final XStream X_STREAM;

    static {
        X_STREAM = new XStream(new StaxDriver());
        X_STREAM.alias("artifactProfile", ArtifactProfile.class);
        X_STREAM.alias("files", List.class, LinkedList.class);
        X_STREAM.alias("file", FileArtifactProfile.class);
    }

    public static void writeXML(Object object, File outputFile) {
        Writer out = null;
        try {
            X_STREAM.marshal(object, new PrettyPrintWriter(new FileWriter(outputFile)));
        } catch (IOException e) {
            throw new ArtifactPatcherException(String.format(UNABLE_TO_WRITE_ARTIFACT_DESCRIPTION, outputFile.getAbsolutePath()), e);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public static <T> void readXML(File inputFile, T object) {
        X_STREAM.fromXML(inputFile, object);
    }
}
