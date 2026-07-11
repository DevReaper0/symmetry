package xyz.devreaper0.symmetry.mixin;

import fi.dy.masa.litematica.gui.widgets.WidgetSchematicEntry;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.devreaper0.symmetry.Symmetry;
import xyz.devreaper0.symmetry.mixin.accessor.WidgetSchematicEntryAccessor;

@Mixin(targets = "fi/dy/masa/litematica/gui/widgets/WidgetSchematicEntry$ButtonListener")
public abstract class WidgetSchematicEntryButtonListenerMixin {
    @Final
    @Shadow
    private WidgetSchematicEntry widget;

    @Inject(method = "actionPerformedWithButton", at = @At(value = "INVOKE", target = "Lfi/dy/masa/litematica/data/SchematicHolder;removeSchematic(Lfi/dy/masa/litematica/schematic/LitematicaSchematic;)Z"))
    private void beforeUnload(ButtonBase button, int mouseButton, CallbackInfo ci) {
        LitematicaSchematic schematic = ((WidgetSchematicEntryAccessor) this.widget).symmetry$getSchematic();
        Symmetry.getInstance().unloadSchematic(schematic);
    }
}
