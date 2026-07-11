package xyz.devreaper0.symmetry;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import org.jspecify.annotations.NonNull;

import java.util.List;

public enum SymmetryType implements IConfigOptionListEntry, StringRepresentable {
    MIRROR_XZ("mirror_xz", 4),
    MIRROR_Z("mirror_z", 2),
    MIRROR_X("mirror_x", 2),
    CIRCLE_CLOCKWISE("circle_clockwise", 4),
    CIRCLE_COUNTERCLOCKWISE("circle_counterclockwise", 4);

    private final String configString;
    private final String translationKey;
    private final int placementCount;

    SymmetryType(String configString, int placementCount) {
        this.configString = configString;
        this.translationKey = "symmetry.gui.label.symmetry_type." + configString;
        this.placementCount = placementCount;
    }

    public int getPlacementCount() {
        return this.placementCount;
    }

    public Mirror getMirror(int placementIndex) {
        return switch (this) {
            case MIRROR_XZ -> switch (placementIndex) {
                case 0, 3 -> Mirror.NONE;
                case 1 -> Mirror.LEFT_RIGHT;
                case 2 -> Mirror.FRONT_BACK;
                default -> throw new MatchException(null, null);
            };
            case MIRROR_Z -> switch (placementIndex) {
                case 0 -> Mirror.NONE;
                case 1 -> Mirror.LEFT_RIGHT;
                default -> throw new MatchException(null, null);
            };
            case MIRROR_X -> switch (placementIndex) {
                case 0 -> Mirror.NONE;
                case 1 -> Mirror.FRONT_BACK;
                default -> throw new MatchException(null, null);
            };
            case CIRCLE_CLOCKWISE, CIRCLE_COUNTERCLOCKWISE -> switch (placementIndex) {
                case 0, 1, 2, 3 -> Mirror.NONE;
                default -> throw new MatchException(null, null);
            };
        };
    }

    public Rotation getRotation(int placementIndex) {
        return switch (this) {
            case MIRROR_XZ -> switch (placementIndex) {
                case 0, 1, 2 -> Rotation.NONE;
                case 3 -> Rotation.CLOCKWISE_180;
                default -> throw new MatchException(null, null);
            };
            case MIRROR_Z, MIRROR_X -> switch (placementIndex) {
                case 0, 1 -> Rotation.NONE;
                default -> throw new MatchException(null, null);
            };
            case CIRCLE_CLOCKWISE -> switch (placementIndex) {
                case 0 -> Rotation.NONE;
                case 1 -> Rotation.CLOCKWISE_90;
                case 2 -> Rotation.CLOCKWISE_180;
                case 3 -> Rotation.COUNTERCLOCKWISE_90;
                default -> throw new MatchException(null, null);
            };
            case CIRCLE_COUNTERCLOCKWISE -> switch (placementIndex) {
                case 0 -> Rotation.NONE;
                case 1 -> Rotation.COUNTERCLOCKWISE_90;
                case 2 -> Rotation.CLOCKWISE_180;
                case 3 -> Rotation.CLOCKWISE_90;
                default -> throw new MatchException(null, null);
            };
        };
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
    public List<String> getHoverText() {
        if (this == CIRCLE_CLOCKWISE || this == CIRCLE_COUNTERCLOCKWISE) {
            return StringUtils.translateAndLineSplit(this.translationKey + ".hover");
        }
        return List.of();
    }

    @Override
    public SymmetryType cycle(boolean forward) {
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
    public SymmetryType fromString(String name) {
        return fromStringStatic(name);
    }

    public static SymmetryType fromStringStatic(String name) {
        for (SymmetryType val : values()) {
            if (val.configString.equalsIgnoreCase(name)) {
                return val;
            }
        }

        return MIRROR_XZ;
    }
}
