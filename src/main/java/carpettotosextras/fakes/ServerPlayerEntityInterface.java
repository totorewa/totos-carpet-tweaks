package carpettotosextras.fakes;

import net.minecraft.util.Identifier;

public interface ServerPlayerEntityInterface {
    // Prefixing with modid as per suggestion in https://fabricmc.net/wiki/tutorial:modding_tips
    public boolean carpettotosextras_hasLastSurvivalPosition();
    public double carpettotosextra_getSurvivalX();
    public double carpettotosextra_getSurvivalY();
    public double carpettotosextra_getSurvivalZ();
    public String carpettotosextra_getSurvivalDimensionName();
    public void carpettotosextras_rememberSurvivalPosition();
    public void carpettotosextras_teleportToSurvivalPosition();
}
