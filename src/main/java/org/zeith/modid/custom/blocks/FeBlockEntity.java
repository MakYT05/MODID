package org.zeith.modid.custom.blocks;

import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridNode;
import appeng.api.networking.energy.IEnergySource;
import appeng.me.GridNode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.tiles.TileSyncableTickable;
import org.zeith.modid.init.TileEntitiesMI;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

@SimplyRegister
public class FeBlockEntity extends TileSyncableTickable implements IEnergySource {
    private static final int MAX_FE = 16000;
    private static final int MAX_DISTANCE = 8;
    private int storedEnergy = 0;

    private IGridNode gridNode;

    private final Set<LightningBolt> processedBolts = new HashSet<>();

    public FeBlockEntity(BlockPos pos, BlockState state) { super(TileEntitiesMI.FE_BLOCK_ENTITY, pos, state); }

    @Override
    public void setLevel(@Nullable Level level) {
        super.setLevel(level);

        if (level != null && !level.isClientSide) {
            this.gridNode = createGridNode();

            if (gridNode != null) {
                gridNode.getGrid();
            }
        }
    }

    @Override
    public void update() {
        if (level == null || level.isClientSide) return;

        level.getEntitiesOfClass(LightningBolt.class, new AABB(worldPosition).inflate(MAX_DISTANCE))
                .forEach(bolt -> {
                    if (!processedBolts.contains(bolt)) {
                        processedBolts.add(bolt);

                        double distance = Math.sqrt(bolt.distanceToSqr(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()));
                        if (distance <= MAX_DISTANCE) {
                            int generated = (int) (MAX_FE * (1.0 - (distance / MAX_DISTANCE)));
                            storedEnergy += generated;
                            storedEnergy = Math.min(storedEnergy, MAX_FE);
                        }
                    }
                });

        if (gridNode != null && gridNode.isActive()) {
            double energyToInject = Math.min(storedEnergy / 2.0, 10);
            storedEnergy -= energyToInject * 2;
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return LazyOptional.of(() -> new EnergyStorage()).cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public double extractAEPower(double amt, Actionable mode, PowerMultiplier multiplier) {
        if (gridNode != null && gridNode.isActive()) {
            double extracted = Math.min(storedEnergy, amt);
            storedEnergy -= extracted;
            return extracted;
        }
        return 0;
    }

    public IGridNode getGridNode() {
        if (gridNode == null && level instanceof ServerLevel serverLevel) {
            gridNode = createGridNode();
        }
        return gridNode;
    }

    private IGridNode createGridNode() {
        if (level instanceof ServerLevel serverLevel) {
            return new GridNode(serverLevel, this, null, EnumSet.noneOf(GridFlags.class));
        }
        return null;
    }

    public int getEnergyStored() { return storedEnergy; }

    private class EnergyStorage implements IEnergyStorage {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) { return 0; }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            int extracted = Math.min(storedEnergy, maxExtract);

            if (!simulate) {
                storedEnergy -= extracted;
            }
            return extracted;
        }

        @Override
        public int getEnergyStored() { return storedEnergy; }

        @Override
        public int getMaxEnergyStored() { return MAX_FE; }

        @Override
        public boolean canExtract() { return true; }

        @Override
        public boolean canReceive() { return false; }
    }
}