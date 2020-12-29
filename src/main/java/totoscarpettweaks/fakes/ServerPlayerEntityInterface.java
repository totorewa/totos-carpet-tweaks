package totoscarpettweaks.fakes;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.Optional;

public interface ServerPlayerEntityInterface {
    public boolean canReturnSpectator();
    public Optional<Vec3d> getSurvivalPosition();
    public float getSurvivalYaw();
    public float getSurvivalPitch();
    public Optional<RegistryKey<World>> getSurvivalWorldKey();
    public String getSurvivalDimensionName();
    public void rememberSurvivalPosition();
    public boolean tryTeleportToSurvivalPosition();
}
