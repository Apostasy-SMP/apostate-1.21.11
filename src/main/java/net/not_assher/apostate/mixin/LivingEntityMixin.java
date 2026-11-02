package net.not_assher.apostate.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.not_assher.apostate.core.cca.entity.PlayerComponent;
import net.not_assher.apostate.core.utilities.ModUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Chemthunder
 */
@Mixin(value = LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "onKilledBy", at = @At(value = "TAIL"))
    private void apostate$redeemBounty(LivingEntity adversary, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (adversary instanceof PlayerEntity player) {
            if (self instanceof PlayerEntity target) {
                ModUtils.redeemBounty(player, target);
            }
        }
    }

    @Inject(method = "applyMovementInput", at = @At("HEAD"), cancellable = true)
    public void mindsEye$forceZeroMovementInput(Vec3d movementInput, float slipperiness, CallbackInfoReturnable<Vec3d> cir) {
        LivingEntity living = (LivingEntity)(Object)this;
        if (living instanceof PlayerEntity player) {
            if (PlayerComponent.KEY.get(player).isAfk()) {
                cir.setReturnValue(Vec3d.ZERO);
            }
        }
    }

    @WrapMethod(method = "damage")
    private boolean apostate$noDamageWhileAFK(ServerWorld world, DamageSource source, float amount, Operation<Boolean> original) {
        LivingEntity living = (LivingEntity)(Object)this;
        if (living instanceof PlayerEntity player) {
            if (PlayerComponent.KEY.get(player).isAfk()) {
                return false;
            }
        }
        return original.call(world, source, amount);
    }
}
