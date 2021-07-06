package totoscarpettweaks.controllers.villagerschedule;

import totoscarpettweaks.logging.VillagerScheduleLog;

public class VillagerScheduleHandler {
    public void handle(int timeOfDay) {
        switch (timeOfDay) {
            case 10:
            case 11000:
                announce("idle");
                break;
            case 2000:
                announce("working");
                break;
            case 9000:
                announce("meeting");
                break;
            case 12000:
                announce("sleeping");
                break;
            default:
                break;
        }
    }

    private void announce(String taskName) {
        VillagerScheduleLog.announceTask(taskName);
    }
}
