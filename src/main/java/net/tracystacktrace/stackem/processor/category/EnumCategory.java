package net.tracystacktrace.stackem.processor.category;

import net.minecraft.common.util.i18n.StringTranslate;
import net.tracystacktrace.stackem.processor.StringManipulation;

import java.util.ArrayList;
import java.util.List;

public enum EnumCategory {
    AUDIO("audio"),
    ANIMATION("animation"),
    BLOCKS("blocks"),
    ENTITIES("entities"),
    FONTS("fonts"),
    GUI("gui"),
    HUD("hud"),
    ITEMS("items"),
    PAINTINGS("paintings"),
    QOL("qol"),
    QUEER("queer");

    public final String id;

    EnumCategory(String id) {
        this.id = id;
    }

    public String cookI18NString() {
        return "stackem.category." + this.id;
    }

    public static EnumCategory define(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }

        return switch (id.trim().toLowerCase()) {
            case "audio" -> AUDIO;
            case "animation" -> ANIMATION;
            case "blocks" -> BLOCKS;
            case "entities" -> ENTITIES;
            case "fonts" -> FONTS;
            case "gui" -> GUI;
            case "hud" -> HUD;
            case "items" -> ITEMS;
            case "paintings" -> PAINTINGS;
            case "qol" -> QOL;
            case "queer" -> QUEER;
            default -> null;
        };
    }

    @SuppressWarnings({"ForLoopReplaceableByForEach", "ManualArrayToCollectionCopy", "UseBulkOperation"})
    public static String[] collect(EnumCategory[] categories, String[] custom) {
        final List<String> names = new ArrayList<>();

        if (categories != null && categories.length > 0) {
            for (int i = 0; i < categories.length; i++) {
                names.add(StringTranslate.getInstance().translateKey(categories[i].cookI18NString()));
            }
        }

        if (custom != null && custom.length > 0) {
            for (int i = 0; i < custom.length; i++) {
                names.add(custom[i]);
            }
        }

        if (names.isEmpty()) {
            return null;
        }

        return StringManipulation.combinations(names, 56).toArray(new String[0]);
    }
}
