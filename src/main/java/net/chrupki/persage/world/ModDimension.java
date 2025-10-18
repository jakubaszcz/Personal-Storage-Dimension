package net.chrupki.persage.world;

import net.chrupki.persage.Persage;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class ModDimension {
        public static final ResourceKey<Level> PERSONAL_DIMENSION = ResourceKey.create(
                Registries.DIMENSION,
                ResourceLocation.fromNamespaceAndPath(Persage.MOD_ID, "personal_dimension")
        );
}
