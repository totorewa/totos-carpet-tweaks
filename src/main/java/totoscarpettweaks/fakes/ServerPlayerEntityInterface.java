package totoscarpettweaks.fakes;

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Optional;

public interface ServerPlayerEntityInterface {
    public boolean toto$hasReturnPosition();
    public Vec3d getSurvivalPosition();
    public float getSurvivalYaw();
    public float getSurvivalPitch();
    public RegistryKey<World> getSurvivalWorldKey();
    public String getSurvivalDimensionName();
    public void rememberSurvivalPosition();
    public boolean tryReturnToSurvivalPosition();
}
