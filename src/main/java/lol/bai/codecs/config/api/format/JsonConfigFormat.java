package lol.bai.codecs.config.api.format;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import org.apache.commons.io.IOUtils;

public class JsonConfigFormat extends ConfigFormat<JsonElement> {

    public static final JsonConfigFormat DEFAULT = new JsonConfigFormat(new GsonBuilder().setPrettyPrinting().create());

    private final Gson gson;

    public JsonConfigFormat(Gson gson) {
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
        IOUtils.write(gson.toJson(jsonElement), stream, StandardCharsets.UTF_8);
    }

}
