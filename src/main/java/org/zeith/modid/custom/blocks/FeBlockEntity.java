package org.zeith.modid.custom.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.zeith.hammerlib.tiles.TileSyncableTickable;
import org.zeith.modid.init.TileEntitiesMI;

import java.util.HashSet;
import java.util.Set;

public class FeBlockEntity extends TileSyncableTickable implements IEnergyStorage {
    private static final int MAX_FE = 16000;
    private int storedEnergy = 0;
    private Set<LightningBolt> processedBolts = new HashSet<>();

    public FeBlockEntity(BlockPos pos, BlockState state) { super(TileEntitiesMI.FE_BLOCK_ENTITY, pos, state); }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int energyReceived = Math.min(MAX_FE - storedEnergy, maxReceive);

        if (!simulate) {
            storedEnergy += energyReceived;
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int energyExtracted = Math.min(storedEnergy, maxExtract);

        if (!simulate) {
            storedEnergy -= energyExtracted;
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() { return storedEnergy; }

    @Override
    public int getMaxEnergyStored() { return MAX_FE; }

    @Override
    public boolean canExtract() { return true; }

    @Override
    public boolean canReceive() { return true; }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return LazyOptional.of(() -> (T) this);
        }
        return super.getCapability(cap, side);
    }

    public void updateEnergyFromLightning(Level level) {
        if (level == null || level.isClientSide) return;

        level.getEntitiesOfClass(LightningBolt.class, new AABB(worldPosition).inflate(8))
                .forEach(bolt -> {
                    if (!processedBolts.contains(bolt)) {
                        processedBolts.add(bolt);

                        double distance = Math.sqrt(bolt.distanceToSqr(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()));

                        if (distance <= 8) {
                            int generatedEnergy = (int) (MAX_FE * (1.0 - (distance / 8)));

                            storedEnergy += generatedEnergy;
                            storedEnergy = Math.min(storedEnergy, MAX_FE);
                        }
                    }
                });
    }

    public void transferEnergy() {
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = worldPosition.relative(direction);
            BlockEntity neighborBlockEntity = level.getBlockEntity(neighborPos);

            if (neighborBlockEntity != null) {
                LazyOptional<IEnergyStorage> capability = neighborBlockEntity.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite());
                capability.ifPresent(energyStorage -> {
                    int energyToSend = Math.min(storedEnergy, 1000);
                    int energyReceived = energyStorage.receiveEnergy(energyToSend, false);

                    storedEnergy -= energyReceived;
                });
            }
        }
    }

    public void tick() {
        if (level != null && !level.isClientSide) {
            updateEnergyFromLightning(level);
            transferEnergy();
        }
    }
}