package org.zeith.modid.client;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.zeith.modid.custom.ZeithMob;

public class ZeithMobRenderer extends EntityRenderer<ZeithMob> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("modid", "textures/entity/zeith_mob.png");

    public ZeithMobRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(ZeithMob entity) {
        return TEXTURE;
    }
}