package com.codedex.soulmod.block;

import com.codedex.soulmod.blockentity.SoulCompressorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.server.level.ServerPlayer;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SoulCompressorBlock extends Block implements EntityBlock {

    // 1. la propriété "Allumé" (LIT)
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public SoulCompressorBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(5f)
                .requiresCorrectToolForDrops()
                .sound(SoundType.METAL)
                .lightLevel(state -> state.getValue(LIT) ? 15 : 0));

        // défaut: la machine est éteinte
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
        if (level.isClientSide()) return null;

        return (lvl, pos, st, blockEntity) -> {
            if (blockEntity instanceof SoulCompressorBlockEntity be) {
                be.tick(lvl, pos, st);
            }
        };
    }

    // 5. Interaction : Clic droit pour ouvrir le menu
    @Override
    @NotNull
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof SoulCompressorBlockEntity) {
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.openMenu((MenuProvider) entity, pos);
                }
            } else {
                throw new IllegalStateException("Notre fournisseur de menu est manquant !");
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, net.minecraft.util.RandomSource random) {
        if (!state.getValue(LIT)) {
            return;
        }

        // Coordonnées du centre du bloc
        double x = (double)pos.getX() + 0.5D;
        double y = (double)pos.getY() + 1.1D;
        double z = (double)pos.getZ() + 0.5D;

        // 1. Petit bruitage de fantome
        if (random.nextDouble() < 0.1D) {
            level.playLocalSound(x, y, z, net.minecraft.sounds.SoundEvents.SOUL_ESCAPE.value(),
                    net.minecraft.sounds.SoundSource.BLOCKS, 0.5F, 1.0F, false);
        }

        // 2. Apparition des particules "SOUL" (les flammes bleues)
        // On en fait apparaître entre 1 et 3.
        for (int i = 0; i < random.nextInt(3) + 1; i++) {
            level.addParticle(net.minecraft.core.particles.ParticleTypes.SOUL,
                    x + (random.nextDouble() - 0.5D) * 0.4D, // Petit décalage aléatoire X
                    y,
                    z + (random.nextDouble() - 0.5D) * 0.4D, // Petit décalage aléatoire Z
                    0.0D, 0.05D, 0.0D); // Elles montent lentement vers le haut
        }
    }
}