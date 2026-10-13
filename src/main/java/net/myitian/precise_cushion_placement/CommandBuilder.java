package net.myitian.precise_cushion_placement;

import com.google.common.collect.Iterables;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.myitian.precise_cushion_placement.config.Config;

import java.util.LinkedHashSet;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public record CommandBuilder<S>(BiConsumer<S, Component> sendFeedback) {
    public LiteralArgumentBuilder<S> build(
        Function<String, LiteralArgumentBuilder<S>> literal,
        BiFunction<String, ArgumentType<?>, RequiredArgumentBuilder<S, ?>> argument) {
        DoubleArgumentType offsetValueArg = DoubleArgumentType.doubleArg(0, 1);
        EnumSetArgumentType<ValueSource> valueSourceArg = EnumSetArgumentType.enumSet(ValueSource.class);
        return literal.apply("precise_cushion_placement")
            .then(literal.apply("offset")
                .then(literal.apply("clear")
                    .executes(this::clearOffset))
                .then(literal.apply("get")
                    .executes(this::getOffset))
                .then(literal.apply("set")
                    .then(argument.apply("value", offsetValueArg)
                        .executes(this::setOffset))))
            .then(literal.apply("config")
                .then(literal.apply("reload")
                    .executes(this::reloadConfig))
                .then(literal.apply("reset")
                    .executes(this::resetConfig))
                .then(literal.apply("get")
                    .executes(this::getConfig))
                .then(literal.apply("set")
                    .then(literal.apply("valueSources")
                        .then(argument.apply("value", valueSourceArg)
                            .executes(this::setConfig_valueSources)))));
    }

    private int clearOffset(CommandContext<S> ctx) {
        PreciseCushionPlacement.yOffset = Double.NaN;
        sendFeedback.accept(ctx.getSource(), PreciseCushionPlacement.getModPrefix()
            .append(Component.translatable("precise_cushion_placement.offset.clear")));
        return Command.SINGLE_SUCCESS;
    }

    private int getOffset(CommandContext<S> ctx) {
        double value = PreciseCushionPlacement.yOffset;
        sendFeedback.accept(ctx.getSource(), PreciseCushionPlacement.getModPrefix()
            .append(Component.translatable("precise_cushion_placement.offset.current",
                Double.isFinite(value) ? value :
                    Component.translatable("precise_cushion_placement.offset.unset"))));
        return Command.SINGLE_SUCCESS;
    }

    private int setOffset(CommandContext<S> ctx) {
        PreciseCushionPlacement.yOffset = DoubleArgumentType.getDouble(ctx, "value");
        sendFeedback.accept(ctx.getSource(), PreciseCushionPlacement.getModPrefix()
            .append(Component.translatable("precise_cushion_placement.offset.set",
                PreciseCushionPlacement.yOffset)));
        return Command.SINGLE_SUCCESS;
    }

    private int reloadConfig(CommandContext<S> ctx) {
        PreciseCushionPlacement.reloadConfig();
        sendFeedback.accept(ctx.getSource(), PreciseCushionPlacement.getModPrefix()
            .append(Component.translatable("precise_cushion_placement.config.reload")));
        return Command.SINGLE_SUCCESS;
    }

    private int resetConfig(CommandContext<S> ctx) {
        Config.reset();
        PreciseCushionPlacement.saveConfig();
        sendFeedback.accept(ctx.getSource(), PreciseCushionPlacement.getModPrefix()
            .append(Component.translatable("precise_cushion_placement.config.reset")));
        return Command.SINGLE_SUCCESS;
    }

    private int getConfig(CommandContext<S> ctx) {
        S source = ctx.getSource();
        sendFeedback.accept(source, Component.literal("=== [")
            .append(Component.translatable("precise_cushion_placement.config.header")
                .withStyle(ChatFormatting.GREEN))
            .append(Component.literal("] ===")));
        sendFeedback.accept(source, Component.empty()
            .append(PreciseCushionPlacement.getConfigName("valueSources"))
            .append(Component.translatable("precise_cushion_placement.config.separator"))
            .append(PreciseCushionPlacement.getEnumsName(Config.valueSources)));
        return Command.SINGLE_SUCCESS;
    }

    private int setConfig_valueSources(CommandContext<S> ctx) {
        @SuppressWarnings("unchecked")
        LinkedHashSet<ValueSource> value = (LinkedHashSet<ValueSource>) EnumSetArgumentType.getSet(ctx, "value");
        PreciseCushionPlacement.loadConfig();
        if (!Iterables.elementsEqual(Config.valueSources, value)) {
            Config.valueSources.clear();
            Config.valueSources.addAll(value);
            PreciseCushionPlacement.saveConfig();
        }
        sendFeedback.accept(ctx.getSource(), PreciseCushionPlacement.getModPrefix()
            .append(Component.translatable("precise_cushion_placement.config.set",
                PreciseCushionPlacement.getConfigName("valueSources")))
            .append(Component.translatable("precise_cushion_placement.config.separator"))
            .append(PreciseCushionPlacement.getEnumsName(Config.valueSources)));
        return Command.SINGLE_SUCCESS;
    }
}