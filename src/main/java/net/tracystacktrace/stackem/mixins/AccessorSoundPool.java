package net.tracystacktrace.stackem.mixins;

import net.minecraft.client.sound.SoundPool;
import net.minecraft.client.sound.SoundPoolEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(SoundPool.class)
public interface AccessorSoundPool {

    @Accessor("allSoundPoolEntries")
    List<SoundPoolEntry> stackem$entryList();

}
