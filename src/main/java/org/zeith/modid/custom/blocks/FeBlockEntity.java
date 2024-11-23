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

import java.util.List;

public class FeBlockEntity extends TileSyncableTickable implements IEnergyStorage {
    private static final int MAX_FE = 16000;
    private int storedEnergy = 0;

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

        List<LightningBolt> bolts = level.getEntitiesOfClass(LightningBolt.class, new AABB(worldPosition).inflate(8));

        for (LightningBolt bolt : bolts) {
            if (bolt.tickCount > 1 || bolt.getTags().contains("processed")) continue;

            BlockPos closestBlock = null;
            double closestDistance = Double.MAX_VALUE;

            for (int x = -8; x <= 8; x++) {
                for (int y = -8; y <= 8; y++) {
                    for (int z = -8; z <= 8; z++) {
                        BlockPos pos = worldPosition.offset(x, y, z);
                        BlockEntity blockEntity = level.getBlockEntity(pos);

                        if (blockEntity instanceof FeBlockEntity) {
                            double distance = worldPosition.distSqr(pos);

                            if (distance < closestDistance) {
                                closestDistance = distance;
                                closestBlock = pos;
                            }
                        }
                    }
                }
            }

            if (closestBlock != null) {
                BlockEntity blockEntity = level.getBlockEntity(closestBlock);

                if (blockEntity instanceof FeBlockEntity feBlockEntity) {
                    double distance = Math.sqrt(bolt.distanceToSqr(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()));
                    int generatedEnergy = (int) (MAX_FE * (1.0 - (distance / 8.0)));

                    feBlockEntity.storedEnergy += generatedEnergy;
                    feBlockEntity.storedEnergy = Math.min(feBlockEntity.storedEnergy, MAX_FE);

                    bolt.addTag("processed");

                    break;
                }
            }
        }
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

    public void serverTick() {
        if (level != null && !level.isClientSide) {
            updateEnergyFromLightning(level);
            transferEnergy();
        }
    }
}