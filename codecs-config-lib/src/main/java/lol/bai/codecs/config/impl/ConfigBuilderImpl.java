package lol.bai.codecs.config.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.function.Supplier;

import com.google.common.io.ByteStreams;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import lol.bai.codecs.config.api.ConfigBuilder.DefaultArg;
import lol.bai.codecs.config.api.ConfigBuilder.FormatArg;
import lol.bai.codecs.config.api.ConfigBuilder.OptionalArgs;
import lol.bai.codecs.config.api.ConfigBuilder.PathArg;
import lol.bai.codecs.config.api.ConfigHolder;
import lol.bai.codecs.config.api.format.ConfigFormat;

public class ConfigBuilderImpl<T> implements FormatArg<T>, PathArg<T>, DefaultArg<T>, OptionalArgs<T> {

    private final Codec<T> codec;

    private ConfigFormat<?> format;
    private Path path;
    private ConfigHolderImpl.DefaultFactory<T> defaultFactory;

    public ConfigBuilderImpl(Codec<T> codec) {
        this.codec = codec;
    }

    @Override
    public DefaultArg<T> path(Path path) {
        this.path = path;
        return this;
    }

    @Override
    public PathArg<T> format(ConfigFormat<?> format) {
        this.format = format;
        return this;
    }

    @Override
    public OptionalArgs<T> defaultFactory(Supplier<T> factory) {
        this.defaultFactory = stream -> {
            T value = factory.get();
            format.write(stream, codec, value);
            return value;
        };
        return this;
    }

    @Override
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public OptionalArgs<T> defaultStream(Supplier<InputStream> streamSupplier) {
        this.defaultFactory = outputStream -> {
            try (InputStream inputStream = streamSupplier.get()) {
                ByteStreams.copy(inputStream, outputStream);
            }

            try (InputStream inputStream = streamSupplier.get()) {
                DataResult<T> result = format.read(inputStream, codec);

                if (result.result().isPresent()) {
                    return result.result().get();
                }

                throw new IOException("Failed to read default config for " + path + ": " + result.error().get().message());
            }
        };
        return this;
    }

    @Override
    public ConfigHolder<T> build() {
        return new ConfigHolderImpl<>(codec, path, format, defaultFactory);
    }

}
