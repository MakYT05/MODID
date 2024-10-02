package org.zeith.modid.custom;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;;
import net.minecraft.world.level.Level;

public class ZeithMob extends Mob {

    public ZeithMob(EntityType<? extends Mob> type, Level world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected void defineSynchedData() {}
}