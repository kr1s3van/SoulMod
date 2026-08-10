package com.codedex.soulmod.screen;

import com.codedex.soulmod.SoulMod;
import com.codedex.soulmod.menu.SoulCompressorMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SoulCompressorScreen extends AbstractContainerScreen<SoulCompressorMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SoulMod.MOD_ID, "textures/gui/container/soul_compressor.png");
    private static final ResourceLocation ACTIVE_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SoulMod.MOD_ID, "textures/gui/container/soul_compressor_active.png");

    public SoulCompressorScreen(SoulCompressorMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;

        // --- POSITION DES TEXTES ---
        // Le titre "Soul Compressor" monte un peu
        this.titleLabelY = 4;

        // Le mot "Inventory" descend juste au-dessus des cases (84 - 11 = 73)
        this.inventoryLabelY = 73;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 176, 166);

        if (menu.isLit()) {
            int h = menu.getScaledLitTime();
            guiGraphics.blit(ACTIVE_TEXTURE, x + 27, y + 37 + (12 - h),
                    27, 37 + (12 - h)
                    , 7, h, 176, 166);
        }

        if (menu.isCrafting()) {
            int w = menu.getScaledProgress();
            guiGraphics.blit(ACTIVE_TEXTURE, x + 73, y + 38, 73, 38, w, 12, 176, 166);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}