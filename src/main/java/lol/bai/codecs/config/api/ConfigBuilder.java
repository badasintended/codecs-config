package lol.bai.codecs.config.api;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.function.Supplier;

import lol.bai.codecs.config.api.format.ConfigFormat;

public final class ConfigBuilder {

    public interface FormatArg<T> {

        /**
         * Specifies what format the config file use.
         */
        PathArg<T> format(ConfigFormat<?> format);

    }

    public interface PathArg<T> {

        /**
         * Specifies where the config file located.
         */
        DefaultArg<T> path(Path path);

    }

    public interface DefaultArg<T> {

        /**
         * Specifies the default object that will be used when config file is not present.
         * <p>
         * The object will be serialized and written to the file.
         */
        OptionalArgs<T> defaultFactory(Supplier<T> factory);

        /**
         * Specifies the default stream that will be used when config file is not present.
         * <p>
         * The input stream will be copied to the config file first, then deserialized.
         */
        OptionalArgs<T> defaultStream(Supplier<InputStream> streamSupplier);

    }

    public interface OptionalArgs<T> {

        ConfigHolder<T> build();

    }

    private ConfigBuilder() {
    }

}
