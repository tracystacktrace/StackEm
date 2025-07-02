package net.tracystacktrace.stackem.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.ITexturePack;
import net.minecraft.client.renderer.block.TexturePackList;
import net.tracystacktrace.stackem.StackEm;
import net.tracystacktrace.stackem.impl.TexturePackStacked;
import net.tracystacktrace.stackem.tools.QuickRNG;
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
            final List<String> candidates = new ArrayList<>(Arrays.asList(StackEm.unpackSaveString(this.mc.gameSettings.texturePack)));
            for (File var3 : getTexturePackDirContents()) {
                if (!var3.isDirectory() && var3.getName().toLowerCase().endsWith(".zip")) {
                    if (candidates.contains(var3.getName())) {
                        collector.add(var3);
                    }
                }
            }
        }

        this.selectedTexturePack = new TexturePackStacked(QuickRNG.getRandomIdentifier(), defaultTexturePack, collector);

        String[] finished = new String[collector.size()];
        for (int i = 0; i < collector.size(); i++) {
            finished[i] = collector.get(i).getName();
        }

        this.mc.gameSettings.texturePack = StackEm.packSaveString(finished);
        this.mc.gameSettings.saveOptions();

        ci.cancel();
    }
}
