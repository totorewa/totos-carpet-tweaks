package carpettotosextras.commands;

import carpet.settings.SettingsManager;
import carpet.utils.Messenger;
import carpettotosextras.TotoCarpetSettings;
import carpettotosextras.fakes.ServerPlayerEntityInterface;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.world.GameMode.SURVIVAL;
import static net.minecraft.world.GameMode.SPECTATOR;

/***
 * While the mod will remember survival positions when using the standard /gamemode command or F3+N,
 * that command is only available to operators. To open up spectator mode to all players (without
 * access to creative mode), a new command is registered.
 */
public class ToggleSpectatorCommand {
    private static final String POSITION_FORMAT_TEMPLATE = "w %.0f ";
    private static final String DIMENSION_FORMAT_TEMPLATE = "w %s";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        // Base command
        LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder =
                literal("ts")
                        .requires(player -> TotoCarpetSettings.returnSpectators &&
                                SettingsManager.canUseCommand(player, TotoCarpetSettings.commandToggleSpectator))
                        .executes(c -> toggleSpectator(c.getSource()));

        // Get last position
        literalArgumentBuilder.then(
                literal("info")
                        .executes(c -> spectatorInfo(c.getSource()))
        );

        dispatcher.register(literalArgumentBuilder);
    }

    private static int toggleSpectator(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();
        boolean inSurvival = player.interactionManager.getGameMode() == SURVIVAL;
        player.setGameMode(inSurvival ? SPECTATOR : SURVIVAL);
        return 1;
    }

    private static int spectatorInfo(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();
        if (!player.isSpectator()) {
            Messenger.m(player, "r You are not in spectator mode.");
            return 0;
        }

        ServerPlayerEntityInterface totoPlayer = (ServerPlayerEntityInterface) player;

        if (!totoPlayer.carpettotosextras_hasLastSurvivalPosition()) {
            Messenger.m(player, "r Sorry, I can't remember your last position.");
            return 0;
        }

        Messenger.m(player,
                "y X ",
                String.format(POSITION_FORMAT_TEMPLATE, totoPlayer.carpettotosextra_getSurvivalX()),
                "y Y ",
                String.format(POSITION_FORMAT_TEMPLATE, totoPlayer.carpettotosextra_getSurvivalY()),
                "y Z ",
                String.format(POSITION_FORMAT_TEMPLATE, totoPlayer.carpettotosextra_getSurvivalZ()));
        Messenger.m(player, "y Dimension ", String.format(DIMENSION_FORMAT_TEMPLATE, totoPlayer.carpettotosextra_getSurvivalDimensionName()));

        return 1;
    }
}
