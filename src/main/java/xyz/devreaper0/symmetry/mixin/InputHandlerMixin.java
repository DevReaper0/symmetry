package xyz.devreaper0.symmetry.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.event.InputHandler;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.tool.ToolMode;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.hotkeys.KeybindMulti;
import net.minecraft.client.Minecraft;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.devreaper0.symmetry.Symmetry;
import xyz.devreaper0.symmetry.config.Configs;
import xyz.devreaper0.symmetry.config.Hotkeys;
import xyz.devreaper0.symmetry.config.SymmetrySchematicEditBehavior;

import java.util.List;

@Mixin(InputHandler.class)
public class InputHandlerMixin {
    @ModifyExpressionValue(method = "addKeysToMap", at = @At(value = "FIELD", target = "Lfi/dy/masa/litematica/config/Hotkeys;HOTKEY_LIST:Ljava/util/List;", opcode = Opcodes.GETSTATIC))
    private List<ConfigHotkey> addKeysToMap(List<ConfigHotkey> original) {
        return ImmutableList.<ConfigHotkey>builder()
                .addAll(original)
                .addAll(Hotkeys.HOTKEY_LIST)
                .build();
    }

    @ModifyExpressionValue(method = "addHotkeys", at = @At(value = "FIELD", target = "Lfi/dy/masa/litematica/config/Hotkeys;HOTKEY_LIST:Ljava/util/List;", opcode = Opcodes.GETSTATIC))
    private List<ConfigHotkey> addHotkeys(List<ConfigHotkey> original) {
        return ImmutableList.<ConfigHotkey>builder()
                .addAll(original)
                .addAll(Hotkeys.HOTKEY_LIST)
                .build();
    }

    @Inject(method = "handleAttackKey", at = @At("HEAD"), cancellable = true)
    private void handleAttackKey(Minecraft mc, CallbackInfoReturnable<Boolean> cir) {
        if (mc.player != null && DataManager.getToolMode() == ToolMode.REBUILD && KeybindMulti.getTriggeredCount() == 0) {
            List<SchematicPlacement> placements = Symmetry.getInstance().getTargetedSchematicPlacements();
            for (SchematicPlacement placement : placements) {
                if (Configs.SYMMETRY_SCHEMATIC_EDIT_BEHAVIOR.getOptionListValue() == SymmetrySchematicEditBehavior.DISABLE && Symmetry.getInstance().isSymmetrySchematic(placement)) {
                    cir.setReturnValue(false);
                    return;
                }
                if (Configs.SYMMETRY_SCHEMATIC_EDIT_BEHAVIOR.getOptionListValue() == SymmetrySchematicEditBehavior.REPLICAS_ONLY && Symmetry.getInstance().isBasePlacement(placement)) {
                    cir.setReturnValue(false);
                    return;
                }
            }
        }
    }

    @Inject(method = "handleUseKey", at = @At("HEAD"), cancellable = true)
    private void handleUseKey(Minecraft mc, CallbackInfoReturnable<Boolean> cir) {
        if (mc.player != null && DataManager.getToolMode() == ToolMode.REBUILD) {
            List<SchematicPlacement> placements = Symmetry.getInstance().getTargetedSchematicPlacements();
            for (SchematicPlacement placement : placements) {
                if (Configs.SYMMETRY_SCHEMATIC_EDIT_BEHAVIOR.getOptionListValue() == SymmetrySchematicEditBehavior.DISABLE && Symmetry.getInstance().isSymmetrySchematic(placement)) {
                    cir.setReturnValue(false);
                    return;
                }
                if (Configs.SYMMETRY_SCHEMATIC_EDIT_BEHAVIOR.getOptionListValue() == SymmetrySchematicEditBehavior.REPLICAS_ONLY && Symmetry.getInstance().isBasePlacement(placement)) {
                    cir.setReturnValue(false);
                    return;
                }
            }
        }
    }
}
