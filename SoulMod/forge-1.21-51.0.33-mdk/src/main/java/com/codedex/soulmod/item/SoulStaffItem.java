package com.codedex.soulmod.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

public class SoulStaffItem extends Item {
    public SoulStaffItem(Properties properties) {
        super(properties);
    }

    // Cette méthode se déclenche quand on fait un clic droit
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        // On ne lance le projectile que sur le Serveur (logique de jeu)
        if (!level.isClientSide) {
            Vec3 lookDirection = player.getLookAngle();

            // En 1.21, constructeur prend (Level, Shooter, MovementVector)
            SmallFireball fireball = new SmallFireball(level, player, lookDirection);

            // On ajuste la position pour que ça sorte des yeux du joueur
            fireball.setPos(player.getX(), player.getEyeY(), player.getZ());

            level.addFreshEntity(fireball);

            // Cooldown de 1 seconde
            player.getCooldowns().addCooldown(this, 20);

        }

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}
