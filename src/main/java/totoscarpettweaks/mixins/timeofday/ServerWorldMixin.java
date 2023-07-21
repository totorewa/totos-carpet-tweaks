package totoscarpettweaks.mixins.timeofday;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
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
    protected ServerWorldMixin(
            MutableWorldProperties properties, RegistryKey<World> registryRef,
            DynamicRegistryManager drm,
            RegistryEntry<DimensionType> dimension, Supplier<Profiler> profiler,
            boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, drm, dimension, profiler, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    @Inject(method = "tickTime", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/world/ServerWorld;setTimeOfDay(J)V",
            ordinal = 0,
            shift = At.Shift.AFTER
    ))
    protected void onTimeTick(CallbackInfo ci) {
        if (getRegistryKey() == World.OVERWORLD) {
            // The villager schedule logger could be invoked directly here, and normally would be,
            // however this time of day advancement could be a useful hook to have for more than this
            // feature and so instead I've created a "controller" which will handle passing out this value
            // to any feature, albeit hard-coded, which may have use of it.
            TimeOfDayAdvanced.handle((int) (getTimeOfDay() % 24000L));
        }
    }
}
