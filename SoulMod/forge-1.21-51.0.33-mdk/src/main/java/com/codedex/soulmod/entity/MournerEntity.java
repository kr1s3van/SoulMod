package com.codedex.soulmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class MournerEntity extends Monster {
    // créatrion du mob
    public MournerEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    // mob stats
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)   // 10 coeurs de vie
                .add(Attributes.MOVEMENT_SPEED, 0.2D) // Vitesse de déplacement
                .add(Attributes.ATTACK_DAMAGE, 4.0D); // 2 coeurs de dégâts
    }
}