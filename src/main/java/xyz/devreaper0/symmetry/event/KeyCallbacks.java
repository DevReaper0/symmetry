package xyz.devreaper0.symmetry.event;

import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.selection.AreaSelection;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.GuiUtils;
import xyz.devreaper0.symmetry.config.Configs;
import xyz.devreaper0.symmetry.config.Hotkeys;
import xyz.devreaper0.symmetry.gui.GuiSymmetrizeSelection;

public class KeyCallbacks {
    public static void init() {
        Hotkeys.SYMMETRIZE_SELECTION.getKeybind().setCallback((_, _) -> {
            AreaSelection area = DataManager.getSelectionManager().getCurrentSelection();
            if (area == null) {
                return true;
            }
            if (!Configs.SYMMETRIZE_IF_SELECTED_PLACEMENT.getBooleanValue() && DataManager.getSchematicPlacementManager().getSelectedSchematicPlacement() != null) {
                return true;
            }

            GuiSymmetrizeSelection gui = new GuiSymmetrizeSelection(area.getName(), GuiUtils.getCurrentScreen());
            GuiBase.openGui(gui);
            return true;
        });
    }
}
