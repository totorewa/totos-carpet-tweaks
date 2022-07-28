package totoscarpettweaks;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Rule;
import carpet.api.settings.Validator;
import net.minecraft.server.command.ServerCommandSource;

import static carpet.api.settings.RuleCategory.*;

public class TotoCarpetSettings {
    public static final String TOTO = "totos-tweaks";

    private static class validateCatSpawnRate extends Validator<Integer> {
        @Override
        public Integer validate(ServerCommandSource source, CarpetRule<Integer> changingRule, Integer newValue, String userInput) {
            return (newValue >= 0 && newValue <= 100) ? newValue : null;
        }

        @Override
        public String description() {
            return "You must choose a value from 0 to 100";
        }
    }

    @Rule(categories = {SURVIVAL, TOTO}, validators = validateCatSpawnRate.class, options = {"0", "25", "50", "75", "100"}, strict = false)
    public static int catSpawnChance = 100;

    @Rule(categories = {SURVIVAL, TOTO})
    public static boolean noPiglinGuarding = false;

    @Rule(categories = {SURVIVAL, TOTO})
    public static boolean returnSpectators = false;

    @Rule(categories = {SURVIVAL, TOTO})
    public static boolean sharedVillagerDiscounts = false;

    @Rule(categories = {TOTO, EXPERIMENTAL})
    public static boolean visibleSpectators = false;

    @Rule(categories = {COMMAND, TOTO}, options = {"true", "ops", "false", "0", "1", "2", "3", "4"})
    public static String commandToggleSpectator = "true";
}
