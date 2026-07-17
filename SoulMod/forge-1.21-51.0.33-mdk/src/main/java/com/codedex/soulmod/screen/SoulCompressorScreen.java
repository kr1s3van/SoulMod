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
        this.imageHeight = 256;
        this.inventoryLabelY = this.imageHeight - 114;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // 1. DESSINER LE FOND GRIS
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        // 2. DESSINER LE FUEL
        if (menu.isLit()) {
            int curFuelHeight = menu.getScaledLitTime(); // Hauteur de 0 à 33 pixels

            // On dessine l'âme bleue par-dessus la grise
            // La logique (33 - curFuelHeight) permet de remplir du bas vers le haut
            guiGraphics.blit(ACTIVE_TEXTURE,
                    x + 13, y + 54 + (33 - curFuelHeight), // Position sur l'écran
                    13, 54 + (33 - curFuelHeight),         // Position sur le fichier PNG
                    25, curFuelHeight);                    // Taille à dessiner
        }

        // 3. DESSINER LA PROGRESSION
        if (menu.isCrafting()) {
            int curProgressWidth = menu.getScaledProgress(); // Largeur de 0 à 41 pixels

            // On dessine la flèche bleue par-dessus la grise
            guiGraphics.blit(ACTIVE_TEXTURE,
                    x + 58, y + 64,      // Position sur l'écran
                    58, 64,              // Position sur le fichier PNG
                    curProgressWidth, 24); // On ne dessine que la largeur actuelle (p)
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY); // Affiche le nom des items au survol
    }
}