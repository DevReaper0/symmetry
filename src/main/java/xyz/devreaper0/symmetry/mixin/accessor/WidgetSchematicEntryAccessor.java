package xyz.devreaper0.symmetry.mixin.accessor;

import fi.dy.masa.litematica.gui.widgets.WidgetSchematicEntry;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WidgetSchematicEntry.class)
public interface WidgetSchematicEntryAccessor {
    @Accessor("schematic")
    LitematicaSchematic symmetry$getSchematic();
}
