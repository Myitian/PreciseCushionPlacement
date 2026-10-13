package net.myitian.precise_cushion_placement.config;

import com.google.gson.Strictness;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.myitian.precise_cushion_placement.PreciseCushionPlacement;
import net.myitian.precise_cushion_placement.ValueSource;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedHashSet;
import java.util.Map;

public final class Config {
    public static final LinkedHashSet<ValueSource> valueSources = new LinkedHashSet<>(3);
    private static final ConfigCodec CODEC = new ConfigCodec();

    static {
        reset();
        registerCodec(CODEC.getFieldMap());
    }

    public static void reset() {
        valueSources.clear();
        valueSources.add(ValueSource.CUSHION_ITEM_NAME);
        valueSources.add(ValueSource.OFFHAND_ITEM_NAME);
        valueSources.add(ValueSource.COMMAND);
    }

    private static void registerCodec(Map<String, Pair<ConfigCodec.ConsumerWithIOException<JsonReader>, ConfigCodec.ConsumerWithIOException<JsonWriter>>> map) {
        map.put("valueSources", Pair.of(
            reader -> ConfigCodec.deserializeEnumCollection(reader, valueSources, ValueSource.class),
            writer -> ConfigCodec.serializeEnumIterable(writer, valueSources)
        ));
    }

    public static boolean load(File configFile) {
        try (var reader = new JsonReader(new FileReader(configFile))) {
            reader.setStrictness(Strictness.LENIENT);
            return CODEC.deserialize(reader);
        } catch (Exception e) {
            PreciseCushionPlacement.LOGGER.info("Failed to read config: {}", e.getLocalizedMessage());
        }
        return false;
    }

    public static void save(File configFile) {
        try (var writer = new JsonWriter(new FileWriter(configFile))) {
            writer.setStrictness(Strictness.LENIENT);
            writer.setHtmlSafe(false);
            writer.setIndent("  ");
            CODEC.serialize(writer);
        } catch (Exception e) {
            PreciseCushionPlacement.LOGGER.warn("Failed to write config: {}", e.getLocalizedMessage());
        }
    }
}