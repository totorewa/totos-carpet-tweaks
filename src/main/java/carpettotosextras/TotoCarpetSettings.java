package carpettotosextras;

import carpet.settings.ParsedRule;
import carpet.settings.Rule;
import carpet.settings.Validator;
import net.minecraft.server.command.ServerCommandSource;

import static carpet.settings.RuleCategory.SURVIVAL;
import static carpet.settings.RuleCategory.FEATURE;

public class TotoCarpetSettings {
    public static final String TOTO = "totos-extras";

    @Rule(desc = "Remembers players location when changing to spectator. Teleports them back to that location when they change back to survival.", category = {SURVIVAL, TOTO})
    public static boolean tpSpectatorsBackOnSurvivalChange = false;

    @Rule(desc = "Prevents cats spawning in villages", category = {SURVIVAL, TOTO})
    public static boolean catsNoSpawnInVillage = false;
}
