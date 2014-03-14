package me.jezzadabomb.es2.common.gui;

import java.util.ArrayList;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.CoordSetD;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class GuiConsoleTracker {

    ArrayList<PlayerState> playerList;

    public GuiConsoleTracker() {
        playerList = new ArrayList<PlayerState>();
    }

    public void openGui(World world, EntityPlayer player) {
        ESLogger.info(playerList);
        PlayerState playerState = new PlayerState(player);

        if (!playerList.contains(playerState))
            playerList.add(playerState);
        ESLogger.info(playerList);
    }

    public void closeGui(World world, EntityPlayer player) {
        ESLogger.info(playerList);
        playerList.remove(new PlayerState(player));
        ESLogger.info(playerList);
    }

    public ArrayList<PlayerState> getPlayerList() {
        return playerList;
    }

    public static CoordSetD getPosForGUI(EntityPlayer player) {
        Vec3 posVec = Vec3.createVectorHelper(player.posX, player.posY, player.posZ);
        Vec3 lookVec = player.getLook(1);
        posVec.yCoord += player.getEyeHeight();

        float distance = 0.1F;
        lookVec = posVec.addVector(lookVec.xCoord * distance, lookVec.yCoord * distance, lookVec.zCoord * distance);

        return new CoordSetD(lookVec);
    }

    public static class PlayerState {
        public String name;

        public CoordSetD playerSet;

        public PlayerState(EntityPlayer player) {
            this.name = player.getDisplayName();
            playerSet = getPosForGUI(player);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof PlayerState))
                return false;
            return ((PlayerState) obj).name.equals(name);
        }
        
        @Override
        public String toString() {
            return name + playerSet;
        }
    }

}
