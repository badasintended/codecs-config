package lol.bai.codecs.config.mod.test;

import java.util.List;
import java.util.Map;

import lol.bai.codecs.config.api.ConfigHolder;
import lol.bai.codecs.config.api.format.ConfigFormats;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ConfigTests implements ModInitializer {

    private static final ConfigHolder<Config> JSON_CONFIG = ConfigHolder.builder(Config.CODEC)
        .format(ConfigFormats.JSON)
        .path(FabricLoader.getInstance().getConfigDir().resolve("codecs-config/test.json"))
        .defaultFactory(() -> new Config(
            245,
            1280.3,
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
        .format(ConfigFormats.TOML)
        .path(FabricLoader.getInstance().getConfigDir().resolve("codecs-config/test.toml"))
        .defaultStream(() -> ConfigTests.class.getResourceAsStream("/default_config/test.toml"))
        .build();

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            JSON_CONFIG.get();
            TOML_CONFIG.get();
        });
    }

}
