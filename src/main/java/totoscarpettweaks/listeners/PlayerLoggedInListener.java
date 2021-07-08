package totoscarpettweaks.listeners;

import net.minecraft.server.network.ServerPlayerEntity;
import totoscarpettweaks.TotoCarpetSettings;
import totoscarpettweaks.fakes.ServerPlayerEntityInterface;

import static net.minecraft.world.GameMode.SURVIVAL;

public class PlayerLoggedInListener {
    public void handle(ServerPlayerEntity player) {
        if (TotoCarpetSettings.returnSpectators && player.interactionManager.getGameMode() == SURVIVAL) {
            ((ServerPlayerEntityInterface) player).tryReturnToSurvivalPosition();
        }
    }
}
