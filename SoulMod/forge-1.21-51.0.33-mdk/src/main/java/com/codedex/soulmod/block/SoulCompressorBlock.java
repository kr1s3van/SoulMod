package com.codedex.soulmod.block;

import com.codedex.soulmod.blockentity.ModBlockEntities;
import com.codedex.soulmod.blockentity.SoulCompressorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class SoulCompressorBlock extends Block implements EntityBlock {
    // 1. la propriété "Allumé" (LIT)
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public SoulCompressorBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(5f)
                .requiresCorrectToolForDrops()
                .sound(SoundType.METAL));
        // Par défaut, la machine est éteinte
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
    }

    // 2. On enregistre la propriété dans le système de Minecraft
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    // 3. Liaison avec le cerveau (Block Entity)
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SoulCompressorBlockEntity(pos, state);
    }

    // 4. LE MOTEUR : Cette méthode dit à Minecraft d'appeler le "tick" du cerveau chaque seconde
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null; // On ne tick que sur le serveur

        return (lvl, pos, st, blockEntity) -> {
            if (blockEntity instanceof SoulCompressorBlockEntity be) {
                be.tick(lvl, pos, st);
            }
        };
    }

    // 5. Interaction : Clic droit pour ouvrir le menu (on le fera juste après)
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        // Logique d'ouverture du menu à venir...
        return InteractionResult.SUCCESS;
    }
}