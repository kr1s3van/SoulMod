package com.codedex.soulmod.blockentity;

import com.codedex.soulmod.block.SoulCompressorBlock;
import com.codedex.soulmod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SoulCompressorBlockEntity extends BlockEntity {
    // Inventaire : 0 = Sable, 1 = Larme (Fuel), 2 = Sortie
    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    // Variables de progression (La flèche)
    private int progress = 0;
    private int maxProgress = 200;

    // Variables de Fuel (Le fantôme)
    private int litTime = 0;    // Temps de fuel restant
    private int maxLitTime = 0; // Temps total donné par la dernière larme

    protected final ContainerData data;

    public SoulCompressorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SOUL_COMPRESSOR_BE.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> SoulCompressorBlockEntity.this.progress;
                    case 1 -> SoulCompressorBlockEntity.this.maxProgress;
                    case 2 -> SoulCompressorBlockEntity.this.litTime;
                    case 3 -> SoulCompressorBlockEntity.this.maxLitTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> SoulCompressorBlockEntity.this.progress = value;
                    case 1 -> SoulCompressorBlockEntity.this.maxProgress = value;
                    case 2 -> SoulCompressorBlockEntity.this.litTime = value;
                    case 3 -> SoulCompressorBlockEntity.this.maxLitTime = value;
                }
            }

            @Override
            public int getCount() {
                return 4; // On synchronise 4 valeurs maintenant
            }
        };
    }

    private boolean isBurning() {
        return this.litTime > 0;
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (pLevel.isClientSide()) return;

        boolean dirty = false;

        // 1. Si la machine brûle, on consomme le fuel
        if (this.isBurning()) {
            this.litTime--;
            dirty = true;
        }

        ItemStack fuelStack = this.itemHandler.getStackInSlot(1);

        // 2. Logique principale
        if (this.isBurning() || !fuelStack.isEmpty() && hasSoulInput()) {

            // Si on ne brûle pas encore mais qu'on a une recette -> On consomme une larme !
            if (!this.isBurning() && hasRecipe()) {
                this.litTime = 1600; // Une larme dure 80 secondes (assez pour 8 crafts)
                this.maxLitTime = this.litTime;
                fuelStack.shrink(1); // On consomme la larme
                dirty = true;
            }

            // Si on brûle et qu'on a la recette -> On avance la progression
            if (this.isBurning() && hasRecipe()) {
                this.progress++;
                if (this.progress >= this.maxProgress) {
                    craftItem();
                    this.progress = 0;
                }
                dirty = true;
            } else {
                this.progress = 0;
            }
        }

        // 3. Mise à jour de l'apparence du bloc (LIT)
        if (pState.getValue(SoulCompressorBlock.LIT) != this.isBurning()) {
            pLevel.setBlock(pPos, pState.setValue(SoulCompressorBlock.LIT, this.isBurning()), 3);
            dirty = true;
        }

        if (dirty) {
            setChanged(pLevel, pPos, pState);
        }
    }

    private boolean hasSoulInput() {
        ItemStack input = itemHandler.getStackInSlot(0);
        return (input.is(Items.SOUL_SAND) || input.is(Items.SOUL_SOIL)) && input.getCount() >= 5;
    }

    private boolean hasRecipe() {
        boolean hasInput = hasSoulInput();
        ItemStack outputSlot = itemHandler.getStackInSlot(2);
        boolean canOutput = outputSlot.isEmpty() ||
                (outputSlot.is(ModItems.SOUL_DUST.get()) && outputSlot.getCount() < outputSlot.getMaxStackSize());

        return hasInput && canOutput;
    }

    private void craftItem() {
        // Consomme 5 sables
        this.itemHandler.extractItem(0, 5, false);
        // Produit 1 poussière
        this.itemHandler.setStackInSlot(2, new ItemStack(ModItems.SOUL_DUST.get(),
                this.itemHandler.getStackInSlot(2).getCount() + 1));
    }

    // --- SAUVEGARDE ---
    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        nbt.put("inventory", itemHandler.serializeNBT(registries));
        nbt.putInt("progress", progress);
        nbt.putInt("litTime", litTime);
        nbt.putInt("maxLitTime", maxLitTime);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        if (nbt.contains("inventory")) {
            itemHandler.deserializeNBT(registries, nbt.getCompound("inventory"));
        }
        progress = nbt.getInt("progress");
        litTime = nbt.getInt("litTime");
        maxLitTime = nbt.getInt("maxLitTime");
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) return lazyItemHandler.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    public ContainerData getContainerData() {
        return this.data;
    }
}