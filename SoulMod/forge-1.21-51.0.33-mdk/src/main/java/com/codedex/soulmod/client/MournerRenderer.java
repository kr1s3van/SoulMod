package com.codedex.soulmod.client;

import com.codedex.soulmod.SoulMod;
import com.codedex.soulmod.entity.MournerEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

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

    @Nullable
    @Override // j ai modif cette fonction de base de forge pour quelle puisse lire la valeur alpha (l opacité) de chaque pixel
    protected RenderType getRenderType(MournerEntity animatable, boolean p_115323_, boolean p_115324_, boolean p_115325_) {
        return RenderType.entityTranslucent(TEXTURE);
    }
}