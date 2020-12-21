package carpettotosextras.mixins.villagecats;

import carpettotosextras.TotoCarpetSettings;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.CatSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CatSpawner.class)
public class CatSpawnerMixin {
    private static int lastSpawnChance;
    private static float spawnChance;

    @Inject(method = "spawnInHouse", at = @At(value = "HEAD"))
    private void onSpawnInHouse(ServerWorld world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        // Fallback to NMS
        if (TotoCarpetSettings.catSpawnChance == 0) return;
        // If set to 100%, disable spawning all together
        if (TotoCarpetSettings.catSpawnChance == 100) {
            cir.setReturnValue(0);
            return;
        }

        if (world.random.nextFloat() < getSpawnChance()) {
            cir.setReturnValue(0);
            return;
        }
    }

    private static float getSpawnChance() {
        if (TotoCarpetSettings.catSpawnChance != lastSpawnChance) {
            spawnChance = (float)TotoCarpetSettings.catSpawnChance / 100;
        }
        return spawnChance;
    }
}
