package lol.bai.codecs.config.api.format;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.electronwill.toml.Toml;
import lol.bai.codecs.config.api.serialization.ObjectOps;

public class TomlConfigFormat extends ConfigFormat<Object> {

    public static final TomlConfigFormat INSTANCE = new TomlConfigFormat();

    private TomlConfigFormat() {
        super(ObjectOps.INSTANCE);
    }

    @Override
    protected Object read(InputStream stream) throws IOException {
        try {
            return Toml.read(stream);
        } catch (IllegalStateException e) {
            throw new IOException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void write(OutputStream stream, Object o) throws IOException {
        if (!(o instanceof Map)) {
            throw new IllegalArgumentException("Object is not a map");
        }

        Toml.write((Map<String, Object>) o, stream);
    }

}
