package totoscarpettweaks.mixins.piglinguarding;

import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import totoscarpettweaks.TotoCarpetSettings;

@Mixin(PiglinBrain.class)
public class PiglinBrainMixin {
    @Inject(method = "onGuardedBlockInteracted", at = @At("HEAD"), cancellable = true)
    private static void cancelOnGuardedBlockInteracted(PlayerEntity player, boolean blockOpen, CallbackInfo ci) {
        if (TotoCarpetSettings.noPiglinGuarding)
            ci.cancel();
    }
}
