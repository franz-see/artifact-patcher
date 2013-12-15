package see.fa.artifactpatcher.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import org.apache.commons.io.IOUtils;
import see.fa.artifactpatcher.ArtifactPatcherException;
import see.fa.artifactpatcher.models.ArtifactProfile;
import see.fa.artifactpatcher.models.FileArtifactProfile;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class XMLUtil {

    private static final XStream X_STREAM;

    static {
        X_STREAM = new XStream(new StaxDriver());
        X_STREAM.alias("artifactProfile", ArtifactProfile.class);
        X_STREAM.alias("files", List.class, LinkedList.class);
        X_STREAM.alias("file", FileArtifactProfile.class);
    }

    public static void write(Object object, File outputFile) {
        Writer out = null;
        try {
            X_STREAM.marshal(object, new PrettyPrintWriter(new FileWriter(outputFile)));
        } catch (IOException e) {
            throw new ArtifactPatcherException(String.format("Unable to write artifact profile to %s.", outputFile.getAbsolutePath()), e);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public static <T> void read(File inputFile, T object) {
        X_STREAM.fromXML(inputFile, object);
    }
}
