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

        // 1. On cherche si le joueur a de la Soul Dust (ou s'il est en Créatif)
        boolean hasAmmo = player.getAbilities().instabuild || player.getInventory().contains(new ItemStack(ModItems.SOUL_DUST.get()));

        if (hasAmmo) {
            if (!level.isClientSide) {
                // 2. Logique de tir (le code qu'on a déjà fait)
                Vec3 lookDirection = player.getLookAngle();
                SmallFireball fireball = new SmallFireball(level, player, lookDirection);
                fireball.setPos(player.getX(), player.getEyeY(), player.getZ());
                level.addFreshEntity(fireball);

                // 3. ON CONSOMME LA POUSSIÈRE (uniquement en survie)
                if (!player.getAbilities().instabuild) {
                    // On cherche l'item dans l'inventaire et on en retire 1
                    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                        ItemStack invStack = player.getInventory().getItem(i);
                        if (invStack.is(ModItems.SOUL_DUST.get())) {
                            invStack.shrink(1); // Retire 1 de la pile
                            break; // On arrête de chercher une fois qu'on a trouvé
                        }
                    }
                }

                // 4. Temps de recharge
                player.getCooldowns().addCooldown(this, 10);
            }

            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        } else {
            // 5. Si pas de poussière, on ne fait rien (ou on pourrait jouer un petit son de "clic" vide)
            return InteractionResultHolder.fail(itemstack);
        }
    }
}
