package xyz.devreaper0.symmetry.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.devreaper0.symmetry.config.Hotkeys;

import java.util.List;

@Mixin(Configs.class)
public class ConfigsMixin {
    @ModifyExpressionValue(method = "loadFromFile", at = @At(value = "FIELD", target = "Lfi/dy/masa/litematica/config/Configs$Generic;OPTIONS:Lcom/google/common/collect/ImmutableList;", opcode = Opcodes.GETSTATIC))
    private static ImmutableList<IConfigBase> loadFromFileGeneric(ImmutableList<IConfigBase> original) {
        return ImmutableList.<IConfigBase>builder()
                .addAll(original)
                .addAll(xyz.devreaper0.symmetry.config.Configs.OPTIONS)
                .build();
    }

    @ModifyExpressionValue(method = "loadFromFile", at = @At(value = "FIELD", target = "Lfi/dy/masa/litematica/config/Hotkeys;HOTKEY_LIST:Ljava/util/List;", opcode = Opcodes.GETSTATIC))
    private static List<ConfigHotkey> loadFromFileHotkeys(List<ConfigHotkey> original) {
        return ImmutableList.<ConfigHotkey>builder()
                .addAll(original)
                .addAll(Hotkeys.HOTKEY_LIST)
                .build();
    }

    @ModifyExpressionValue(method = "saveToFile", at = @At(value = "FIELD", target = "Lfi/dy/masa/litematica/config/Configs$Generic;OPTIONS:Lcom/google/common/collect/ImmutableList;", opcode = Opcodes.GETSTATIC))
    private static ImmutableList<IConfigBase> saveToFileGeneric(ImmutableList<IConfigBase> original) {
        return ImmutableList.<IConfigBase>builder()
                .addAll(original)
                .addAll(xyz.devreaper0.symmetry.config.Configs.OPTIONS)
                .build();
    }

    @ModifyExpressionValue(method = "saveToFile", at = @At(value = "FIELD", target = "Lfi/dy/masa/litematica/config/Hotkeys;HOTKEY_LIST:Ljava/util/List;", opcode = Opcodes.GETSTATIC))
    private static List<ConfigHotkey> saveToFileHotkeys(List<ConfigHotkey> original) {
        return ImmutableList.<ConfigHotkey>builder()
                .addAll(original)
                .addAll(Hotkeys.HOTKEY_LIST)
                .build();
    }
}
