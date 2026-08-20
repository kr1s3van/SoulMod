package com.codedex.soulmod.item;
import com.codedex.soulmod.SoulMod;
import com.codedex.soulmod.entity.ModEntities;
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

    // Gloomy Rune
    public static final RegistryObject<Item> GLOOMY_RUNE = ITEMS.register("gloomy_rune",
            () -> new Item(new Item.Properties()));

    // Mourner spawn egg
    public static final RegistryObject<Item> MOURNER_SPAWN_EGG = ITEMS.register("mourner_spawn_egg",
            () -> new net.minecraftforge.common.ForgeSpawnEggItem(ModEntities.MOURNER,
                    0x1e385a, 0x3de0f7, new Item.Properties()));

    // La méthode pour enregistrer tout ça au démarrage du jeu
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}


