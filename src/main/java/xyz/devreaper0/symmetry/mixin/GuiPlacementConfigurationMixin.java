package xyz.devreaper0.symmetry.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.litematica.gui.GuiPlacementConfiguration;
import fi.dy.masa.litematica.gui.widgets.WidgetListPlacementSubRegions;
import fi.dy.masa.litematica.gui.widgets.WidgetPlacementSubRegion;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.schematic.placement.SubRegionPlacement;
import fi.dy.masa.malilib.gui.GuiListBase;
import fi.dy.masa.malilib.gui.button.ButtonOnOff;
import fi.dy.masa.malilib.gui.widgets.WidgetCheckBox;
import fi.dy.masa.malilib.util.position.PositionUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.devreaper0.symmetry.Symmetry;

@Mixin(GuiPlacementConfiguration.class)
public abstract class GuiPlacementConfigurationMixin extends GuiListBase<SubRegionPlacement, WidgetPlacementSubRegion, WidgetListPlacementSubRegions> {
    @Final
    @Shadow
    public SchematicPlacement placement;

    protected GuiPlacementConfigurationMixin(int listX, int listY) {
        super(listX, listY);
    }

    @ModifyExpressionValue(method = "createButtonOnOff", at = @At(value = "NEW", target = "Lfi/dy/masa/malilib/gui/button/ButtonOnOff;"))
    private ButtonOnOff createButtonOnOff(ButtonOnOff button, int x, int y, int width, boolean isCurrentlyOn, GuiPlacementConfiguration.ButtonListener.Type type) {
        if (type == GuiPlacementConfiguration.ButtonListener.Type.TOGGLE_LOCKED && Symmetry.getInstance().isBasePlacement(this.placement)) {
            button.setEnabled(false);
        } else if (type == GuiPlacementConfiguration.ButtonListener.Type.TOGGLE_ENTITIES && Symmetry.getInstance().isSymmetrySchematic(this.placement)) {
            button.setEnabled(false);
        }
        return button;
    }

    @Inject(method = "createCoordinateInput", at = @At("TAIL"))
    private void createCoordinateInput(int x, int y, int width, PositionUtils.CoordinateType type, CallbackInfo ci, @Local(name = "cb") WidgetCheckBox cb) {
        if (Symmetry.getInstance().isBasePlacement(this.placement)) {
            cb.setListener((WidgetCheckBox entry) -> entry.setChecked(true, false));
        }
    }
}
