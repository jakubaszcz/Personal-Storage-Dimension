package net.chrupki.persage.items;

import net.chrupki.persage.Persage;
import net.chrupki.persage.world.ModDimension;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Persage.MOD_ID);

    public static final RegistryObject<Item> TEST = ModItems.ITEMS.register("test",
            () -> new ModDimension(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
