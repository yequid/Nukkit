package cn.nukkit.pack;

import cn.nukkit.Nukkit;
import cn.nukkit.utils.SemVersion;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
public final class PackManifest {

    @JsonProperty("format_version")
    private String formatVersion;

    private Header header;

    private List<Module> modules = Collections.emptyList();

    private Metadata metadata;

    private List<Dependency> dependencies = Collections.emptyList();

    public static PackManifest load(InputStream stream) throws IOException {
        return Nukkit.JSON_MAPPER.readValue(stream, PackManifest.class);
    }

    public boolean isValid() {
        if (this.formatVersion != null && this.header != null && this.modules != null) {
            return header.description != null &&
                    header.name != null &&
                    header.uuid != null &&
                    header.version != null;
        } else {
            return false;
        }
    }

    @Data
    public static class Header {
        private String name;
        private String description;
        private UUID uuid;
        @JsonProperty("platform_locked")
        private boolean platformLocked;
        private SemVersion version;
        @JsonProperty("min_engine_version")
        private SemVersion minEngineVersion;
    }

    @Data
    public static class Module {
        private UUID uuid;
        private String description;
        private SemVersion version;
        private PackType type;
    }

    @Data
    public static class Metadata {
        private List<String> authors;
        private String license;
        private String url;
    }

    @Data
    public static class Dependency {
        private UUID uuid;
        private SemVersion version;
    }
}
