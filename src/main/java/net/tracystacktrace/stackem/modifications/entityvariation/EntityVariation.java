package net.tracystacktrace.stackem.modifications.entityvariation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.common.entity.Entity;
import net.minecraft.common.entity.EntityList;
import net.tracystacktrace.stackem.StackEm;
import net.tracystacktrace.stackem.tools.QuickRNG;
import net.tracystacktrace.stackem.tools.json.JsonExtractionException;
import net.tracystacktrace.stackem.tools.json.ThrowingJson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class EntityVariation {
    public record Description(
            @NotNull String entity, boolean forceOnly,
            @NotNull String @NotNull [] variants
    ) {
    }

    private static @NotNull Set<@NotNull String> getLowerCasedEntityNames() {
        return EntityList.getEntityTypeNames().stream().map(String::toLowerCase).collect(Collectors.toUnmodifiableSet());
    }

    public static @Nullable String getRandomTextureFor(@NotNull Entity entity) {
        String entityName = EntityList.getEntityString(entity);
        if (entityName == null) return null;
        entityName = entityName.toLowerCase();
        if (!StackEm.getContainerInstance().getDeepMeta().hasVariationsForEntity(entityName)) return null;

        final List<String> candidates = StackEm.getContainerInstance().getDeepMeta().entityVariations.get(entityName);
        final int picked = QuickRNG.getBetween(entity.entityId, 0, candidates.size());

        return candidates.get(picked);
    }

    public static @NotNull EntityVariation.Description @NotNull [] fromJson(
            @NotNull final String input,
            @NotNull final String sourceName
    ) throws JsonExtractionException {
        return fromJson(ThrowingJson.stringToJsonObject(input, sourceName), sourceName);
    }

    public static @NotNull EntityVariation.Description @NotNull [] fromJson(
            @NotNull final JsonObject object,
            @NotNull final String sourceName
    ) throws JsonExtractionException {
        final JsonArray data_array = ThrowingJson.cautiouslyGetArray(object, "data", sourceName);

        final List<Description> cooked_descriptions = new ArrayList<>();
        final Set<String> lowercasedNames = getLowerCasedEntityNames();

        for (JsonElement element : data_array) {
            if (!element.isJsonObject())
                throw new JsonExtractionException(JsonExtractionException.INVALID_OBJECT, sourceName, element.toString());

            final JsonObject entity_data = element.getAsJsonObject();

            String entity_data_raw = ThrowingJson.cautiouslyGetString(entity_data, "entity", sourceName);
            entity_data_raw = entity_data_raw.replace("minecraft:", "").replace("reindev:", "").toLowerCase();

            if (lowercasedNames.contains(entity_data_raw)) {
                //create and save entity descriptor
                boolean forceOnly = false;
                try {
                    forceOnly = ThrowingJson.cautiouslyGetBoolean(entity_data, "force_only", sourceName);
                } catch (JsonExtractionException ignored) {
                }

                final String[] variants = ThrowingJson.cautiouslyGetStringArray(object, "variants", sourceName);

                cooked_descriptions.add(new EntityVariation.Description(entity_data_raw, forceOnly, variants));
            } else {
                StackEm.LOGGER.warning("Found unknown entity \"" + entity_data.get("entity").getAsString() + "\" in a texturepack's stackem.entity.json, ignoring...");
            }
        }

        return cooked_descriptions.toArray(new EntityVariation.Description[0]);
    }
}
