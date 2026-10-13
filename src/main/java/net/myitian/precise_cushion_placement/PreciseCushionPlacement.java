package net.myitian.precise_cushion_placement;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.ItemStack;
import net.myitian.precise_cushion_placement.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.function.Supplier;


public final class PreciseCushionPlacement {
    public static final String MOD_ID = "precise_cushion_placement";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final Style ENUM_VALUE = Style.EMPTY
        .withColor(ChatFormatting.YELLOW)
        .withUnderlined(true);
    public static double yOffset = Double.NaN;
    private static File configFile;

    public static void init(Supplier<Path> configDirectorySupplier) {
        configFile = configDirectorySupplier.get().resolve(MOD_ID + ".json").toFile();
        reloadConfig();
    }

    public static void reloadConfig() {
        if (!loadConfig()) {
            saveConfig();
        }
    }

    public static boolean loadConfig() {
        return Config.load(configFile);
    }

    public static void saveConfig() {
        Config.save(configFile);
    }

    public static double getYOffset(ItemStack cushionStack, ItemStack offhandStack) {
        for (ValueSource source : Config.valueSources) {
            switch (source) {
                case CUSHION_ITEM_NAME -> {
                    double offset = getDoubleFromCustomName(cushionStack);
                    if (Double.isFinite(offset)) {
                        return offset;
                    }
                }
                case OFFHAND_ITEM_NAME -> {
                    double offset = getDoubleFromCustomName(offhandStack);
                    if (Double.isFinite(offset)) {
                        return offset;
                    }
                }
                case COMMAND -> {
                    return yOffset;
                }
            }
        }
        return Double.NaN;
    }

    public static Component getEnumsName(Iterable<? extends Enum<?>> enums) {
        MutableComponent result = Component.empty();
        boolean first = true;
        for (Enum<?> e : enums) {
            if (!first) {
                result.append(Component.translatable("precise_cushion_placement.enum.separator")
                    .withStyle(ChatFormatting.GRAY));
            }
            String name = e.name();
            String nameKey = "precise_cushion_placement.enum." + name;
            String descKey = nameKey + ".description";
            result.append(Component.translatable(nameKey).setStyle(ENUM_VALUE
                .withClickEvent(new ClickEvent.CopyToClipboard(name))
                .withHoverEvent(new HoverEvent.ShowText(Component.literal(name)
                    .append(Component.literal("\n"))
                    .append(Component.translatable(descKey))))));
            first = false;
        }
        return result;
    }

    public static MutableComponent getModName() {
        return Component.translatable("modmenu.nameTranslation.precise_cushion_placement");
    }

    public static MutableComponent getModPrefix() {
        return Component.literal("[")
            .append(getModName().withStyle(ChatFormatting.GREEN))
            .append(Component.literal("] "));
    }

    public static MutableComponent getConfigName(String configName) {
        String nameKey = "precise_cushion_placement.config.entry." + configName;
        String descKey = nameKey + ".description";
        Style style = Style.EMPTY
            .withClickEvent(new ClickEvent.CopyToClipboard(configName))
            .withHoverEvent(new HoverEvent.ShowText(Component.literal(configName)
                .append(Component.literal("\n"))
                .append(Component.translatable(descKey))));
        return Component.translatableWithFallback(nameKey, configName).setStyle(style);
    }

    private static double getDoubleFromCustomName(ItemStack cushionStack) {
        if (cushionStack.getCustomName() instanceof Component itemName) {
            try {
                double offset = Double.parseDouble(itemName.getString());
                if (Double.isFinite(offset)) {
                    return Math.clamp(offset, 0, 1);
                }
            } catch (NumberFormatException _) {
            }
        }
        return Double.NaN;
    }
}