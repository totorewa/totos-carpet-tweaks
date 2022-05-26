package totoscarpettweaks.mixins.seespectators;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import totoscarpettweaks.TotoCarpetSettings;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    public ServerPlayerEntityMixin(MinecraftServer server, ServerWorld world, GameProfile profile, @Nullable PlayerPublicKey publicKey) {
        super(world, world.getSpawnPos(), world.getSpawnAngle(), profile, publicKey);
    }

    @Shadow
    protected abstract Entity getCameraEntity();
    @Shadow
    public abstract boolean isSpectator();

    @Inject(method = "updatePotionVisibility", at = @At("HEAD"), cancellable = true)
    private void noInvisibleSpectators(CallbackInfo ci) {
        if (TotoCarpetSettings.visibleSpectators) {
            if (isSpectator()) clearPotionSwirls();
            else super.updatePotionVisibility();
            ci.cancel();
        }
    }

    @Inject(method = "canBeSpectated", at = @At("HEAD"), cancellable = true)
    private void allowSpectatorsToBeSpectated(ServerPlayerEntity spectator, CallbackInfoReturnable<Boolean> cir) {
        if (TotoCarpetSettings.visibleSpectators) {
            if (spectator.isSpectator()) cir.setReturnValue(getCameraEntity() == (Object) this);
            else cir.setReturnValue(super.canBeSpectated(spectator));
        }
    }
}
