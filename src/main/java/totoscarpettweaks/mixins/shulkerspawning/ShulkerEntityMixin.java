package totoscarpettweaks.mixins.shulkerspawning;

import carpet.utils.Messenger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import totoscarpettweaks.TotoCarpetSettings;

@Mixin(ShulkerEntity.class)
public abstract class ShulkerEntityMixin extends GolemEntity {
    private static final int NEARBY_SHULKER_UPPERBOUNDS = 5;
    private static final int NEARBY_SHULKER_LOWERBOUNDS = 0;
    private boolean inDamageStep;
    private boolean triedToTeleport;

    protected ShulkerEntityMixin(EntityType<? extends GolemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    protected abstract boolean isClosed();

    @Shadow
    protected abstract boolean tryTeleport();


    @Inject(method = "damage", at = @At("HEAD"))
    private void beforeDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (TotoCarpetSettings.shulkerBulletsSpawnShulkers)
            setInDamageStep(true);
    }

    @Inject(method = "damage", at = @At("RETURN"))
    private void afterDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (inDamageStep) {
            // Check if the return value is true, otherwise damage was mitigated and we shouldn't bother checking further.
            if (cir.getReturnValue()
                    // Check shulker hasn't yet attempted to teleport (25% chance if health is lower than 50%)
                    && !triedToTeleport
                    && source.isProjectile()) {
                Entity projectile = source.getSource();
                if (projectile != null && projectile.getType() == EntityType.SHULKER_BULLET) {
                    trySpawnShulker();
                }
            }
            setInDamageStep(false);
        }
    }

    @Inject(method = "tryTeleport", at = @At("RETURN"))
    private void onTryTeleport(CallbackInfoReturnable<Boolean> cir) {
        if (inDamageStep)
            triedToTeleport = true;
    }

    private void setInDamageStep(boolean value) {
        inDamageStep = value;
        triedToTeleport = false;
    }

    private boolean trySpawnShulker() {
        Vec3d pos = getPos();
        Box boundingBox = getBoundingBox();
        if (!isClosed() && tryTeleport() && shouldSpawnNewShulker(boundingBox)) {
            ShulkerEntity newShulker = EntityType.SHULKER.create(world);
            newShulker.refreshPositionAfterTeleport(pos);
            world.spawnEntity(newShulker);
            return true;
        }
        return false;
    }

    private boolean shouldSpawnNewShulker(Box boundingBox) {
        int nearbyShulkers = world.getEntitiesByType(EntityType.SHULKER, boundingBox.expand(8.0), Entity::isAlive).size() - 1;
        if (nearbyShulkers >= NEARBY_SHULKER_UPPERBOUNDS)
            return false;
        if (nearbyShulkers <= NEARBY_SHULKER_LOWERBOUNDS)
            return true;
        return world.random.nextFloat() >= (float) nearbyShulkers / 5.0f;
    }
}
