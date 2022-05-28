package lol.bai.codecs.config.api.format;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lol.bai.codecs.config.impl.format.GsonConfigFormat;
import lol.bai.codecs.config.impl.format.Toml4JConfigFormat;

public final class ConfigFormats {

    public static final ConfigFormat<?> TOML = new Toml4JConfigFormat();
    public static final ConfigFormat<?> JSON = json(new GsonBuilder().setPrettyPrinting().create());

    public static ConfigFormat<?> json(Gson gson) {
        return new GsonConfigFormat(gson);
    }

    private ConfigFormats() {
    }

}
