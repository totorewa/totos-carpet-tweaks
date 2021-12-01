package totoscarpettweaks;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import net.minecraft.server.network.ServerPlayerEntity;
import totoscarpettweaks.commands.ToggleSpectatorCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import totoscarpettweaks.fakes.ServerPlayerEntityInterface;
import totoscarpettweaks.listeners.PlayerLoggedInListener;

public class TotoCarpetServer implements CarpetExtension {
	public static void noop() { }

	static {
		CarpetServer.manageExtension(new TotoCarpetServer());
	}

	private PlayerLoggedInListener playerLoggedInListener;

	@Override
	public void onGameStarted() {
		CarpetServer.settingsManager.parseSettingsClass(TotoCarpetSettings.class);
		playerLoggedInListener = new PlayerLoggedInListener();
	}

	@Override
	public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
		ToggleSpectatorCommand.register(dispatcher);
	}

	@Override
	public void registerLoggers() {
		try {
			LoggerRegistry.registerLogger("villagerSchedule", new Logger(TotoCarpetServer.class.getField("__villagerSchedule"), "villagerSchedule", null, null, false));
		} catch (NoSuchFieldException e) {
			throw new RuntimeException("Could not create logger: villagerSchedule");
		}
	}

	@Override
	public void onPlayerLoggedIn(ServerPlayerEntity player) {
		playerLoggedInListener.handle(player);
	}

	@Override
	public String version() {
		return "totos-extras";
	}

	public static boolean __villagerSchedule;
}
