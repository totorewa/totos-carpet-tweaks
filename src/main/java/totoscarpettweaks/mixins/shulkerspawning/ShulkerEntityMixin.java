package totoscarpettweaks.mixins.shulkerspawning;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import totoscarpettweaks.TotoCarpetSettings;

@Mixin(ShulkerEntity.class)
public abstract class ShulkerEntityMixin extends GolemEntity {

    protected ShulkerEntityMixin(EntityType<? extends GolemEntity> entityType, World world) {
        super(entityType, world);
    }


    @Inject(method = "spawnNewShulker", at = @At("HEAD"), cancellable = true)
    private void beforeDamage(CallbackInfo ci) {
        if (!TotoCarpetSettings.shulkerBulletsSpawnShulkers)
            ci.cancel();
    }
}
