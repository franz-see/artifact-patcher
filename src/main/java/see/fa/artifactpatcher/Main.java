package see.fa.artifactpatcher;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import see.fa.artifactpatcher.models.ApplyPatch;
import see.fa.artifactpatcher.models.CreateArtifactProfile;
import see.fa.artifactpatcher.models.CreatePatch;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Object arguments = buildArguments(args);

        ArtifactPatcher artifactPatcher = new ArtifactPatcher();

        if (CreateArtifactProfile.class.isInstance(arguments)) {
            artifactPatcher.execute((CreateArtifactProfile)arguments);
        } else if (CreatePatch.class.isInstance(arguments)) {
            artifactPatcher.execute((CreatePatch)arguments);
        } else if (ApplyPatch.class.isInstance(arguments)) {
            artifactPatcher.execute((ApplyPatch)arguments);
        }
    }

    private static Object buildArguments(String[] args) {
        Object argumentObject = null;
        if ("profile".equals(args[0])) {
            argumentObject = new CreateArtifactProfile();
        } else if ("diff".equals(args[0])) {
            argumentObject = new CreatePatch();
        } else if ("patch".equals(args[0])) {
            argumentObject = new ApplyPatch();
        }

        String[] subArgs = new String[args.length-1];
        System.arraycopy(args, 1, subArgs, 0, subArgs.length);

        return buildArguments(argumentObject, subArgs);
    }

    private static Object buildArguments(Object arguments, String[] args) {
        try {
            new JCommander(arguments, args);
            return arguments;
        } catch (ParameterException ex) {
            ex.printStackTrace();
            new JCommander(new CreateArtifactProfile()).usage();
            return null;
        }
    }
}
