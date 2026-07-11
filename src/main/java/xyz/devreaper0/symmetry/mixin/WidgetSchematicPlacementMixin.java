package xyz.devreaper0.symmetry.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fi.dy.masa.litematica.gui.widgets.WidgetSchematicPlacement;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase;
import fi.dy.masa.malilib.render.GuiContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import xyz.devreaper0.symmetry.Symmetry;
import xyz.devreaper0.symmetry.gui.Icons;

@Mixin(WidgetSchematicPlacement.class)
public abstract class WidgetSchematicPlacementMixin extends WidgetListEntryBase<SchematicPlacement> {
    @Final
    @Shadow
    public SchematicPlacement placement;

    public WidgetSchematicPlacementMixin(int x, int y, int width, int height, SchematicPlacement entry, int listIndex) {
        super(x, y, width, height, entry, listIndex);
    }

    @ModifyExpressionValue(method = "createButtonGeneric", at = @At(value = "NEW", target = "Lfi/dy/masa/malilib/gui/button/ButtonGeneric;"))
    private ButtonGeneric disableRemoveButton(ButtonGeneric button, int xRight, int y, WidgetSchematicPlacement.ButtonListener.ButtonType type) {
        if (type == WidgetSchematicPlacement.ButtonListener.ButtonType.REMOVE && Symmetry.getInstance().isSymmetrySchematic(this.placement)) {
            button.setEnabled(false);
        }
        return button;
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lfi/dy/masa/litematica/gui/Icons;renderAt(Lfi/dy/masa/malilib/render/GuiContext;IIFZZ)V", ordinal = 0))
    private void renderSymmetryIcon(fi.dy.masa.litematica.gui.Icons instance, GuiContext ctx, int x, int y, float zLevel, boolean enabled, boolean selected, Operation<Void> original) {
        if (Symmetry.getInstance().isSymmetrySchematic(this.placement)) {
            Icons.SCHEMATIC_TYPE_SYMMETRY.renderAt(ctx, this.x + 2, this.y + 5, this.zLevel, false, false);
        } else {
            original.call(instance, ctx, x, y, zLevel, enabled, selected);
        }
    }
}
