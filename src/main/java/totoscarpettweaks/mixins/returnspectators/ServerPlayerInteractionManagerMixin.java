package totoscarpettweaks.mixins.returnspectators;

import carpet.patches.EntityPlayerMPFake;
import totoscarpettweaks.TotoCarpetSettings;
import totoscarpettweaks.fakes.ServerPlayerEntityInterface;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "setGameMode(Lnet/minecraft/world/GameMode;Lnet/minecraft/world/GameMode;)V", at = @At("HEAD"))
    private void onGameModeChange(GameMode gameMode, GameMode previousGameMode, CallbackInfo ci) {
        if (TotoCarpetSettings.returnSpectators && gameMode != previousGameMode && !(player instanceof EntityPlayerMPFake)) {
            // If changing from survival mode, remember position
            if (gameMode == GameMode.SURVIVAL) {
                ((ServerPlayerEntityInterface) player).tryReturnToSurvivalPosition();
            // If changing to survival mode, teleport to previous survival position
            } else if (previousGameMode == GameMode.SURVIVAL) {
                ((ServerPlayerEntityInterface) player).rememberSurvivalPosition();
            }
        }
    }
}
