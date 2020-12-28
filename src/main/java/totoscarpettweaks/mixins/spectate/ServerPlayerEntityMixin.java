package totoscarpettweaks.mixins.spectate;

import net.minecraft.util.math.Vec3d;
import totoscarpettweaks.fakes.ServerPlayerEntityInterface;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;


@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements ServerPlayerEntityInterface {
    private static final String NBT_PREFIX = "CarpetTotosExtras_";
    private static final String NBT_SURVIVALX = getTagKey("SurvivalX");
    private static final String NBT_SURVIVALY = getTagKey("SurvivalY");
    private static final String NBT_SURVIVALZ = getTagKey("SurvivalZ");
    private static final String NBT_SURVIVALYAW = getTagKey("SurvivalYaw");
    private static final String NBT_SURVIVALPITCH = getTagKey("SurvivalPitch");
    private static final String NBT_SURVIVALWORLD = getTagKey("SurvivalWorld");
    private static final String UNKNOWN = "Unknown";

    private Optional<Vec3d> survivalPos;
    private float survivalYaw;
    private float survivalPitch;
    private Optional<RegistryKey<World>> survivalWorldKey;

    @Shadow
    public ServerPlayNetworkHandler networkHandler;

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
        survivalWorldKey = Optional.empty();
        survivalPos = Optional.empty();
    }

    @Shadow
    public abstract void teleport(ServerWorld targetWorld, double x, double y, double z, float yaw, float pitch);

    @Override
    public boolean canReturnSpectator() {
        return survivalPos.isPresent() && survivalWorldKey.isPresent() && !isPlayerStillJoining();
    }

    public Optional<Vec3d> getSurvivalPos() {
        return survivalPos;
    }

    @Override
    public String getSurvivalDimensionName() {
        return survivalWorldKey.map(r -> r.getValue().getPath()).orElse(UNKNOWN);
    }

    @Override
    public void rememberSurvivalPosition() {
        if (isPlayerStillJoining())
            return;
        setSurvivalPosition(getX(), getY(), getZ());
        survivalYaw = getYaw(1);
        survivalPitch = getPitch(1);
        survivalWorldKey = Optional.ofNullable(getEntityWorld().getRegistryKey());
    }

    @Override
    public boolean tryTeleportToSurvivalPosition() {
        if (canReturnSpectator()) {
            ServerWorld world = getServer().getWorld(survivalWorldKey.get());
            Vec3d pos = survivalPos.get();
            if (world != null) {
                teleport(world, pos.x, pos.y, pos.z, survivalYaw, survivalPitch);
                return true;
            }
        }
        return false;
    }

    @Inject(method = "writeCustomDataToTag", at = @At("HEAD"))
    private void writeSurvivalPosition(CompoundTag tag, CallbackInfo ci) {
        if (canReturnSpectator()) {
            Vec3d pos = survivalPos.get();
            tag.putDouble(NBT_SURVIVALX, pos.x);
            tag.putDouble(NBT_SURVIVALY, pos.y);
            tag.putDouble(NBT_SURVIVALZ, pos.z);
            tag.putFloat(NBT_SURVIVALYAW, survivalYaw);
            tag.putFloat(NBT_SURVIVALPITCH, survivalPitch);
            tag.putString(NBT_SURVIVALWORLD, survivalWorldKey.map(rk -> rk.getValue().toString()).orElse("none"));
        }
    }

    @Inject(method = "readCustomDataFromTag", at = @At("HEAD"))
    private void readSurvivalPosition(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains(NBT_SURVIVALX) && tag.contains(NBT_SURVIVALY) && tag.contains(NBT_SURVIVALZ))
            setSurvivalPosition(tag.getDouble(NBT_SURVIVALX), tag.getDouble(NBT_SURVIVALY), tag.getDouble(NBT_SURVIVALZ));
        if (tag.contains(NBT_SURVIVALYAW))
            survivalYaw = tag.getFloat(NBT_SURVIVALYAW);
        if (tag.contains(NBT_SURVIVALPITCH))
            survivalPitch = tag.getFloat(NBT_SURVIVALPITCH);
        if (tag.contains(NBT_SURVIVALWORLD)) {
            Identifier worldId = Identifier.tryParse(tag.getString(NBT_SURVIVALWORLD));
            if (worldId != null)
                survivalWorldKey = Optional.ofNullable(RegistryKey.of(Registry.DIMENSION, worldId));
        }
    }

    private void setSurvivalPosition(double x, double y, double z) {
        survivalPos = Optional.of(new Vec3d(x, y, z));
    }

    private boolean isPlayerStillJoining() {
        return networkHandler == null;
    }

    private static String getTagKey(String key) {
        return NBT_PREFIX + key;
    }
}
