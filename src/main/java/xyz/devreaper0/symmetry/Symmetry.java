package xyz.devreaper0.symmetry;

import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.scheduler.TaskScheduler;
import fi.dy.masa.litematica.scheduler.tasks.TaskSaveSchematic;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.schematic.SchematicMetadata;
import fi.dy.masa.litematica.schematic.container.LitematicaBlockStateContainer;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacementManager;
import fi.dy.masa.litematica.schematic.placement.SubRegionPlacement;
import fi.dy.masa.litematica.selection.AreaSelection;
import fi.dy.masa.litematica.selection.SelectionManager;
import fi.dy.masa.litematica.util.RayTraceUtils;
import fi.dy.masa.litematica.util.SchematicUtils;
import fi.dy.masa.litematica.util.WorldUtils;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.util.EntityUtils;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.malilib.util.position.PositionUtils;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import xyz.devreaper0.symmetry.event.KeyCallbacks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Symmetry implements ModInitializer {
    private static Symmetry INSTANCE;
    Map<LitematicaSchematic, SymmetryType> symmetrySchematics = new HashMap<>();

    public static Symmetry getInstance() {
        return INSTANCE;
    }

    @Override
    public void onInitialize() {
        INSTANCE = this;

        KeyCallbacks.init();
    }

    public void unloadSchematic(LitematicaSchematic schematic) {
        symmetrySchematics.remove(schematic);
    }

    public List<SchematicPlacement> getTargetedSchematicPlacements() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return List.of();
        }
        Entity entity = EntityUtils.getCameraEntity();
        if (entity == null) {
            return List.of();
        }
        double range = Math.max(10, WorldUtils.getValidBlockRange(mc));
        RayTraceUtils.RayTraceWrapper trace = RayTraceUtils.getGenericTrace(mc.level, entity, range);
        if (trace.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.SCHEMATIC_BLOCK) {
            return getTouchedPlacements(trace.getBlockHitResult().getBlockPos());
        }
        return List.of();
    }

    public List<SchematicPlacement> getTouchedPlacements(BlockPos pos) {
        List<SchematicPlacement> touched = new ArrayList<>();
        for (SchematicPlacementManager.PlacementPart part : DataManager.getSchematicPlacementManager().getAllPlacementsTouchingChunk(pos)) {
            if (part.getBox().contains(pos)) {
                SchematicPlacement placement = part.getPlacement();
                if (!touched.contains(placement)) {
                    touched.add(placement);
                }
            }
        }
        return touched;
    }

    public void setBlockIfBasePlacement(BlockPos pos, BlockState state) {
        SchematicPlacementManager pm = DataManager.getSchematicPlacementManager();

        List<LitematicaSchematic> toRebuild = new ArrayList<>();
        for (SchematicPlacementManager.PlacementPart part : pm.getAllPlacementsTouchingChunk(pos)) {
            SchematicPlacement placement = part.getPlacement();
            if (part.getBox().contains(pos) && isBasePlacement(placement)) {
                String regionName = part.getSubRegionName();
                LitematicaSchematic schematic = placement.getSchematic();
                LitematicaBlockStateContainer container = schematic.getSubRegionContainer(regionName);
                BlockPos posSchematic = SchematicUtils.getSchematicContainerPositionFromWorldPosition(pos, schematic, regionName, placement, placement.getRelativeSubRegionPlacement(regionName), container);
                if (posSchematic != null) {
                    state = SchematicUtils.getUntransformedBlockState(state, placement, regionName);
                    BlockState stateOriginal = container.get(posSchematic.getX(), posSchematic.getY(), posSchematic.getZ());

                    int totalBlocks = schematic.getMetadata().getTotalBlocks();
                    int increment;
                    if (!stateOriginal.isAir()) {
                        increment = !state.isAir() ? 0 : -1;
                    } else {
                        increment = !state.isAir() ? 1 : 0;
                    }
                    totalBlocks += increment;

                    container.set(posSchematic.getX(), posSchematic.getY(), posSchematic.getZ(), state);

                    SchematicMetadata metadata = schematic.getMetadata();
                    metadata.setTotalBlocks(totalBlocks);
                    metadata.setTimeModifiedToNow();
                    metadata.setModifiedSinceSaved();

                    toRebuild.add(schematic);
                }
            }
        }
        markSchematicsForRebuild(toRebuild);
    }

    public void markSchematicsForRebuild(List<LitematicaSchematic> toRebuild) {
        SchematicPlacementManager pm = DataManager.getSchematicPlacementManager();
        List<ChunkPos> markedChunks = new ArrayList<>();
        for (LitematicaSchematic schematic : toRebuild) {
            for (SchematicPlacement placement : pm.getAllPlacementsOfSchematic(schematic)) {
                SubRegionPlacement.RequiredEnabled re = SubRegionPlacement.RequiredEnabled.PLACEMENT_ENABLED;
                if (placement.matchesRequirement(re)) {
                    for (ChunkPos chunkPos : placement.getTouchedChunks()) {
                        if (!markedChunks.contains(chunkPos)) {
                            markedChunks.add(chunkPos);
                            pm.markChunkForRebuild(chunkPos);
                        }
                    }
                }
            }
        }
    }

    public void markSchematicForRebuild(LitematicaSchematic schematic) {
        SchematicPlacementManager pm = DataManager.getSchematicPlacementManager();
        List<ChunkPos> markedChunks = new ArrayList<>();
        for (SchematicPlacement placement : pm.getAllPlacementsOfSchematic(schematic)) {
            SubRegionPlacement.RequiredEnabled re = SubRegionPlacement.RequiredEnabled.PLACEMENT_ENABLED;
            if (placement.matchesRequirement(re)) {
                for (ChunkPos chunkPos : placement.getTouchedChunks()) {
                    if (!markedChunks.contains(chunkPos)) {
                        markedChunks.add(chunkPos);
                        pm.markChunkForRebuild(chunkPos);
                    }
                }
            }
        }
    }

    public boolean isSymmetrySchematic(SchematicPlacement placement) {
        if (placement == null) {
            return false;
        }
        return isSymmetrySchematic(placement.getSchematic());
    }

    public boolean isSymmetrySchematic(LitematicaSchematic schematic) {
        return symmetrySchematics.containsKey(schematic) && DataManager.getSchematicPlacementManager().getAllPlacementsOfSchematic(schematic).size() == symmetrySchematics.get(schematic).getPlacementCount();
    }

    public boolean isBasePlacement(SchematicPlacement placement) {
        if (placement == null) {
            return false;
        }
        LitematicaSchematic schematic = placement.getSchematic();
        return isSymmetrySchematic(schematic) && placement == DataManager.getSchematicPlacementManager().getAllPlacementsOfSchematic(schematic).getFirst();
    }

    public void symmetrizeSelection(String name, SymmetryType symmetryType) {
        Minecraft mc = Minecraft.getInstance();
        SelectionManager sm = DataManager.getSelectionManager();
        AreaSelection area = sm.getCurrentSelection();

        if (area == null || area.getAllSubRegionBoxes().isEmpty()) {
            InfoUtils.showGuiOrInGameMessage(Message.MessageType.ERROR, "litematica.message.error.no_area_selected");
            return;
        }
        LitematicaSchematic schematic = LitematicaSchematic.createEmptySchematic(area, mc.player.getName().getString());
        schematic.getMetadata().setName(name);

        LitematicaSchematic.SchematicSaveInfo info = new LitematicaSchematic.SchematicSaveInfo(false, true);
        TaskSaveSchematic taskSave = new TaskSaveSchematic(schematic, area, info);
        taskSave.disableCompletionMessage();

        symmetrizeSchematic(schematic, area.getEffectiveOrigin(), symmetryType);
        taskSave.setCompletionListener(() -> {
            symmetrySchematics.put(schematic, symmetryType);
            markSchematicForRebuild(schematic);
        });

        TaskScheduler.getServerInstanceIfExistsOrClient().scheduleTask(taskSave, 10);
    }

    public void symmetrizeSchematic(LitematicaSchematic schematic, BlockPos origin, SymmetryType symmetryType) {
        String name = schematic.getMetadata().getName();

        SchematicPlacementManager manager = DataManager.getSchematicPlacementManager();

        SchematicPlacement basePlacement = SchematicPlacement.createFor(schematic, origin, name + " (Symmetry Base)", true, true);
        basePlacement.toggleIgnoreEntities(null);
        basePlacement.setCoordinateLocked(PositionUtils.CoordinateType.X, true);
        basePlacement.setCoordinateLocked(PositionUtils.CoordinateType.Y, true);
        basePlacement.setCoordinateLocked(PositionUtils.CoordinateType.Z, true);
        basePlacement.toggleLocked();
        manager.addSchematicPlacement(basePlacement, false);
        manager.setSelectedSchematicPlacement(basePlacement);

        for (int i = 1; i < symmetryType.getPlacementCount(); i++) {
            SchematicPlacement placement = SchematicPlacement.createFor(schematic, origin, name + " (Symmetry " + i + "/" + (symmetryType.getPlacementCount() - 1) + ")", true, true);
            placement.toggleIgnoreEntities(null);
            placement.setMirror(symmetryType.getMirror(i), null);
            placement.setRotation(symmetryType.getRotation(i), null);
            placement.toggleLocked();
            manager.addSchematicPlacement(placement, false);
        }
    }
}
