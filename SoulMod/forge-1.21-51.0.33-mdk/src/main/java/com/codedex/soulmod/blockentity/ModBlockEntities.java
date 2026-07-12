package com.codedex.soulmod.blockentity;

import com.codedex.soulmod.SoulMod;
import com.codedex.soulmod.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SoulMod.MOD_ID);

    // On lie notre classe "Cerveau" (SoulCompressorBlockEntity) à notre "Bloc" (SOUL_COMPRESSOR)
    public static final RegistryObject<BlockEntityType<SoulCompressorBlockEntity>> SOUL_COMPRESSOR_BE =
            BLOCK_ENTITIES.register("soul_compressor_be", () ->
                    BlockEntityType.Builder.of(SoulCompressorBlockEntity::new,
                            ModBlocks.SOUL_COMPRESSOR.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}