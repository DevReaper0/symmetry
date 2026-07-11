package xyz.devreaper0.symmetry.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import fi.dy.masa.litematica.gui.GuiConfigs;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.devreaper0.symmetry.config.Configs;
import xyz.devreaper0.symmetry.config.Hotkeys;

import java.util.List;

@Mixin(GuiConfigs.class)
public class GuiConfigsMixin {
    @ModifyExpressionValue(method = "getConfigs", at = @At(value = "FIELD", target = "Lfi/dy/masa/litematica/config/Configs$Generic;OPTIONS:Lcom/google/common/collect/ImmutableList;", opcode = Opcodes.GETSTATIC))
    private ImmutableList<IConfigBase> getConfigsGeneric(ImmutableList<IConfigBase> original) {
        return ImmutableList.<IConfigBase>builder()
                .addAll(original)
                .addAll(Configs.OPTIONS)
                .build();
    }

    @ModifyExpressionValue(method = "getConfigs", at = @At(value = "FIELD", target = "Lfi/dy/masa/litematica/config/Hotkeys;HOTKEY_LIST:Ljava/util/List;", opcode = Opcodes.GETSTATIC))
    private List<ConfigHotkey> getConfigsHotkeys(List<ConfigHotkey> original) {
        return ImmutableList.<ConfigHotkey>builder()
                .addAll(original)
                .addAll(Hotkeys.HOTKEY_LIST)
                .build();
    }

    @ModifyExpressionValue(method = "getAllConfigs", at = @At(value = "FIELD", target = "Lfi/dy/masa/litematica/config/Configs$Generic;OPTIONS:Lcom/google/common/collect/ImmutableList;", opcode = Opcodes.GETSTATIC))
    private ImmutableList<IConfigBase> getAllConfigsGeneric(ImmutableList<IConfigBase> original) {
        return ImmutableList.<IConfigBase>builder()
                .addAll(original)
                .addAll(Configs.OPTIONS)
                .build();
    }

    @ModifyExpressionValue(method = "getAllConfigs", at = @At(value = "FIELD", target = "Lfi/dy/masa/litematica/config/Hotkeys;HOTKEY_LIST:Ljava/util/List;", opcode = Opcodes.GETSTATIC))
    private List<ConfigHotkey> getAllConfigsHotkeys(List<ConfigHotkey> original) {
        return ImmutableList.<ConfigHotkey>builder()
                .addAll(original)
                .addAll(Hotkeys.HOTKEY_LIST)
                .build();
    }
}
