package totoscarpettweaks.mixins.returnspectators;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import totoscarpettweaks.TotoCarpetSettings;
import totoscarpettweaks.fakes.ServerPlayerEntityInterface;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
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

    private Vec3d survivalPos;
    private float survivalYaw;
    private float survivalPitch;
    private RegistryKey<World> survivalWorldKey;

    @Shadow
    public ServerPlayNetworkHandler networkHandler;

    public ServerPlayerEntityMixin(MinecraftServer server, ServerWorld world, GameProfile profile, @Nullable PlayerPublicKey publicKey) {
        super(world, world.getSpawnPos(), world.getSpawnAngle(), profile, publicKey);
    }

    @Shadow
    public abstract void teleport(ServerWorld targetWorld, double x, double y, double z, float yaw, float pitch);

    @Override
    public boolean toto$hasReturnPosition() {
        return survivalPos != null && survivalWorldKey != null;
    }

    public Vec3d getSurvivalPosition() {
        return survivalPos;
    }

    public float getSurvivalYaw() {
        return survivalYaw;
    }

    public float getSurvivalPitch() {
        return survivalPitch;
    }

    public RegistryKey<World> getSurvivalWorldKey() {
        return survivalWorldKey;
    }

    @Override
    public String getSurvivalDimensionName() {
        if (survivalWorldKey == null)
            return UNKNOWN;
        return survivalWorldKey.getValue().getPath();
    }

    @Override
    public void rememberSurvivalPosition() {
        if (!isPlayerAlive())
            return;
        setSurvivalPosition(getX(), getY(), getZ());
        survivalYaw = getYaw(1);
        survivalPitch = getPitch(1);
        survivalWorldKey = getEntityWorld().getRegistryKey();
    }

    @Override
    public boolean tryReturnToSurvivalPosition() {
        if (toto$hasReturnPosition() && networkHandler != null && !isDead()) {
            ServerWorld world = getServer().getWorld(survivalWorldKey);
            if (world != null) {
                teleport(world, survivalPos.x, survivalPos.y, survivalPos.z, survivalYaw, survivalPitch);
                clearSurvivalPosition();
                return true;
            }

        }
        clearSurvivalPosition();
        return false;
    }

    private void clearSurvivalPosition() {
        survivalPos = null;
        survivalWorldKey = null;
        survivalYaw = 0;
        survivalPitch = 0;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void writeSurvivalPosition(NbtCompound tag, CallbackInfo ci) {
        if (TotoCarpetSettings.returnSpectators && toto$hasReturnPosition()) {
            tag.putDouble(NBT_SURVIVALX, survivalPos.x);
            tag.putDouble(NBT_SURVIVALY, survivalPos.y);
            tag.putDouble(NBT_SURVIVALZ, survivalPos.z);
            tag.putFloat(NBT_SURVIVALYAW, survivalYaw);
            tag.putFloat(NBT_SURVIVALPITCH, survivalPitch);
            tag.putString(NBT_SURVIVALWORLD, survivalWorldKey.getValue().toString());
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void readSurvivalPosition(NbtCompound tag, CallbackInfo ci) {
        if (TotoCarpetSettings.returnSpectators) {
            if (tag.contains(NBT_SURVIVALX) && tag.contains(NBT_SURVIVALY) && tag.contains(NBT_SURVIVALZ))
                setSurvivalPosition(tag.getDouble(NBT_SURVIVALX), tag.getDouble(NBT_SURVIVALY), tag.getDouble(NBT_SURVIVALZ));
            if (tag.contains(NBT_SURVIVALYAW))
                survivalYaw = tag.getFloat(NBT_SURVIVALYAW);
            if (tag.contains(NBT_SURVIVALPITCH))
                survivalPitch = tag.getFloat(NBT_SURVIVALPITCH);
            if (tag.contains(NBT_SURVIVALWORLD)) {
                Identifier worldId = Identifier.tryParse(tag.getString(NBT_SURVIVALWORLD));
                if (worldId != null)
                    survivalWorldKey = RegistryKey.of(Registry.WORLD_KEY, worldId);
            }
        }
    }

    @Inject(method = "copyFrom", at = @At("RETURN"))
    private void copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        ServerPlayerEntityInterface oldServerPlayer = (ServerPlayerEntityInterface) oldPlayer;
        survivalPos = oldServerPlayer.getSurvivalPosition();
        survivalPitch = oldServerPlayer.getSurvivalPitch();
        survivalYaw = oldServerPlayer.getSurvivalYaw();
        survivalWorldKey = oldServerPlayer.getSurvivalWorldKey();
    }

    private void setSurvivalPosition(double x, double y, double z) {
        survivalPos = new Vec3d(x, y, z);
    }

    private boolean isPlayerAlive() {
        return networkHandler != null && !isDead();
    }

    private static String getTagKey(String key) {
        return NBT_PREFIX + key;
    }
}
