package lol.bai.codecs.config.api;

import com.mojang.serialization.Codec;
import lol.bai.codecs.config.impl.ConfigBuilderImpl;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface ConfigHolder<T> {

    static <T> ConfigBuilder.FormatArg<T> builder(Codec<T> codec) {
        return new ConfigBuilderImpl<>(codec);
    }

    /**
     * Get the current config.
     */
    T get();

    /**
     * Invalidate the config, forcing re-read on next {@link #get()} call.
     */
    void invalidate();

}
