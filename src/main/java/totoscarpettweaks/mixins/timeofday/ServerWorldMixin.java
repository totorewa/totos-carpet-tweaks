package totoscarpettweaks.mixins.timeofday;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.spawner.Spawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import totoscarpettweaks.controllers.TimeOfDayAdvanced;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {
    protected ServerWorldMixin(MinecraftServer server, Executor workerExecutor, LevelStorage.Session session,
                          ServerWorldProperties properties, RegistryKey<World> worldKey, DimensionOptions dimensionOptions,
                          WorldGenerationProgressListener worldGenerationProgressListener, boolean debugWorld, long seed,
                          List<Spawner> spawners, boolean shouldTickTime) {
        super(properties, worldKey, dimensionOptions.getDimensionTypeEntry(), server::getProfiler, false,
                debugWorld, seed, server.getMaxChainedNeighborUpdates());
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
