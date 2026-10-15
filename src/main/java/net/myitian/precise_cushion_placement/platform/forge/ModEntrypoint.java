package net.myitian.precise_cushion_placement.platform.forge;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import net.myitian.precise_cushion_placement.CommandBuilder;
import net.myitian.precise_cushion_placement.PreciseCushionPlacement;

@Mod(PreciseCushionPlacement.MOD_ID)
@Mod.EventBusSubscriber(modid = PreciseCushionPlacement.MOD_ID, value = Dist.CLIENT)
public final class ModEntrypoint {
    public ModEntrypoint() {
        PreciseCushionPlacement.init(FMLPaths.CONFIGDIR::get);
    }

    @SubscribeEvent
    public static void onClientCommandRegister(RegisterClientCommandsEvent event) {
        event.getDispatcher().register(
            new CommandBuilder<>(CommandSourceStack::sendSystemMessage)
                .build(Commands::literal, Commands::argument));
    }
}