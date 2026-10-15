package net.myitian.precise_cushion_placement.platform.fabric;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandBuildContext;
import net.myitian.precise_cushion_placement.CommandBuilder;
import net.myitian.precise_cushion_placement.PreciseCushionPlacement;

public final class ModEntrypoint implements ClientModInitializer {
    private static void onClientCommandRegister(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext registryAccess) {
        dispatcher.register(
            new CommandBuilder<>(FabricClientCommandSource::sendFeedback)
                .build(ClientCommands::literal, ClientCommands::argument));
    }

    @Override
    public void onInitializeClient() {
        PreciseCushionPlacement.init(FabricLoader.getInstance()::getConfigDir);
        ClientCommandRegistrationCallback.EVENT.register(ModEntrypoint::onClientCommandRegister);
    }
}