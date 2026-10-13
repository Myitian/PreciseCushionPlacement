package net.myitian.precise_cushion_placement;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.EnumUtils;

import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.concurrent.CompletableFuture;

public record EnumSetArgumentType<E extends Enum<E>>(Class<E> enumClass) implements ArgumentType<LinkedHashSet<E>> {
    private static final DynamicCommandExceptionType INVALID_ENUM_VALUE
        = new DynamicCommandExceptionType(value -> Component.translatable("precise_cushion_placement.command.invalid_enum_value", value));

    private static String readRemaining(StringReader reader) {
        String text = reader.getRemaining();
        reader.setCursor(reader.getTotalLength());
        return text;
    }

    public static <E extends Enum<E>> EnumSetArgumentType<E> enumSet(Class<E> enumClass) {
        return new EnumSetArgumentType<>(enumClass);
    }

    public static LinkedHashSet<?> getSet(final CommandContext<?> context, final String name) {
        return context.getArgument(name, LinkedHashSet.class);
    }

    @Override
    public LinkedHashSet<E> parse(StringReader reader) throws CommandSyntaxException {
        String s = readRemaining(reader);
        LinkedHashSet<E> set = new LinkedHashSet<>();
        while (true) {
            int comma = s.indexOf(',');
            String name = comma == -1 ? s : s.substring(0, comma);
            E value = EnumUtils.getEnumIgnoreCase(enumClass, name.strip());
            if (value == null) {
                throw INVALID_ENUM_VALUE.createWithContext(reader, name);
            }
            set.add(value);
            if (comma == -1) {
                break;
            }
            s = s.substring(comma + 1);
        }
        return set;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String s = builder.getRemainingLowerCase();
        StringBuilder buffer = new StringBuilder(s.length() + 20);
        EnumSet<E> set = EnumSet.noneOf(enumClass);
        while (true) {
            int comma = s.indexOf(',');
            String name = comma == -1 ? s : s.substring(0, comma);
            E value = EnumUtils.getEnumIgnoreCase(enumClass, name.strip());
            if (value == null) {
                break;
            }
            buffer.append(value.name());
            set.add(value);
            if (comma == -1) {
                break;
            }
            buffer.append(',');
            s = s.substring(comma + 1);
        }
        s = s.strip();
        for (E value : enumClass.getEnumConstants()) {
            String name = value.name();
            if (!set.contains(value) && name.toLowerCase().startsWith(s)) {
                buffer.append(name);
                builder.suggest(buffer.toString());
                buffer.setLength(buffer.length() - name.length());
            }
        }
        return builder.buildFuture();
    }
}
