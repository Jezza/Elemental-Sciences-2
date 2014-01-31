package me.jezzadabomb.es2.common.packets;

import me.jezzadabomb.es2.common.core.handlers.HoverHandler;
import me.jezzadabomb.es2.common.core.handlers.HoverHandler.HoveringPlayer;
import me.jezzadabomb.es2.common.packets.handler.CentralPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;

public class HoverHandlerPacket extends CentralPacket {

    String username;
    int time;
    boolean hovering;
    boolean waiting;

    public HoverHandlerPacket(HoveringPlayer hoveringPlayer) {
        username = hoveringPlayer.getUsername();
        time = hoveringPlayer.tickCount();
        hovering = hoveringPlayer.isHovering();
        waiting = hoveringPlayer.isWaiting();
    }

    public HoverHandlerPacket() {
    }

    @Override
    public void write(ByteArrayDataOutput out) {
        out.writeUTF(username);
        out.writeInt(time);
        out.writeBoolean(hovering);
        out.writeBoolean(waiting);

    }

    @Override
    public void read(ByteArrayDataInput in) throws ProtocolException {
        username = in.readUTF();
        time = in.readInt();
        hovering = in.readBoolean();
        waiting = in.readBoolean();

    }

    @Override
    public void execute(EntityPlayer player, Side side) throws ProtocolException {
        if (side.isClient()) {
            World world = player.worldObj;
            EntityPlayer target = world.getPlayerEntityByName(username);
            HoverHandler.getInstance().updatePlayer(target, time, hovering, waiting);

        } else {
            throw new ProtocolException("Cannot send packet to the server!");
        }
    }

}
