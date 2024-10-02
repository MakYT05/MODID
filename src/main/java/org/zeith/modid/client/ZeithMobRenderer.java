package org.zeith.modid.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.zeith.modid.ModId;
import org.zeith.modid.custom.ZeithMob;
import org.zeith.modid.custom.ZeithMobModel;
;

public class ZeithMobRenderer extends MobRenderer<ZeithMob, ZeithMobModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModId.MOD_ID, "textures/entity/zeith_mob.png");

    public ZeithMobRenderer(EntityRendererProvider.Context context)
    {
        super(context, new ZeithMobModel(context.bakeLayer(ZeithMobModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(ZeithMob entity)
    {
        return TEXTURE;
    }

    @Override
    public void render(ZeithMob entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        this.model.renderToBuffer(matrixStack, buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)), packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }
}