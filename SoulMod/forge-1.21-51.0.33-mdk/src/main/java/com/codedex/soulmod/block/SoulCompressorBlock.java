package com.codedex.soulmod.block; // Vérifie bien que le 'pa' de package est là

import com.codedex.soulmod.blockentity.SoulCompressorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SoulCompressorBlock extends Block implements EntityBlock {
    public SoulCompressorBlock() {
        // propriétés physiques du bloc
        super(BlockBehaviour.Properties.of()
                .strength(5.0f)
                .requiresCorrectToolForDrops()
                .sound(SoundType.METAL));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SoulCompressorBlockEntity(pos, state);
    }
}