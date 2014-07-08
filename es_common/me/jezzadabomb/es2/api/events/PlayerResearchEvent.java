package me.jezzadabomb.es2.api.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class PlayerResearchEvent extends PlayerEvent {

    public PlayerResearchEvent(EntityPlayer player) {
        super(player);
    }

}
