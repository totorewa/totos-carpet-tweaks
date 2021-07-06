package totoscarpettweaks.mixins.returnspectators;

import carpet.patches.EntityPlayerMPFake;
import net.minecraft.server.command.GameModeCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import totoscarpettweaks.TotoCarpetSettings;
import totoscarpettweaks.fakes.ServerPlayerEntityInterface;

import static net.minecraft.world.GameMode.SURVIVAL;


@Mixin(GameModeCommand.class)
public class GameModeCommandMixin {
    @Inject(method = "sendFeedback", at = @At("RETURN"))
    private static void beforeExecute(ServerCommandSource source, ServerPlayerEntity player, GameMode gameMode, CallbackInfo ci) {
        if (TotoCarpetSettings.returnSpectators && !(player instanceof EntityPlayerMPFake)) {
            // If changing to survival mode, teleport to previous survival position
            if (gameMode == SURVIVAL) {
                ((ServerPlayerEntityInterface) player).tryTeleportToSurvivalPosition();
            // If changing from survival mode, remember position
            } else if (player.interactionManager.getPreviousGameMode() == SURVIVAL) {
                ((ServerPlayerEntityInterface) player).rememberSurvivalPosition();
            }
        }
    }
}
