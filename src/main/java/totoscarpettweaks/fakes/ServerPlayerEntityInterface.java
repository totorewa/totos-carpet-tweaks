package totoscarpettweaks.fakes;

import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public interface ServerPlayerEntityInterface {
    public boolean canReturnSpectator();
    public Optional<Vec3d> getSurvivalPosition();
    public String getSurvivalDimensionName();
    public void rememberSurvivalPosition();
    public boolean tryTeleportToSurvivalPosition();
}
