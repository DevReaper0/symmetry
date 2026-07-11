package xyz.devreaper0.symmetry.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.litematica.gui.widgets.WidgetSchematicEntry;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase;
import fi.dy.masa.malilib.render.GuiContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.devreaper0.symmetry.Symmetry;
import xyz.devreaper0.symmetry.gui.Icons;

@Mixin(WidgetSchematicEntry.class)
public abstract class WidgetSchematicEntryMixin extends WidgetListEntryBase<LitematicaSchematic> {
    @Final
    @Shadow
    private LitematicaSchematic schematic;

    @Final
    @Shadow
    private int typeIconX;
    @Final
    @Shadow
    private int typeIconY;

    public WidgetSchematicEntryMixin(int x, int y, int width, int height, LitematicaSchematic entry, int listIndex) {
        super(x, y, width, height, entry, listIndex);
    }

    @Inject(method = "addButton", at = @At("TAIL"))
    private void addButton(int x, int y, @Coerce Enum<?> type, CallbackInfoReturnable<Integer> cir, @Local(name = "button") ButtonGeneric button) {
        if (type.name().equals("CREATE_PLACEMENT") || type.name().equals("RELOAD")) {
            if (Symmetry.getInstance().isSymmetrySchematic(this.schematic)) {
                button.setEnabled(false);
            }
        }
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lfi/dy/masa/litematica/gui/Icons;renderAt(Lfi/dy/masa/malilib/render/GuiContext;IIFZZ)V", ordinal = 0))
    private void renderSymmetryIcon(fi.dy.masa.litematica.gui.Icons instance, GuiContext ctx, int x, int y, float zLevel, boolean enabled, boolean selected, Operation<Void> original) {
        if (Symmetry.getInstance().isSymmetrySchematic(this.schematic)) {
            Icons.SCHEMATIC_TYPE_SYMMETRY.renderAt(ctx, this.typeIconX, this.typeIconY, this.zLevel, false, false);
        } else {
            original.call(instance, ctx, x, y, zLevel, enabled, selected);
        }
    }
}
