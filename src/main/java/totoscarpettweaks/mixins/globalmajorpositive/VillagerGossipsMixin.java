package totoscarpettweaks.mixins.globalmajorpositive;

import net.minecraft.village.VillageGossipType;
import net.minecraft.village.VillagerGossips;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import totoscarpettweaks.TotoCarpetSettings;

import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

import static net.minecraft.village.VillageGossipType.MAJOR_POSITIVE;

@Mixin(VillagerGossips.class)
public abstract class VillagerGossipsMixin {
    @Shadow
    @Final
    private Map<UUID, Object> entityReputation;

    @Inject(method = "getReputationFor(Ljava/util/UUID;Ljava/util/function/Predicate;)I", at = @At("HEAD"), cancellable = true)
    public void overrideReputation(UUID target, Predicate<VillageGossipType> filter, CallbackInfoReturnable<Integer> cir) {
        if (TotoCarpetSettings.sharedVillagerDiscounts && filter.test(MAJOR_POSITIVE)) {
            VillagerGossips$ReputationInvoker targetReputation = (VillagerGossips$ReputationInvoker)entityReputation.get(target);
            if (targetReputation == null) {
                cir.setReturnValue(0);
                return;
            }
            int otherRep = targetReputation.toto$getValueFor(vgt -> filter.test(vgt) && !vgt.equals(MAJOR_POSITIVE));
            int majorPositiveRep = entityReputation.values()
                    .stream()
                    .mapToInt(r -> ((VillagerGossips$ReputationInvoker) r).toto$getValueFor(vgt -> vgt.equals(MAJOR_POSITIVE)))
                    .sum();
            cir.setReturnValue(otherRep + Math.min(majorPositiveRep, MAJOR_POSITIVE.maxValue * MAJOR_POSITIVE.multiplier));
        }
    }
}
