package totoscarpettweaks.mixins.returnspectators;

import carpet.patches.EntityPlayerMPFake;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import totoscarpettweaks.TotoCarpetSettings;
import totoscarpettweaks.fakes.ServerPlayerEntityInterface;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @Shadow
    private GameMode gameMode;

    @Inject(
            method = "changeGameMode(Lnet/minecraft/world/GameMode;)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerPlayerInteractionManager;setGameMode(Lnet/minecraft/world/GameMode;Lnet/minecraft/world/GameMode;)V",
                    shift = At.Shift.BEFORE))
    private void onGameModeChange(GameMode gameMode, CallbackInfoReturnable<Boolean> cir) {
        if (TotoCarpetSettings.returnSpectators && !(player instanceof EntityPlayerMPFake)) {
            // If changing from survival mode, remember position
            if (gameMode == GameMode.SURVIVAL) {
                ((ServerPlayerEntityInterface) player).tryReturnToSurvivalPosition();
                // If changing to survival mode, teleport to previous survival position
            } else if (this.gameMode == GameMode.SURVIVAL) {
                ((ServerPlayerEntityInterface) player).rememberSurvivalPosition();
            }
        }
    }
}
