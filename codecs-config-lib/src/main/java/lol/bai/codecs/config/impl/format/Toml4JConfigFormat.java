package lol.bai.codecs.config.impl.format;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import lol.bai.codecs.config.api.format.ConfigFormat;
import lol.bai.codecs.config.api.ops.ObjectOps;

public class Toml4JConfigFormat extends ConfigFormat<Object> {

    private static final TomlWriter WRITER = new TomlWriter();

    public Toml4JConfigFormat() {
        super(ObjectOps.INSTANCE);
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
        WRITER.write(o, stream);
    }

}
