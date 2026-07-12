package com.codedex.soulmod.item;
import com.codedex.soulmod.SoulMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    // On crée le registre d'items.
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SoulMod.MOD_ID);

    // ITEMS

    // Soul Dust
    public static final RegistryObject<Item> SOUL_DUST = ITEMS.register("soul_dust",
            () -> new Item(new Item.Properties()));

    // Soul Staff
    public static final RegistryObject<Item> SOUL_STAFF = ITEMS.register("soul_staff",
            () -> new SoulStaffItem(new Item.Properties().stacksTo(1).durability(100)));


    // La méthode pour enregistrer tout ça au démarrage du jeu
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}


