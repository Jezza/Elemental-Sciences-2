package me.jezzadabomb.es2.common.packets;

import java.io.IOException;
import java.util.ArrayList;

import me.jezzadabomb.es2.client.tickers.HUDHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;

public class InventoryPacket extends CentralPacket {

    public ArrayList<ItemStack> itemStacks;
    public String loc;
    public String inventoryTitle;
    public int x;
    public int y;
    public int z;

    public InventoryPacket(TileEntity tileEntity, String loc) {
        if (tileEntity instanceof IInventory) {
            itemStacks = new ArrayList<ItemStack>();
            this.loc = loc;
            IInventory inventory = ((IInventory) tileEntity);
            inventoryTitle = inventory.getInvName();
            for(int i = 0; i < inventory.getSizeInventory(); i++){
                if(inventory.getStackInSlot(i) != null){
                    itemStacks.add(inventory.getStackInSlot(i));
                }
            }
        }
    }

    public InventoryPacket() {
    }

    @Override
    public void write(ByteArrayDataOutput out) {
        out.writeUTF(inventoryTitle);
        out.writeUTF(loc);
        out.writeShort(itemStacks.size());
        for(ItemStack i : itemStacks){
            try {
                writeItemStack(i,out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void read(ByteArrayDataInput in) throws ProtocolException {
        inventoryTitle = in.readUTF();
        loc = in.readUTF();
        int length = in.readShort();
        itemStacks = new ArrayList<ItemStack>(length);
        for(int i = 0; i < length; i++){
            try {
                itemStacks.add(readItemStack(in));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }

    @Override
    public void execute(EntityPlayer player, Side side) throws ProtocolException {
        if (side.isClient()) {
            Integer[] coord = getXYZFromString(loc);
            x = coord[0];
            y = coord[1];
            z = coord[2];
            HUDHandler.addHUDHandler(this);
        } else {
            throw new ProtocolException("Cannot send this packet to the server!");
        }
    }

}
