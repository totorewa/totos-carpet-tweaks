package totoscarpettweaks.mixins.globalmajorpositive;

import net.minecraft.village.VillageGossipType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Predicate;

@Mixin(targets = "net.minecraft.village.VillagerGossips$Reputation")
public interface VillagerGossips$ReputationInvoker {
    @Invoker("getValueFor")
    public int toto$getValueFor(Predicate<VillageGossipType> gossipTypeFilter);
}
