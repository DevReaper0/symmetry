package xyz.devreaper0.symmetry.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigOptionList;

public class Configs {
    private static final String GENERIC_KEY = "symmetry.config.generic";

    public static final ConfigOptionList SYMMETRY_SCHEMATIC_EDIT_BEHAVIOR = new ConfigOptionList("symmetrySchematicEditBehavior", SymmetrySchematicEditBehavior.DISABLE).apply(GENERIC_KEY);
    public static final ConfigBoolean SYMMETRIZE_IF_SELECTED_PLACEMENT = new ConfigBoolean("symmetrizeIfSelectedPlacement", false).apply(GENERIC_KEY);
    public static final ConfigBoolean IGNORE_SERVER_SYMMETRY_EDITS = new ConfigBoolean("ignoreServerSymmetryEdits", false).apply(GENERIC_KEY);

    public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
            SYMMETRY_SCHEMATIC_EDIT_BEHAVIOR,
            SYMMETRIZE_IF_SELECTED_PLACEMENT,
            IGNORE_SERVER_SYMMETRY_EDITS
    );
}
