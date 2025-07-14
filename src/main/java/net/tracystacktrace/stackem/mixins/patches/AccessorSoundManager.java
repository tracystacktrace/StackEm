package net.tracystacktrace.stackem.mixins.patches;

import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.SoundPool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import paulscode.sound.SoundSystem;

@Mixin(SoundManager.class)
public interface AccessorSoundManager {
    @Accessor("sndSystem")
    SoundSystem stackem$getSoundSystem();

    @Accessor("soundPoolSounds")
    SoundPool stackem$sp1();

    @Accessor("soundPoolStreaming")
    SoundPool stackem$sp2();

    @Accessor("soundPoolLoops")
    SoundPool stackem$sp3();

    @Accessor("soundPoolMenu")
    SoundPool stackem$sp4();

    @Accessor("soundPoolMusic")
    SoundPool stackem$sp5();

    @Accessor("soundPoolMusicNight")
    SoundPool stackem$sp6();

    @Accessor("soundPoolMusicSkylands")
    SoundPool stackem$sp7();

    @Accessor("soundPoolMusicNetherAmbient")
    SoundPool stackem$sp8();

    @Accessor("soundPoolMusicNetherDungeon")
    SoundPool stackem$sp9();
}
