package lol.bai.codecs.config.api.format;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

public abstract class ConfigFormat<T> {

    private final DynamicOps<T> ops;

    protected ConfigFormat(DynamicOps<T> ops) {
        this.ops = ops;
    }

    public final <R> DataResult<R> read(InputStream stream, Codec<R> codec) throws IOException {
        return codec.decode(ops, read(stream)).map(Pair::getFirst);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public final <I> void write(OutputStream stream, Codec<I> codec, I input) throws IOException {
        DataResult<T> result = codec.encodeStart(ops, input);

        if (result.result().isPresent()) {
            write(stream, result.result().get());
        } else {
            throw new IOException("Failed to encode object: " + result.error().get().message());
        }
    }


    protected abstract T read(InputStream stream) throws IOException;

    protected abstract void write(OutputStream stream, T t) throws IOException;

}
