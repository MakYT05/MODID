package org.zeith.modid.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.api.forge.BlockAPI;
import org.zeith.modid.custom.blocks.FeBlockEntity;

@SimplyRegister
public interface TileEntitiesMI {
    @RegistryName("fe_block_entity")
    BlockEntityType<FeBlockEntity> FE_BLOCK_ENTITY = BlockAPI.createBlockEntityType(FeBlockEntity::new, BlocksMI.FE_BLOCK);
}