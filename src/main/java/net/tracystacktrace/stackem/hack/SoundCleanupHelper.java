package net.tracystacktrace.stackem.hack;

import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.SoundPool;
import net.tracystacktrace.stackem.mixins.AccessorSoundManager;
import net.tracystacktrace.stackem.mixins.AccessorSoundPool;
import paulscode.sound.SoundSystem;

/**
 * A small hijack stuff
 */
public final class SoundCleanupHelper {

    public static void cleanupSoundSources(SoundManager manager) {
        final SoundSystem soundSystem = ((AccessorSoundManager)manager).stackem$getSoundSystem();
        final AccessorSoundManager accessor = (AccessorSoundManager) manager;

        soundSystem.removeTemporarySources();

        cleanSound(soundSystem, accessor.stackem$sp1());
        cleanSound(soundSystem, accessor.stackem$sp2());
        cleanSound(soundSystem, accessor.stackem$sp3());
        cleanSound(soundSystem, accessor.stackem$sp4());
        cleanSound(soundSystem, accessor.stackem$sp5());
        cleanSound(soundSystem, accessor.stackem$sp6());
        cleanSound(soundSystem, accessor.stackem$sp7());
        cleanSound(soundSystem, accessor.stackem$sp8());
        cleanSound(soundSystem, accessor.stackem$sp9());
    }

    static void cleanSound(
            final SoundSystem soundSystem,
            final SoundPool pool
    ) {
        ((AccessorSoundPool) pool).stackem$entryList().forEach(entry -> {
            soundSystem.unloadSound(entry.soundName);
            soundSystem.removeSource(entry.soundName);
        });
    }

}
