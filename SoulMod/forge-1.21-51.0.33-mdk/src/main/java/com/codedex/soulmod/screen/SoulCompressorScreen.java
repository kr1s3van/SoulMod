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
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // 1. DESSINER LE FOND GRIS
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        // 2. DESSINER LE FUEL (Fantôme bleu)
        if (menu.isLit()) {
            int curFuelHeight = menu.getScaledLitTime();
            guiGraphics.blit(ACTIVE_TEXTURE,
                    x + 13, y + 54 + (33 - curFuelHeight),
                    13, 54 + (33 - curFuelHeight),
                    25, curFuelHeight);
        }

        // 3. DESSINER LA PROGRESSION (Flèche bleue)
        if (menu.isCrafting()) {
            int curProgressWidth = menu.getScaledProgress();
            guiGraphics.blit(ACTIVE_TEXTURE,
                    x + 58, y + 64,
                    58, 64,
                    curProgressWidth, 24);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}