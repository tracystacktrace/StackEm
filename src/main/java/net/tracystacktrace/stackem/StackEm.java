package net.tracystacktrace.stackem;

import com.fox2code.foxevents.EventHandler;
import com.fox2code.foxloader.client.KeyBindingAPI;
import com.fox2code.foxloader.event.client.GuiItemInfoEvent;
import com.fox2code.foxloader.loader.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.KeyBinding;
import net.minecraft.common.util.i18n.StringTranslate;
import net.tracystacktrace.stackem.impl.TexturePackStacked;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import java.util.logging.Logger;

public class StackEm extends Mod {
    public static final StackEmConfig CONFIG = new StackEmConfig();
    public static final Logger LOGGER = Logger.getLogger("STACKEM");
    public static final KeyBinding DEBUG_KEYBIND_FALLBACK = new KeyBinding("stackem.keybind.fallback", Keyboard.KEY_F10);
    public static boolean DEBUG_FORCE_FALLBACK = false;

    public static TexturePackStacked getContainerInstance() {
        return (TexturePackStacked) Minecraft.getInstance().texturePackList.getSelectedTexturePack();
    }

    public static void toggleDefaultTextures() {
        final Minecraft mc = Minecraft.getInstance();

        // handle safely
        if (mc.thePlayer == null) {
            return;
        }

        StackEm.DEBUG_FORCE_FALLBACK = !StackEm.DEBUG_FORCE_FALLBACK;
        mc.renderEngine.refreshTextures();
        mc.thePlayer.addChatMessage(StringTranslate.getInstance().translateKey(StackEm.DEBUG_FORCE_FALLBACK ? "stackem.debug.on" : "stackem.debug.off"));
    }

    public static @NotNull String @NotNull [] unpackSaveString(String input) {
        final int start = input.indexOf('[');
        final int end = input.indexOf(']');

        if (start == -1 || end == -1 || !input.contains("stackem")) {
            return new String[0];
        }

        return input.substring(start + 1, end).split(";");
    }

    public static @NotNull String packSaveString(String[] input) {
        if (input == null || input.length < 1) {
            return "stackem[]";
        }
        return "stackem[" + String.join(";", input) + "]";
    }

    public static boolean isValidWebsite(@Nullable String website) {
        return website != null && (website.startsWith("https://") || website.startsWith("http://"));
    }

    @Override
    public void onPreInit() {
        this.setConfigObject(CONFIG);
        KeyBindingAPI.registerKeyBinding(DEBUG_KEYBIND_FALLBACK);
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void eventProvideMetadataInfo(GuiItemInfoEvent event) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
            event.addDescriptionLine("§8[meta/max]: " + event.getItemStack().getItemDamage() + "/" + event.getItemStack().getMaxDamage());
        }
    }
}
