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

    // Déclaration du Projectile
    public static final RegistryObject<EntityType<MournerFireball>> MOURNER_FIREBALL =
            ENTITY_TYPES.register("mourner_fireball",
                    () -> EntityType.Builder.<MournerFireball>of(MournerFireball::new, MobCategory.MISC)
                            .sized(0.3125f, 0.3125f) // Taille de la hitbox d'une shulker bullet
                            .clientTrackingRange(8)   // Distance à laquelle le joueur peut la voir
                            .updateInterval(1)        // Mise à jour ultra fluide (chaque tick)
                            .build("mourner_fireball"));

    // Liaison mod
    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}