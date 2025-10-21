package net.chrupki.persage.events;

import net.chrupki.persage.Persage;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "persage", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {

    @SubscribeEvent
    public static void onLeftClick(PlayerInteractEvent.RightClickEmpty event) {
        Player player = event.getEntity();

        if (player.isShiftKeyDown()) {
            player.displayClientMessage(
                    net.minecraft.network.chat.Component.literal("Shift + Left Click detected!"),
                    true
            );
        }
    }
}
