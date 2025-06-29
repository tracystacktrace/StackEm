package net.tracystacktrace.stackem.processor.category;

public enum EnumCategory {
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
        if(id == null || id.isEmpty()) {
            return null;
        }

        return switch (id.trim().toLowerCase()) {
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
}
