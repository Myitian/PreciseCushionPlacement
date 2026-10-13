package net.myitian.precise_cushion_placement.platform.neoforge;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.myitian.precise_cushion_placement.CommandBuilder;
import net.myitian.precise_cushion_placement.PreciseCushionPlacement;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;

@Mod(value = PreciseCushionPlacement.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = PreciseCushionPlacement.MOD_ID, value = Dist.CLIENT)
public final class PreciseCushionPlacementNeoForge {
    public PreciseCushionPlacementNeoForge() {
        PreciseCushionPlacement.init(FMLPaths.CONFIGDIR::get);
    }

    @SubscribeEvent
    public static void onClientCommandRegister(RegisterClientCommandsEvent event) {
        event.getDispatcher().register(
            new CommandBuilder<>(CommandSourceStack::sendSystemMessage)
                .build(Commands::literal, Commands::argument));
    }
}