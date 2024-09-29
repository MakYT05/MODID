package org.zeith.modid.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.modid.custom.LightningBoltProjectile;

@SimplyRegister
public interface EntitiesMI {
    @RegistryName("lightning_ball")
    EntityType<LightningBoltProjectile> LIGHTNING_BALL = EntityType.Builder.<LightningBoltProjectile>of(LightningBoltProjectile::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .setTrackingRange(80)
            .setUpdateInterval(1)
            .build("lightning_ball");
}