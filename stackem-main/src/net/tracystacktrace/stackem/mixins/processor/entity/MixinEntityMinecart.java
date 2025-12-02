package net.tracystacktrace.stackem.mixins.processor.entity;

import net.minecraft.common.entity.Entity;
import net.minecraft.common.entity.other.EntityMinecart;
import net.minecraft.common.world.World;
import net.tracystacktrace.stackem.hacks.ITextureHolder;
import net.tracystacktrace.stackem.modifications.entityvariation.EntityVariation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityMinecart.class)
public abstract class MixinEntityMinecart extends Entity implements ITextureHolder {
    public MixinEntityMinecart(World world) {
        super(world);
    }

    @Unique
    private String stackem$textureMinecart = null;

    @Override
    public String stackem$getCachedTexture() {
        if (this.stackem$textureMinecart == null) {
            final String alter = EntityVariation.getRandomTextureFor(this);
            this.stackem$textureMinecart = alter != null ? alter : "/textures/entity/minecart.png";
        }
        return this.stackem$textureMinecart;
    }
}
