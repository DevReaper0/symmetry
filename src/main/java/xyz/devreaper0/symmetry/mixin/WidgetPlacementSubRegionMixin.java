package xyz.devreaper0.symmetry.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fi.dy.masa.litematica.gui.widgets.WidgetPlacementSubRegion;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.schematic.placement.SubRegionPlacement;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase;
import fi.dy.masa.malilib.render.GuiContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import xyz.devreaper0.symmetry.Symmetry;
import xyz.devreaper0.symmetry.gui.Icons;

@Mixin(WidgetPlacementSubRegion.class)
public abstract class WidgetPlacementSubRegionMixin extends WidgetListEntryBase<SubRegionPlacement> {
    @Final
    @Shadow
    private SchematicPlacement schematicPlacement;

    public WidgetPlacementSubRegionMixin(int x, int y, int width, int height, SubRegionPlacement entry, int listIndex) {
        super(x, y, width, height, entry, listIndex);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lfi/dy/masa/litematica/gui/Icons;renderAt(Lfi/dy/masa/malilib/render/GuiContext;IIFZZ)V", ordinal = 0))
    private void renderSymmetryIcon(fi.dy.masa.litematica.gui.Icons instance, GuiContext ctx, int x, int y, float zLevel, boolean enabled, boolean selected, Operation<Void> original) {
        if (Symmetry.getInstance().isSymmetrySchematic(this.schematicPlacement)) {
            Icons.SCHEMATIC_TYPE_SYMMETRY.renderAt(ctx, this.x + 2, this.y + 5, this.zLevel, false, false);
        } else {
            original.call(instance, ctx, x, y, zLevel, enabled, selected);
        }
    }
}
