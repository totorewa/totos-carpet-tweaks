package totoscarpettweaks.mixins.spectate;

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

    @Inject(method="setGameMode(Lnet/minecraft/world/GameMode;Lnet/minecraft/world/GameMode;)V", at = @At(value = "HEAD"))
    private void onGameModeChange(GameMode gameMode, GameMode previousGameMode, CallbackInfo ci) {
        if (TotoCarpetSettings.returnSpectators && previousGameMode != gameMode && !(player instanceof EntityPlayerMPFake)) {
            // If changing from survival mode, remember position
            if (previousGameMode == GameMode.SURVIVAL) {
                getPlayer().rememberSurvivalPosition();
            // If changing to survival mode, teleport to previous survival position
            } else if (gameMode == GameMode.SURVIVAL) {
                getPlayer().tryTeleportToSurvivalPosition();
            }
        }
    }

    private ServerPlayerEntityInterface getPlayer() {
        return (ServerPlayerEntityInterface)player;
    }
}
