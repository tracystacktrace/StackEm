package net.tracystacktrace.stackem.neptune.category;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public enum EnumCategory {
    AUDIO("audio"),
    ANIMATION("animation"),
    BLOCKS("blocks"),
    ENTITIES("entities"),
    ENVIRONMENT("environment"),
    FONTS("fonts"),
    GUI("gui"),
    HUD("hud"),
    ITEMS("items"),
    PAINTINGS("paintings"),
    QOL("qol"),
    QUEER("queer");

    public final @NotNull String identifier;

    EnumCategory(@NotNull String identifier) {
        this.identifier = identifier;
    }

    public @NotNull String cookI18NString() {
        return "stackem.category." + this.identifier;
    }

    public static @Nullable EnumCategory define(@Nullable String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }

        final String trimmedID = id.trim();
        for (EnumCategory category : EnumCategory.values()) {
            if (category.identifier.equalsIgnoreCase(trimmedID)) {
                return category;
            }
        }

        return null;
    }

    @SuppressWarnings({"ForLoopReplaceableByForEach", "ManualArrayToCollectionCopy", "UseBulkOperation"})
    public static @NotNull String @Nullable [] collect(
            @NotNull Function<@NotNull String, @NotNull String> translator,
            @NotNull EnumCategory @Nullable [] categories,
            @NotNull String @Nullable [] custom
    ) {
        final List<String> names = new ArrayList<>();

        if (categories != null && categories.length > 0) {
            for (int i = 0; i < categories.length; i++) {
                names.add(translator.apply(categories[i].cookI18NString()));
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

        return names.toArray(new String[0]);
    }
}
