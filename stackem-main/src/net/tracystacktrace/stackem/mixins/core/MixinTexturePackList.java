package net.tracystacktrace.stackem.mixins.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.ITexturePack;
import net.minecraft.client.renderer.block.TexturePackList;
import net.tracystacktrace.stackem.StackEm;
import net.tracystacktrace.stackem.impl.ModernStackedImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Mixin(TexturePackList.class)
public abstract class MixinTexturePackList {
    @Shadow
    private ITexturePack selectedTexturePack;

    @Shadow
    @Final
    private static ITexturePack defaultTexturePack;

    @Shadow
    protected abstract List<File> getTexturePackDirContents();

    @Shadow
    @Final
    private Minecraft mc;

    @Inject(method = "updateAvaliableTexturePacks", at = @At("HEAD"), cancellable = true)
    private void stackem$injectReplaceLoader(CallbackInfo ci) {
        this.selectedTexturePack = defaultTexturePack;

        final List<File> collector = new ArrayList<>();

        if (this.mc.gameSettings.texturePack.startsWith("stackem")) {
            final String[] candidates = StackEm.unpackSaveString(this.mc.gameSettings.texturePack);
            List<File> files = getTexturePackDirContents();

            Arrays.stream(candidates)
                    .map(c -> files.stream()
                            .filter(f -> f.getName().toLowerCase().endsWith(".zip"))
                            .filter(f -> f.getName().contains(c))
                            .findFirst()
                            .orElse(null))
                    .filter(Objects::nonNull)
                    .forEach(collector::add);
        }

        String[] finished = new String[collector.size()];
        for (int i = 0; i < collector.size(); i++) {
            finished[i] = collector.get(i).getName();
        }

        this.selectedTexturePack = new ModernStackedImpl(StackEm.getInternalRndIdentifier(), defaultTexturePack, collector);

        this.mc.gameSettings.texturePack = StackEm.packSaveString(finished);
        this.mc.gameSettings.saveOptions();

        ci.cancel();
    }
}
