package org.zeith.modid.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import org.zeith.modid.custom.ZeithMob;

public class ZeithMobRenderer extends EntityRenderer<ZeithMob> {

    private static final ModelLayerLocation MODEL_LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "zeith_mob"), "main");
    private static final ResourceLocation TEXTURE = new ResourceLocation("modid", "textures/entity/zeith_mob.png");
    private final ModelPart model;

    public ZeithMobRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = context.bakeLayer(MODEL_LAYER_LOCATION);
    }

    @Override
    public ResourceLocation getTextureLocation(ZeithMob entity) {
        return TEXTURE;
    }

    @Override
    public void render(ZeithMob entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        matrixStack.pushPose();
        matrixStack.translate(0.0D, 0.0D, 0.0D);
        model.render(matrixStack, buffer.getBuffer(RenderType.entityCutout(getTextureLocation(entity))), packedLight, OverlayTexture.NO_OVERLAY);
        matrixStack.popPose();
    }
}