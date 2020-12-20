package carpettotosextras.commands;

import carpet.utils.Messenger;
import carpettotosextras.TotoCarpetSettings;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.world.GameMode.SURVIVAL;
import static net.minecraft.world.GameMode.SPECTATOR;

/***
 * While the mod will remember survival positions when using the standard /gamemode command or F3+N,
 * that command is only available to operators. To open up the spectator option to all players (without
 * access to creative mode), a new command will be registered.
 */
public class SpectatorCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("spec")
                .executes((c -> toggleSpectator(c.getSource()))));
    }

    private static int toggleSpectator(ServerCommandSource source) throws CommandSyntaxException {
        if (!TotoCarpetSettings.tpSpectatorsBackOnSurvivalChange) {
            Messenger.m(source, "w This feature has not been enabled.");
            return 0;
        }

        ServerPlayerEntity player = source.getPlayer();
        boolean inSurvival = player.interactionManager.getGameMode() == SURVIVAL;
        player.setGameMode(inSurvival ? SPECTATOR : SURVIVAL);
        return 1;
    }
}
