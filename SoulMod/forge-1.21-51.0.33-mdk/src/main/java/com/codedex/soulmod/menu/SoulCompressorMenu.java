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

    // Constructeur pour le CLIENT (Forge l'utilise pour ouvrir le menu côté joueur)
    public SoulCompressorMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new ContainerData() {
            @Override public int get(int i) { return 0; }
            @Override public void set(int i, int i1) {}
            @Override public int getCount() { return 0; }
        });
    }

    // Constructeur pour le SERVEUR (Le vrai coeur du menu)
    public SoulCompressorMenu(int containerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.SOUL_COMPRESSOR_MENU.get(), containerId);
        checkContainerSize(inv, 2); // On vérifie qu'on a bien nos 2 slots
        this.blockEntity = (SoulCompressorBlockEntity) entity;
        this.level = inv.player.level();

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        // Ajout des slots de la machine
        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, 0, 12, 15)); // Slot d'entrée
            this.addSlot(new SlotItemHandler(handler, 1, 86, 15)); // Slot de sortie
        });
    }

    // --- LOGIQUE DU SHIFT-CLICK (Quick Move) ---
    // Obligatoire pour ne pas faire crash le jeu quand on shift-clic
    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyStack = sourceStack.copy();

        if (index < 36) { // Si on clique dans l'inventaire joueur
            if (!moveItemStackTo(sourceStack, 36, 38, false)) return ItemStack.EMPTY;
        } else if (index < 38) { // Si on clique dans la machine
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