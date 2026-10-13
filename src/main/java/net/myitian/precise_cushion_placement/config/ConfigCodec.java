package net.myitian.precise_cushion_placement.config;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.*;

public final class ConfigCodec {
    private final LinkedHashMap<String, Pair<ConsumerWithIOException<JsonReader>, ConsumerWithIOException<JsonWriter>>> fieldMap = new LinkedHashMap<>();

    public static <E extends Enum<E>> void deserializeEnumCollection(JsonReader reader, Collection<E> collection, Class<E> enumClass) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return;
        }
        reader.beginArray();
        while (reader.peek() == JsonToken.STRING) {
            String name = reader.nextString();
            E enumValue = EnumUtils.getEnumIgnoreCase(enumClass, name);
            collection.add(enumValue);
        }
        reader.endArray();
    }

    public static void serializeEnumIterable(JsonWriter writer, Iterable<? extends Enum<?>> iterable) throws IOException {
        if (iterable == null) {
            writer.nullValue();
            return;
        }
        writer.beginArray();
        for (Enum<?> enumValue : iterable) {
            writer.value(enumValue.name());
        }
        writer.endArray();
    }

    public SequencedMap<String, Pair<ConsumerWithIOException<JsonReader>, ConsumerWithIOException<JsonWriter>>> getFieldMap() {
        return fieldMap;
    }

    public boolean deserialize(JsonReader reader) throws IOException {
        if (reader.peek() != JsonToken.BEGIN_OBJECT) {
            return false;
        }
        reader.beginObject();
        Set<String> nameSet = new HashSet<>(fieldMap.size());
        while (reader.peek() == JsonToken.NAME) {
            String name = reader.nextName();
            var pair = fieldMap.get(name);
            if (pair != null) {
                nameSet.add(name);
                pair.getLeft().accept(reader);
            } else {
                reader.skipValue();
            }
        }
        return nameSet.size() == fieldMap.size();
    }

    public void serialize(JsonWriter writer) throws IOException {
        writer.beginObject();
        for (var fieldInfo : fieldMap.entrySet()) {
            writer.name(fieldInfo.getKey());
            fieldInfo.getValue().getRight().accept(writer);
        }
        writer.endObject();
    }

    @FunctionalInterface
    public interface ConsumerWithIOException<T> {
        void accept(T t) throws IOException;
    }
}