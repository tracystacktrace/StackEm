package net.tracystacktrace.stackem.mixins.processor.entity;

import net.minecraft.common.entity.EntityLiving;
import net.minecraft.common.entity.monsters.EntityZombie;
import net.minecraft.common.world.World;
import net.tracystacktrace.stackem.modifications.entityvariation.EntityVariation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityZombie.class)
public abstract class MixinEntityZombie extends EntityLiving {
    public MixinEntityZombie(World world) {
        super(world);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void stackem$entityVariation(World world, CallbackInfo ci) {
        final String alter = EntityVariation.getRandomTextureFor(this);
        if (alter != null) {
            this.texture = alter;
        }
    }
}
