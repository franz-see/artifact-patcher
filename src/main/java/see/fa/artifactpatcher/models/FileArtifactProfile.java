package see.fa.artifactpatcher.models;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class FileArtifactProfile {
    private final String name;
    private final String checksum;

    public FileArtifactProfile(String name, String checksum) {
        this.name = name;
        this.checksum = checksum;
    }

    public String getName() {
        return name;
    }

    public String getChecksum() {
        return checksum;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getName())
                .append(getChecksum())
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!FileArtifactProfile.class.isInstance(o)) {
            return false;
        }
        FileArtifactProfile that = (FileArtifactProfile) o;
        return new EqualsBuilder()
                .append(this.getName(), that.getName())
                .append(this.getChecksum(), that.getChecksum())
                .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", getName())
                .append("checksum", getChecksum())
                .toString();
    }
}
