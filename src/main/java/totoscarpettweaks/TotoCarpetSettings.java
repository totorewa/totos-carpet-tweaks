package totoscarpettweaks;

import carpet.settings.ParsedRule;
import carpet.settings.Rule;
import carpet.settings.Validator;
import net.minecraft.server.command.ServerCommandSource;

import static carpet.settings.RuleCategory.COMMAND;
import static carpet.settings.RuleCategory.SURVIVAL;

public class TotoCarpetSettings {
    public static final String TOTO = "totos-tweaks";

    @Rule(desc = "Piglins anger when a player interacts with a chest", category = {SURVIVAL, TOTO})
    public static boolean piglinsAngerOnChestUse = true;

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

    @Rule(desc = "Prevent players from teleporting when using spectator mode by returning them to their previous survival position", category = {SURVIVAL, TOTO})
    public static boolean returnSpectators = false;

    @Rule(desc = "Enables /ts command to toggle spectator mode", category = {COMMAND, TOTO})
    public static String commandToggleSpectator = "true";
}
