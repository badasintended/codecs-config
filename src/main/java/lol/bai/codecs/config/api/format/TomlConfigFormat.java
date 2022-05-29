package lol.bai.codecs.config.api.format;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import lol.bai.codecs.config.api.serialization.ObjectOps;

public class TomlConfigFormat extends ConfigFormat<Object> {

    public static final TomlConfigFormat DEFAULT = new TomlConfigFormat(new TomlWriter());

    private final TomlWriter writer;

    public TomlConfigFormat(TomlWriter writer) {
        super(ObjectOps.INSTANCE);
        this.writer = writer;
    }

    @Override
    protected Object read(InputStream stream) throws IOException {
        try {
            return new Toml().read(stream).toMap();
        } catch (IllegalStateException e) {
            throw new IOException(e);
        }
    }

    @Override
    protected void write(OutputStream stream, Object o) throws IOException {
        writer.write(o, stream);
    }

}
