package xyz.devreaper0.symmetry.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.devreaper0.symmetry.Symmetry;
import xyz.devreaper0.symmetry.config.Configs;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level {
    private ClientLevelMixin(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeRegistration, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates) {
        super(levelData, dimension, registryAccess, dimensionTypeRegistration, isClientSide, isDebug, biomeZoomSeed, maxChainedNeighborUpdates);
    }

    @Inject(method = "setServerVerifiedBlockState", at = @At("HEAD"))
    private void setServerVerifiedBlockIfBasePlacement(BlockPos pos, BlockState blockState, int updateFlag, CallbackInfo ci) {
        BlockState oldState = this.getBlockState(pos);
        if (oldState == blockState) {
            return;
        }
        if (Configs.IGNORE_SERVER_SYMMETRY_EDITS.getBooleanValue()) {
            return;
        }
        Symmetry.getInstance().setBlockIfBasePlacement(pos, blockState);
    }

    @Inject(method = "setBlock", at = @At("HEAD"))
    private void setBlockIfBasePlacement(BlockPos pos, BlockState blockState, int updateFlags, int updateLimit, CallbackInfoReturnable<Boolean> cir) {
        BlockState oldState = this.getBlockState(pos);
        if (oldState == blockState) {
            return;
        }
        Symmetry.getInstance().setBlockIfBasePlacement(pos, blockState);
    }
}
