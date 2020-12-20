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

    @Inject(method = "spawnInHouse", at = @At(value = "HEAD"))
    private void onSpawnInHouse(ServerWorld world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (TotoCarpetSettings.catsNoSpawnInVillage) {
            cir.setReturnValue(0);
        }
    }
}
