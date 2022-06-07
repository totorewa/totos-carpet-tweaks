package totoscarpettweaks.mixins.villagecats;

import net.minecraft.world.spawner.CatSpawner;
import totoscarpettweaks.TotoCarpetSettings;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CatSpawner.class)
public class CatSpawnerMixin {
    private static int lastSpawnChance;
    private static float spawnChance;

    @Inject(method = "spawnInHouse", at = @At(value = "HEAD"), cancellable = true)
    private void onSpawnInHouse(ServerWorld world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        // Fallback to NMS
        if (TotoCarpetSettings.catSpawnChance == 100) return;
        if (TotoCarpetSettings.catSpawnChance == 0 || world.random.nextFloat() >= getSpawnChance())
            cir.setReturnValue(0);
    }

    // Calculates and stores the spawn chance. Although it's cheap to calculate, the setting is unlikely to change
    // and therefore the output will be the same.
    private static float getSpawnChance() {
        if (TotoCarpetSettings.catSpawnChance != lastSpawnChance) {
            spawnChance = (float)TotoCarpetSettings.catSpawnChance / 100;
            lastSpawnChance = TotoCarpetSettings.catSpawnChance;
        }
        return spawnChance;
    }
}
