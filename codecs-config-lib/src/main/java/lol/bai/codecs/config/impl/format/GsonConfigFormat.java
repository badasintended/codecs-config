package lol.bai.codecs.config.impl.format;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import lol.bai.codecs.config.api.format.ConfigFormat;

public class GsonConfigFormat extends ConfigFormat<JsonElement> {

    private final Gson gson;

    public GsonConfigFormat(Gson gson) {
        super(JsonOps.INSTANCE);
        this.gson = gson;
    }

    @Override
    protected JsonElement read(InputStream stream) throws IOException {
        try {
            return JsonParser.parseReader(new InputStreamReader(stream));
        } catch (JsonParseException e) {
            throw new IOException(e);
        }
    }

    @Override
    protected void write(OutputStream stream, JsonElement jsonElement) throws IOException {
        stream.write(gson.toJson(jsonElement).getBytes(StandardCharsets.UTF_8));
    }

}
