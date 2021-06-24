package scarpet_canvas.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FilledMapItem.class)
public class FilledMap_Mixin {
    @Inject(at= @At("HEAD"), method = "createSyncPacket", cancellable = true)
    public void createSyncPacket(ItemStack stack, World world, PlayerEntity player, CallbackInfoReturnable<Packet<?>> cir) {
        if(FilledMapItem.getMapId(stack) > 10000) {
            cir.setReturnValue(null);
            cir.cancel();
        }
    }


    @Inject(at= @At("HEAD"), method = "inventoryTick", cancellable = true)
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        if(FilledMapItem.getMapId(stack) > 10000) {
            ci.cancel();
        }
    }
}
