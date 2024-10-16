package org.zeith.modid.client;

import net.minecraft.client.renderer.entity.EntityRenderers;
import org.zeith.modid.init.EntitiesMI;

public class ModEntityRenderers {

    public static void registerRenderers() {
        EntityRenderers.register(EntitiesMI.LIGHTNING_BALL, LightningBoltRenderer::new);
        EntityRenderers.register(EntitiesMI.ZEITH_MOB, ZeithMobRenderer::new);
    }
}