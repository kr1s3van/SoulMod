package com.codedex.soulmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class MournerEntity extends Monster {
    // créatrion du mob
    public MournerEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    // LE CERVEAU
    @Override
    protected void registerGoals() {
        // Priorité 0 : Ne pas couler si on est dans l'eau
        this.goalSelector.addGoal(0, new FloatGoal(this));

        // Priorité 1 : S'enfuit si un joueur approche à moins de 6 blocs (Il est peureux !)
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Player.class, 6.0F, 1.0D, 1.2D));

        // Priorité 2 : Se balader au hasard
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));

        // Priorité 3 : Regarder le joueur quand il est proche
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));

        // Priorité 4 : Regarder autour de soi
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    // mob stats
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)   // 10 coeurs de vie
                .add(Attributes.MOVEMENT_SPEED, 0.2D) // Vitesse de déplacement
                .add(Attributes.ATTACK_DAMAGE, 4.0D); // 2 coeurs de dégâts
    }
}