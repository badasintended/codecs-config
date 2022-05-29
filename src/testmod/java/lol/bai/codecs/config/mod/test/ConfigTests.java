package lol.bai.codecs.config.mod.test;

import java.util.List;
import java.util.Map;

import lol.bai.codecs.config.api.ConfigHolder;
import lol.bai.codecs.config.api.format.JsonConfigFormat;
import lol.bai.codecs.config.api.format.SnbtConfigFormat;
import lol.bai.codecs.config.api.format.TomlConfigFormat;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ConfigTests implements ModInitializer {

    private static final ConfigHolder<Config> JSON_CONFIG = ConfigHolder.builder(Config.CODEC)
        .format(JsonConfigFormat.DEFAULT)
        .path(FabricLoader.getInstance().getConfigDir().resolve("codecs-config/test.json"))
        .defaultFactory(() -> new Config(
            245,
            1280.3,
            true,
            Items.DIAMOND,
            new ItemStack(Items.ITEM_FRAME, 2),
            new Config.Nested(
                List.of("afefea", "fafegwh", "fahofie", "afa"),
                List.of(new ItemStack(Items.CREEPER_HEAD, 22), new ItemStack(Items.BEACON, 8)),
                Map.of(
                    "k1", "v1",
                    "k2", "v2"
                ))))
        .build();

    private static final ConfigHolder<Config> TOML_CONFIG = ConfigHolder.builder(Config.CODEC)
        .format(TomlConfigFormat.DEFAULT)
        .path(FabricLoader.getInstance().getConfigDir().resolve("codecs-config/test.toml"))
        .defaultStream(() -> ConfigTests.class.getResourceAsStream("/default_config/test.toml"))
        .build();

    private static final ConfigHolder<Config> SNBT_CONFIG = ConfigHolder.builder(Config.CODEC)
        .format(SnbtConfigFormat.INSTANCE)
        .path(FabricLoader.getInstance().getConfigDir().resolve("codecs-config/test.snbt"))
        .defaultFactory(() -> new Config(
            245,
            1280.3,
            true,
            Items.DIAMOND,
            new ItemStack(Items.ITEM_FRAME, 2),
            new Config.Nested(
                List.of("afefea", "fafegwh", "fahofie", "afa"),
                List.of(new ItemStack(Items.CREEPER_HEAD, 22), new ItemStack(Items.BEACON, 8)),
                Map.of(
                    "k1", "v1",
                    "k2", "v2"
                ))))
        .build();

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            JSON_CONFIG.get();
            TOML_CONFIG.get();
            SNBT_CONFIG.get();
        });
    }

}
