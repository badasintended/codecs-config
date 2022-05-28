package lol.bai.codecs.config.mod.test;

import java.util.List;
import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;

public record Config(
    int intVal,
    double doubleVal,
    Item item,
    ItemStack stack,
    Nested nested
) {

    public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("int").forGetter(Config::intVal),
        Codec.DOUBLE.fieldOf("double").forGetter(Config::doubleVal),
        Registry.ITEM.getCodec().fieldOf("item").forGetter(Config::item),
        ItemStack.CODEC.fieldOf("stack").forGetter(Config::stack),
        Nested.CODEC.fieldOf("nested").forGetter(Config::nested)
    ).apply(instance, Config::new));

    public record Nested(
        List<String> stringList,
        List<ItemStack> stackList,
        Map<String, String> map
    ) {

        public static final Codec<Nested> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(Codec.STRING).fieldOf("string_list").forGetter(Nested::stringList),
            Codec.list(ItemStack.CODEC).fieldOf("stack_list").forGetter(Nested::stackList),
            Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("map").forGetter(Nested::map)
        ).apply(instance, Nested::new));

    }

}
