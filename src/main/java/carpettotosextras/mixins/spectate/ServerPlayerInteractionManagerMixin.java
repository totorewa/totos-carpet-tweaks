package carpettotosextras.mixins.spectate;

import carpettotosextras.TotoCarpetSettings;
import carpettotosextras.fakes.ServerPlayerEntityInterface;
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
        if (TotoCarpetSettings.tpSpectatorsBackOnSurvivalChange && previousGameMode != gameMode) {
            System.out.println(String.format("Changing from %s to %s", previousGameMode.getName(), gameMode.getName()));
            // If changing from survival mode, remember position
            if (previousGameMode == GameMode.SURVIVAL) {
                getPlayer().carpettotosextras_rememberSurvivalPosition();
            // If changing to survival mode, teleport to previous survival position
            } else if (gameMode == GameMode.SURVIVAL) {
                getPlayer().carpettotosextras_teleportToSurvivalPosition();
            }
        }
    }

    private ServerPlayerEntityInterface getPlayer() {
        return (ServerPlayerEntityInterface)player;
    }
}
