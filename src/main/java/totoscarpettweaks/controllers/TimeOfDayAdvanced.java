package totoscarpettweaks.controllers;

import totoscarpettweaks.controllers.villagerschedule.VillagerScheduleHandler;

public class TimeOfDayAdvanced {
    private final static VillagerScheduleHandler villagerSchedule;
    static {
        villagerSchedule = new VillagerScheduleHandler();
    }

    public static void handle(int timeOfDay) {
        villagerSchedule.handle(timeOfDay);
    }
}
