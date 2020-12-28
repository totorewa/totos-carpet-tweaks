package totoscarpettweaks.mixins.villagerschedule;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import totoscarpettweaks.controllers.TimeOfDayAdvanced;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {
    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DimensionType dimensionType, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed) {
        super(properties, registryRef, dimensionType, profiler, isClient, debugWorld, seed);
    }

    @Inject(method = "tickTime", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/world/ServerWorld;setTimeOfDay(J)V",
            ordinal = 0,
            shift = At.Shift.AFTER
    ))
    protected void onTimeTick(CallbackInfo ci) {
        if (getRegistryKey() == World.OVERWORLD) {
            TimeOfDayAdvanced.handle((int) (getTimeOfDay() % 24000L));
        }
    }
}
