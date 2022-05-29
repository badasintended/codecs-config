package lol.bai.codecs.config.api.format;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.nbt.visitor.NbtOrderedStringFormatter;
import org.apache.commons.io.IOUtils;

public class SnbtConfigFormat extends ConfigFormat<NbtElement> {

    public static final SnbtConfigFormat INSTANCE = new SnbtConfigFormat();

    private SnbtConfigFormat() {
        super(NbtOps.INSTANCE);
    }

    @Override
    protected NbtElement read(InputStream stream) throws IOException {
        try {
            return StringNbtReader.parse(IOUtils.toString(stream, StandardCharsets.UTF_8));
        } catch (CommandSyntaxException e) {
            throw new IOException(e);
        }
    }

    @Override
    protected void write(OutputStream stream, NbtElement nbtElement) throws IOException {
        IOUtils.write(new NbtOrderedStringFormatter().apply(nbtElement), stream, StandardCharsets.UTF_8);
    }

}
