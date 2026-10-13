package net.myitian.precise_cushion_placement.mixin;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CushionItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.myitian.precise_cushion_placement.PreciseCushionPlacement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MultiPlayerGameMode.class)
abstract class MultiPlayerGameModeMixin {
    @SuppressWarnings("ModifyVariableMayUseName")
    @ModifyVariable(at = @At("HEAD"), method = "useItemOn", argsOnly = true)
    private BlockHitResult useItemOn$ModifyVariable(BlockHitResult blockHit,
                                                    LocalPlayer player, InteractionHand hand) {
        ItemStack cushionStack;
        ItemStack offhandStack;
        if (hand == InteractionHand.MAIN_HAND) {
            cushionStack = player.getItemBySlot(EquipmentSlot.MAINHAND);
            offhandStack = player.getItemBySlot(EquipmentSlot.OFFHAND);
        } else {
            cushionStack = player.getItemBySlot(EquipmentSlot.OFFHAND);
            offhandStack = player.getItemBySlot(EquipmentSlot.MAINHAND);
        }
        if (cushionStack.getItem() instanceof CushionItem) {
            double offset = PreciseCushionPlacement.getYOffset(cushionStack, offhandStack);
            if (Double.isFinite(offset)) {
                Vec3 pos = blockHit.getLocation();
                double newY = Math.floor(pos.y) + offset;
                Vec3 newPos = new Vec3(pos.x, newY, pos.z);
                return new BlockHitResult(newPos,
                    Direction.UP,
                    blockHit.getBlockPos(),
                    blockHit.isInside(),
                    blockHit.isWorldBorderHit());
            }
        }
        return blockHit;
    }
}