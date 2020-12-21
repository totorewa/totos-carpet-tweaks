package carpettotosextras;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpettotosextras.commands.ToggleSpectatorCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;

public class TotoCarpetServer implements CarpetExtension {
	public static void noop() { }

	static {
		CarpetServer.manageExtension(new TotoCarpetServer());
	}

	@Override
	public void onGameStarted() {
		CarpetServer.settingsManager.parseSettingsClass(TotoCarpetSettings.class);
	}

	@Override
	public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
		ToggleSpectatorCommand.register(dispatcher);
	}

	@Override
	public String version() {
		return "totos-extras";
	}
}
