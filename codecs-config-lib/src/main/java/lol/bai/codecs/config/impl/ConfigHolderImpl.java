package lol.bai.codecs.config.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import lol.bai.codecs.config.api.ConfigHolder;
import lol.bai.codecs.config.api.format.ConfigFormat;
import org.jetbrains.annotations.Nullable;

public class ConfigHolderImpl<T> implements ConfigHolder<T> {

    private final Codec<T> codec;
    private final Path path;
    private final ConfigFormat<?> format;
    private final DefaultFactory<T> defaultFactory;

    @Nullable
    private T value = null;

    ConfigHolderImpl(Codec<T> codec, Path path, ConfigFormat<?> format, DefaultFactory<T> defaultFactory) {
        this.path = path;
        this.codec = codec;
        this.format = format;
        this.defaultFactory = defaultFactory;
    }

    @Override
    public T get() {
        if (value == null) {
            if (Files.exists(path)) {
                try (InputStream stream = Files.newInputStream(path)) {
                    DataResult<T> result = format.read(stream, codec);

                    if (result.result().isPresent()) {
                        value = result.result().get();
                    } else if (result.error().isPresent()) {
                        throw new RuntimeException("Error when reading file \"" + path + "\": " + result.error().get().message());
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Error when reading file \"" + path + "\"", e);
                }
            } else {
                Path parent = path.getParent();

                if (!Files.exists(parent)) {
                    try {
                        Files.createDirectories(parent);
                    } catch (IOException e) {
                        throw new RuntimeException("Error when creating directories \"" + parent + "\"", e);
                    }
                }

                try (OutputStream stream = Files.newOutputStream(path)) {
                    value = defaultFactory.getAndWriteDefault(stream);
                } catch (IOException e) {
                    throw new RuntimeException("Error when writing file \"" + path + "\"", e);
                }
            }
        }

        return Objects.requireNonNull(value, "Config value is somehow null");
    }

    @Override
    public void invalidate() {
        value = null;
    }

    @FunctionalInterface
    interface DefaultFactory<T> {

        T getAndWriteDefault(OutputStream stream) throws IOException;

    }

}
