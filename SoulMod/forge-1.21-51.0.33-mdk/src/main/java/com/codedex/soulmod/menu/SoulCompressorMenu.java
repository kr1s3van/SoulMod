package com.codedex.soulmod.menu;

import com.codedex.soulmod.block.ModBlocks;
import com.codedex.soulmod.blockentity.SoulCompressorBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class SoulCompressorMenu extends AbstractContainerMenu {
    private final SoulCompressorBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public SoulCompressorMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    public SoulCompressorMenu(int containerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.SOUL_COMPRESSOR_MENU.get(), containerId);
        checkContainerSize(inv, 3);
        this.blockEntity = (SoulCompressorBlockEntity) entity;
        this.level = inv.player.level();
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            // Tes coordonnées "Pixel-Perfect" avec les points blancs
            this.addSlot(new SlotItemHandler(handler, 0, 18, 19));  // Slot Sable (Haut)
            this.addSlot(new SlotItemHandler(handler, 1, 18, 106)); // Slot Fuel (Bas)
            this.addSlot(new SlotItemHandler(handler, 2, 127, 62)); // Slot Sortie (Droite)
        });

        addDataSlots(data);
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public boolean isLit() {
        return data.get(2) > 0;
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int arrowPixelSize = 41; // La largeur de ta flèche
        return maxProgress != 0 && progress != 0 ? progress * arrowPixelSize / maxProgress : 0;
    }

    public int getScaledLitTime() {
        int litTime = this.data.get(2);
        int maxLitTime = this.data.get(3);
        int ghostPixelHeight = 33; // La hauteur de ton âme/fantôme
        return maxLitTime != 0 && litTime != 0 ? (litTime * ghostPixelHeight / maxLitTime) : 0;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyStack = sourceStack.copy();

        if (index < 36) {
            if (!moveItemStackTo(sourceStack, 36, 39, false)) return ItemStack.EMPTY;
        } else if (index < 39) {
            if (!moveItemStackTo(sourceStack, 0, 36, false)) return ItemStack.EMPTY;
        } else {
            return ItemStack.EMPTY;
        }

        if (sourceStack.getCount() == 0) sourceSlot.set(ItemStack.EMPTY);
        else sourceSlot.setChanged();
        sourceSlot.onTake(playerIn, sourceStack);
        return copyStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, ModBlocks.SOUL_COMPRESSOR.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}