package com.codedex.soulmod.client;

import com.codedex.soulmod.SoulMod;
import com.codedex.soulmod.entity.MournerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MournerRenderer extends MobRenderer<MournerEntity, MournerModel<MournerEntity>> {

    // Le lien vers la texture PNG (Skin)
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SoulMod.MOD_ID, "textures/entity/mourner.png");

    // skin sur le model 3D
    public MournerRenderer(EntityRendererProvider.Context context) {
        super(context, new MournerModel<>(context.bakeLayer(MournerModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(MournerEntity entity) {
        return TEXTURE;
    }
}