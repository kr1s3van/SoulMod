package com.codedex.soulmod.entity;

import com.codedex.soulmod.SoulMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    // Création du registre des entités
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SoulMod.MOD_ID);

    // Déclaration du Mourner
    public static final RegistryObject<EntityType<MournerEntity>> MOURNER =
            ENTITY_TYPES.register("mourner",
                    () -> EntityType.Builder.of(MournerEntity::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.2f)
                            .build("mourner"));

    // Liaison mod
    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}