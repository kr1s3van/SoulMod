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

    // Force le jeu à reconnaître NOTRE projectile (indique au jeu de charger le type mournerfireball quand le mourner invoque son projectile au lieu du shulker type) ---
    @Override
    public EntityType<?> getType() {
        return ModEntities.MOURNER_FIREBALL.get();
    }

    // Définition des degats, de l'explosion de l'impact et effets de la charge de shulker modifié (j ai enlevé le super pour ne pas recuperer ses propriétés)
    @Override
    protected void onHitEntity(EntityHitResult result) {
        net.minecraft.world.entity.Entity target = result.getEntity();
        net.minecraft.world.entity.Entity owner = this.getOwner();

        DamageSource source = this.damageSources().indirectMagic(this, owner != null ? owner : this);
        if (target.hurt(source, 4.0F) && target instanceof LivingEntity livingTarget) {
            livingTarget.igniteForSeconds(4.0F); // Enflamme pendant 4s
        }

        // Effet d'impact: gerbe de flammes bleues quand ça touche
        if (this.level().isClientSide) {
            for (int i = 0; i < 12; i++) {
                this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME,
                        this.getX(), this.getY(), this.getZ(),
                        (this.random.nextDouble() - 0.5) * 0.3,
                        (this.random.nextDouble() - 0.5) * 0.3,
                        (this.random.nextDouble() - 0.5) * 0.3);
            }
        }
    }

    // 2. effet visuel en vol (La Comète Bleue)
    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            // A. Projectile core (amas compact de flammes bleues)
            for (int i = 0; i < 6; i++) {
                double offsetX = (this.random.nextDouble() - 0.5) * 0.25;
                double offsetY = (this.random.nextDouble() - 0.5) * 0.25;
                double offsetZ = (this.random.nextDouble() - 0.5) * 0.25;
                this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME,
                        this.getX() + offsetX, this.getY() + 0.15 + offsetY, this.getZ() + offsetZ,
                        0, 0, 0);
            }

            // B. La Traînée durable d'âmes qui flottent derrière
            this.level().addParticle(ParticleTypes.SOUL,
                    this.getX(), this.getY() + 0.15, this.getZ(),
                    0, 0.02, 0);
        }
    }
}