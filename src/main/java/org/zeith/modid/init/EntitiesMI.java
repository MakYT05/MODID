package org.zeith.modid.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.modid.custom.entyties.LightningBoltProjectile;
import org.zeith.modid.custom.entyties.ZeithMob;

@SimplyRegister
public interface EntitiesMI {
    @RegistryName("lightning_ball")
    EntityType<LightningBoltProjectile> LIGHTNING_BALL = EntityType.Builder.<LightningBoltProjectile>of(LightningBoltProjectile::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .setTrackingRange(80)
            .setUpdateInterval(1)
            .build("lightning_ball");

    @RegistryName("zeith_mob")
    EntityType<ZeithMob> ZEITH_MOB = EntityType.Builder.of(ZeithMob::new, MobCategory.MONSTER)
            .sized(1F, 1.5F)
            .build("zeith_mob");
}