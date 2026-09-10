package com.codedex.soulmod.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class MournerFireball extends ShulkerBullet {

    public MournerFireball(EntityType<? extends ShulkerBullet> type, Level level) {
        super(type, level);
    }

    // Constructeur pour faire apparaître la boule de feu via le code du mob
    public MournerFireball(Level level, LivingEntity shooter, net.minecraft.world.entity.Entity target, net.minecraft.core.Direction.Axis axis) {
        super(level, shooter, target, axis);
    }

    //  Définition des degats et effets de la charge de shulker modifié (j ai enlevé le super pour ne pas recuperer ses propriétés)
    @Override
    protected void onHitEntity(EntityHitResult result) {
        net.minecraft.world.entity.Entity target = result.getEntity();
        net.minecraft.world.entity.Entity owner = this.getOwner();

        // On applique les dégâts (4 points = 2 cœurs)
        DamageSource source = this.damageSources().indirectMagic(this, owner != null ? owner : this);
        if (target.hurt(source, 4.0F) && target instanceof LivingEntity livingTarget) {
            livingTarget.igniteForSeconds(5.0F);
        }
    }

    // visuel de la charge
    @Override
    public void tick() {
        super.tick();

        // ici, on fait apparaître des particules de Soul Fire pendant que le projectile vole
        if (this.level().isClientSide) {
            for(int i = 0; i < 2; i++) {
                this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME,
                        this.getX(), this.getY() + 0.2, this.getZ(),
                        0, 0, 0);
            }
        }
    }
}