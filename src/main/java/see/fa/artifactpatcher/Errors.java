package see.fa.artifactpatcher;

public class Errors {

    public static final String UNABLE_TO_READ_ARTIFACT = "[ARTIFACTPATCHER_ERR001] Unable to read JAR/EAR/WAR/ZIP from dir '%s'.";

    public static final String UNABLE_TO_READ_INPUT_STREAM_FOR_CHECKSUM = "[ARTIFACTPATCHER_ERR002] Unable to read input stream to generate checksum on.";

    public static final String UNABLE_TO_CREATE_TEMP_WORKING_DIR = "[ARTIFACTPATCHER_ERR003] Unable to create temp working directory at '%s'.";

    public static final String UNABLE_TO_COPY_DIRECTORY = "[ARTIFACTPATCHER_ERR004] Unable to copy from '%s' to '%s'.";

    public static final String UNABLE_TO_CREATE_MESSAGE_DIGEST = "[ARTIFACTPATCHER_ERR005] Unable to create MessageDigest for algorithm '%s'.";

    public static final String UNABLE_TO_WRITE_ARTIFACT_DESCRIPTION = "[ARTIFACTPATCHER_ERR006] Unable to write artifact description to %s.";

    public static final String UNABLE_TO_ZIP = "[ARTIFACTPATCHER_ERR007] Unable to create zip file '%s' from directory '%s'.";

    public static final String UNABLE_TO_UNZIP = "[ARTIFACTPATCHER_ERR008] Unable to extract zip file '%s' to directory '%s'.";

}
