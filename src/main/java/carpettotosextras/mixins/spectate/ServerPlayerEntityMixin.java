package carpettotosextras.mixins.spectate;

import carpettotosextras.fakes.ServerPlayerEntityInterface;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
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
    public static final String NBT_PREFIX = "CarpetTotosExtras_";
    public static final String NBT_SURVIVALX = getTagKey("SurvivalX");
    public static final String NBT_SURVIVALY = getTagKey("SurvivalX");
    public static final String NBT_SURVIVALZ = getTagKey("SurvivalZ");
    public static final String NBT_SURVIVALYAW = getTagKey("SurvivalYaw");
    public static final String NBT_SURVIVALPITCH = getTagKey("SurvivalPitch");
    public static final String NBT_SURVIVALWORLD = getTagKey("SurvivalWorld");

    private double survivalX;
    private double survivalY;
    private double survivalZ;
    private float survivalYaw;
    private float survivalPitch;
    private Optional<RegistryKey<World>> survivalWorldKey;

    @Shadow
    public ServerPlayNetworkHandler networkHandler;

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
        survivalWorldKey = Optional.empty();
    }

    @Shadow
    public abstract void teleport(ServerWorld targetWorld, double x, double y, double z, float yaw, float pitch);

    @Override
    public void carpettotosextras_rememberSurvivalPosition() {
        if (isPlayerStillJoining())
            return;
        survivalX = getX();
        survivalY = getY();
        survivalZ = getZ();
        survivalYaw = getYaw(1);
        survivalPitch = getPitch(1);
        survivalWorldKey = Optional.ofNullable(getEntityWorld().getRegistryKey());
        System.out.println("I member");
    }

    @Override
    public void carpettotosextras_teleportToSurvivalPosition() {
        if (survivalX == 0 || survivalY == 0 || survivalZ == 0 || survivalYaw == 0 || survivalPitch == 0 || !survivalWorldKey.isPresent() || isPlayerStillJoining())
            return;
        ServerWorld world = getServer().getWorld(survivalWorldKey.get());
        if (world != null)
            teleport(world, survivalX, survivalY, survivalZ, survivalYaw, survivalPitch);
    }

    @Inject(method = "writeCustomDataToTag", at = @At(value = "HEAD"))
    private void writeSurvivalPosition(CompoundTag tag, CallbackInfo ci) {
        tag.putDouble(NBT_SURVIVALX, survivalX);
        tag.putDouble(NBT_SURVIVALY, survivalY);
        tag.putDouble(NBT_SURVIVALZ, survivalZ);
        tag.putFloat(NBT_SURVIVALYAW, survivalYaw);
        tag.putFloat(NBT_SURVIVALPITCH, survivalPitch);
        tag.putString(NBT_SURVIVALWORLD, survivalWorldKey.map(rk -> rk.getValue().toString()).orElse("none"));
    }

    @Inject(method = "readCustomDataFromTag", at = @At(value = "HEAD"))
    private void readSurvivalPosition(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains(NBT_SURVIVALX))
            survivalX = tag.getDouble(NBT_SURVIVALX);
        if (tag.contains(NBT_SURVIVALY))
            survivalY = tag.getDouble(NBT_SURVIVALY);
        if (tag.contains(NBT_SURVIVALZ))
            survivalZ = tag.getDouble(NBT_SURVIVALZ);
        if (tag.contains(NBT_SURVIVALYAW))
            survivalYaw = tag.getFloat(NBT_SURVIVALYAW);
        if (tag.contains(NBT_SURVIVALPITCH))
            survivalPitch = tag.getFloat(NBT_SURVIVALPITCH);
        if (tag.contains(NBT_SURVIVALWORLD)) {
            // Couldn't find a lookup function so using if statements but there may be a way to do a lookup
            Identifier worldId = Identifier.tryParse(tag.getString("CarpetSurvivalWorld"));
            if (World.OVERWORLD.getValue().equals(worldId))
                survivalWorldKey = Optional.of(World.OVERWORLD);
            else if (World.NETHER.getValue().equals(worldId))
                survivalWorldKey = Optional.of(World.NETHER);
            else if (World.END.getValue().equals(worldId))
                survivalWorldKey = Optional.of(World.END);
        }
    }

    private boolean isPlayerStillJoining() {
        return networkHandler == null;
    }

    private static String getTagKey(String key) {
        return NBT_PREFIX + key;
    }
}
