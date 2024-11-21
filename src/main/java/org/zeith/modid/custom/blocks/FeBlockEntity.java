package org.zeith.modid.custom.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.zeith.hammerlib.tiles.TileSyncableTickable;
import org.zeith.modid.init.TileEntitiesMI;

import java.util.HashSet;
import java.util.Set;

public class FeBlockEntity extends TileSyncableTickable {

    private static final int MAX_FE = 16000;
    private static final int MAX_DISTANCE = 8;
    private static final int MAX_TRANSFER = 100;
    private int storedEnergy = 0;
    private final Set<LightningBolt> processedBolts = new HashSet<>();

    public FeBlockEntity(BlockPos pos, BlockState state) { super(TileEntitiesMI.FE_BLOCK_ENTITY, pos, state); }

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

        for (Direction direction : Direction.values()) {
            BlockEntity neighbor = level.getBlockEntity(worldPosition.relative(direction));

            if (neighbor != null) {
                neighbor.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).ifPresent(storage -> {
                    int toTransfer = Math.min(storedEnergy, MAX_TRANSFER);
                    int received = storage.receiveEnergy(toTransfer, false);

                    storedEnergy -= received;

                    if (storedEnergy <= 0) {
                        storedEnergy = 0;
                    }
                });
            }
        }
    }

    public int getEnergyStored() { return storedEnergy; }
}