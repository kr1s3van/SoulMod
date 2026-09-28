package com.codedex.soulmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.Nullable;

public class MournerEntity extends Monster implements RangedAttackMob {
    // On crée la "Clé" pour le haut-parleur
    private static final EntityDataAccessor<Boolean> ANGRY =
            SynchedEntityData.defineId(MournerEntity.class, EntityDataSerializers.BOOLEAN);

    public MournerEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    // On initialise le haut-parleur au démarrage du mob
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ANGRY, false); // Par défaut, pas fâché
    }

    // On force la mise à jour quand la cible change
    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        // On met à jour le haut-parleur : si target n'est pas null, on est fâché
        this.entityData.set(ANGRY, target != null);
    }

    // 4. On modifie isAngry pour qu'il écoute le haut-parleur
    public boolean isAngry() {
        return this.entityData.get(ANGRY);
    }

    // LE CERVEAU
    @Override
    protected void registerGoals() {
        // Priorité 0 : Ne pas couler si on est dans l'eau
        this.goalSelector.addGoal(0, new FloatGoal(this));

        // Priorité 1 : Cibler la personne qui attaque et se battre au corps à corps
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, false));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));

        // Priorité 2 : S'enfuit si un joueur approche à moins de 6 blocs (Il est peureux !)
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 6.0F, 1.0D, 1.2D));

        // Priorité 3 : Se balader au hasard
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));

        // Priorité 4 : Regarder le joueur quand il est proche
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));

        // Priorité 5 : Regarder autour de soi
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    // L'attaque a distance
    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        if (!this.level().isClientSide) {
            // Flamme Gauche (Part vers l'axe X)
            MournerFireball fireballLeft = new MournerFireball(this.level(), this, target, Direction.Axis.X);
            fireballLeft.setPos(this.getX() - 0.8D, this.getY() + 0.5D, this.getZ());
            this.level().addFreshEntity(fireballLeft);

            // Flamme Droite (Part vers l'axe Z)
            MournerFireball fireballRight = new MournerFireball(this.level(), this, target, Direction.Axis.Z);
            fireballRight.setPos(this.getX() + 0.8D, this.getY() + 0.5D, this.getZ());
            this.level().addFreshEntity(fireballRight);

            // Flamme Haute (Part vers le ciel, axe Y)
            MournerFireball fireballTop = new MournerFireball(this.level(), this, target, Direction.Axis.Y);
            fireballTop.setPos(this.getX(), this.getY() + 1.2D, this.getZ());
            this.level().addFreshEntity(fireballTop);

            // Son de tir
            this.playSound(SoundEvents.BLAZE_SHOOT, 1.0F, 1.5F);
        }
    }

    // mob stats
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }
}