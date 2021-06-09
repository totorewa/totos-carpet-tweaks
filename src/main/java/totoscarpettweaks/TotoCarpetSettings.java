package totoscarpettweaks;

import carpet.settings.ParsedRule;
import carpet.settings.Rule;
import carpet.settings.RuleCategory;
import carpet.settings.Validator;
import net.minecraft.server.command.ServerCommandSource;

import static carpet.settings.RuleCategory.*;

public class TotoCarpetSettings {
    public static final String TOTO = "totos-tweaks";

    private static class validateCatSpawnRate extends Validator<Integer> {
        @Override
        public Integer validate(ServerCommandSource source, ParsedRule<Integer> currentRule, Integer newValue, String string) {
            return (newValue >= 0 && newValue <= 100) ? newValue : null;
        }

        @Override
        public String description() {
            return "You must choose a value from 0 to 100";
        }
    }

    @Rule(desc = "Sets chance cat will spawn in a village", category = {SURVIVAL, TOTO}, validate = validateCatSpawnRate.class, options = {"0", "25", "50", "75", "100"}, strict = false)
    public static int catSpawnChance = 100;

    @Rule(desc = "Stops Piglins angering when a block they guard is interacted with or broken", category = {SURVIVAL, TOTO})
    public static boolean noPiglinGuarding = false;

    @Rule(desc = "Prevent players from teleporting when using spectator mode by returning them to their previous survival position", category = {SURVIVAL, TOTO})
    public static boolean returnSpectators = false;

    @Rule(desc = "Major positive reputation is shared amongst all players", category = {SURVIVAL, TOTO})
    public static boolean sharedVillagerDiscounts = false;

    @Rule(desc = "Makes spectators visible to non-spectator players", category = {TOTO})
    public static boolean visibleSpectators = false;

    @Rule(desc = "Enables /ts command to toggle spectator mode", category = {COMMAND, TOTO})
    public static String commandToggleSpectator = "true";
}
