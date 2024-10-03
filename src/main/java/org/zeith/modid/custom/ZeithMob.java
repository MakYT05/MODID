package org.zeith.modid.custom;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import org.zeith.modid.init.EntitiesMI;

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

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }
    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(EntitiesMI.ZEITH_MOB, createAttributes().build());
    }
}