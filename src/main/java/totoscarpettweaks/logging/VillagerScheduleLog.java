package totoscarpettweaks.logging;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import net.minecraft.text.BaseText;

public class VillagerScheduleLog {
    public static void announceTask(String task) {
        LoggerRegistry.getLogger("villagerSchedule").log(o ->
                new BaseText[]{
                        Messenger.c("g Villagers are now ", "d " + task)
                }
        );
    }
}
