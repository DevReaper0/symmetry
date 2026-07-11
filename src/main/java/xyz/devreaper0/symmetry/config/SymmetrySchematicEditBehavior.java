package xyz.devreaper0.symmetry.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

public enum SymmetrySchematicEditBehavior implements IConfigOptionListEntry, StringRepresentable {
    DISABLE("disable"),
    REPLICAS_ONLY("replicas_only"),
    ENABLE("enable");

    public static final StringRepresentable.EnumCodec<SymmetrySchematicEditBehavior> CODEC = StringRepresentable.fromEnum(SymmetrySchematicEditBehavior::values);
    public static final ImmutableList<SymmetrySchematicEditBehavior> VALUES = ImmutableList.copyOf(values());

    private final String configString;
    private final String translationKey;

    SymmetrySchematicEditBehavior(String configString) {
        this.configString = configString;
        this.translationKey = "symmetry.gui.label.symmetry_schematic_edit_behavior." + configString;
    }

    @Override
    @NonNull
    public String getSerializedName() {
        return this.configString;
    }

    @Override
    public String getStringValue() {
        return this.configString;
    }

    @Override
    public String getDisplayName() {
        return StringUtils.translate(this.translationKey);
    }

    @Override
    public SymmetrySchematicEditBehavior cycle(boolean forward) {
        int id = this.ordinal();

        if (forward) {
            if (++id >= values().length) {
                id = 0;
            }
        } else {
            if (--id < 0) {
                id = values().length - 1;
            }
        }

        return values()[id % values().length];
    }

    @Override
    public SymmetrySchematicEditBehavior fromString(String name) {
        return fromStringStatic(name);
    }

    public static SymmetrySchematicEditBehavior fromStringStatic(String name) {
        for (SymmetrySchematicEditBehavior val : values()) {
            if (val.configString.equalsIgnoreCase(name)) {
                return val;
            }
        }

        return DISABLE;
    }
}
