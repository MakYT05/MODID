package org.zeith.modid.custom;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.core.BlockPos;

public class ZeithEggItem extends Item {
    private final EntityType<?> type;

    public ZeithEggItem(EntityType<?> type, Item.Properties properties) {
        super(properties);
        this.type = type;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();

        if (!world.isClientSide()) {
            ServerLevel serverWorld = (ServerLevel) world;
            BlockPos clickedPos = context.getClickedPos();
            Player player = context.getPlayer();

            type.spawn(serverWorld, null, player, clickedPos, MobSpawnType.SPAWN_EGG, true, false);

            context.getItemInHand().hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(context.getHand()));
        }

        return InteractionResult.sidedSuccess(world.isClientSide());
    }
}