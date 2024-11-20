package org.zeith.modid.custom.blocks;

import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.networking.energy.IEnergySource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.zeith.hammerlib.tiles.TileSyncableTickable;
import org.zeith.modid.init.TileEntitiesMI;
import javax.annotation.Nullable;

public class FeBlockEntity extends TileSyncableTickable implements IEnergySource {
    private static final int MAX_FE = 16000;
    private static final int MAX_DISTANCE = 8;
    private int storedEnergy = 0;

    // Constructor
    public FeBlockEntity(BlockPos pos, BlockState state) { super(TileEntitiesMI.FE_BLOCK_ENTITY, pos, state); }

    @Override
    public void update() {
        if (level == null || level.isClientSide) return;

        level.getEntitiesOfClass(LightningBolt.class, new AABB(worldPosition).inflate(MAX_DISTANCE))
                .forEach(bolt -> {
                    if (!bolt.getTags().contains("generated_energy")) {
                        bolt.addTag("generated_energy");

                        double distance = Math.sqrt(bolt.distanceToSqr(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()));

                        if (distance <= MAX_DISTANCE) {
                            storedEnergy += (int) (MAX_FE * (1.0 - (distance / MAX_DISTANCE)));
                            storedEnergy = Math.min(storedEnergy, MAX_FE);
                        }
                    }
                });

        for (Direction dir : Direction.values()) {
            BlockEntity neighbor = level.getBlockEntity(worldPosition.relative(dir));

            if (neighbor != null) {
                neighbor.getCapability(ForgeCapabilities.ENERGY, dir.getOpposite()).ifPresent(storage -> {
                    int toTransfer = Math.min(storedEnergy, 100);
                    int received = storage.receiveEnergy(toTransfer, false);

                    storedEnergy -= received;

                    if (storedEnergy <= 0) {
                        storedEnergy = 0;
                    }
                });
            }
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
    public double extractAEPower(double amt, Actionable mode, PowerMultiplier usePowerMultiplier) {
        double energyToExtract = Math.min(storedEnergy / 2.0, amt);

        if (mode == Actionable.MODULATE) {
            storedEnergy -= energyToExtract * 2;
        }
        return energyToExtract;
    }

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
        public int getEnergyStored() {
            return storedEnergy;
        }

        @Override
        public int getMaxEnergyStored() {
            return MAX_FE;
        }

        @Override
        public boolean canExtract() {
            return true;
        }

        @Override
        public boolean canReceive() {
            return false;
        }
    }
}