package totoscarpettweaks.commands;

import carpet.patches.EntityPlayerMPFake;
import carpet.settings.SettingsManager;
import carpet.utils.Messenger;
import net.minecraft.util.math.Vec3d;
import totoscarpettweaks.TotoCarpetSettings;
import totoscarpettweaks.fakes.ServerPlayerEntityInterface;
import com.mojang.brigadier.CommandDispatcher;
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
        dispatcher.register(
                literal("ts")
                        .requires(c -> TotoCarpetSettings.returnSpectators &&
                                SettingsManager.canUseCommand(c, (Object) TotoCarpetSettings.commandToggleSpectator))
                        .executes(c -> toggleSpectator(c.getSource()))
                        .then(
                                literal("info")
                                        .executes(c -> spectatorInfo(c.getSource()))
                        ));
    }

    private static int toggleSpectator(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();
        if (player instanceof EntityPlayerMPFake) {
            Messenger.m(player, "r Command cannot be used on a fake player.");
            return 0;
        }

        if (player.interactionManager.getGameMode() == SURVIVAL) {
            player.setGameMode(SPECTATOR);
        } else {
            player.setGameMode(SURVIVAL);
        }
        return 1;
    }

    private static int spectatorInfo(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();
        if (!player.isSpectator()) {
            Messenger.m(player, "r You are not in spectator mode.");
            return 0;
        }

        if (player instanceof EntityPlayerMPFake) {
            Messenger.m(player, "r Command cannot be used on a fake player.");
            return 0;
        }

        ServerPlayerEntityInterface totoPlayer = (ServerPlayerEntityInterface) player;

        if (!totoPlayer.toto$hasReturnPosition()) {
            Messenger.m(player, "r Sorry, I can't remember your last position.");
            return 0;
        }

        Vec3d pos = totoPlayer.getSurvivalPosition();
        Messenger.m(player,
                "y X ",
                String.format(POSITION_FORMAT_TEMPLATE, pos.x),
                "y Y ",
                String.format(POSITION_FORMAT_TEMPLATE, pos.y),
                "y Z ",
                String.format(POSITION_FORMAT_TEMPLATE, pos.z));
        Messenger.m(player, "y Dimension ", String.format(DIMENSION_FORMAT_TEMPLATE, totoPlayer.getSurvivalDimensionName()));

        return 1;
    }
}
