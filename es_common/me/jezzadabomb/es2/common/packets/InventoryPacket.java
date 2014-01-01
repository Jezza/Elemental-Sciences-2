package me.jezzadabomb.es2.common.packets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import me.jezzadabomb.es2.client.ClientProxy;
import me.jezzadabomb.es2.client.renderers.HUDRenderer;
import me.jezzadabomb.es2.client.utils.CoordSet;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.hud.InventoryInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;

public class InventoryPacket extends CentralPacket {

	public ArrayList<ItemStack> itemStacks;
	public String loc = "null";
	public String inventoryTitle = null;
	private int x;
	private int y;
	private int z;
	public CoordSet coordSet;
	public int tickTiming;

	public InventoryPacket(TileEntity tileEntity, String loc) {
		if (tileEntity != null && tileEntity instanceof IInventory) {
			itemStacks = new ArrayList<ItemStack>(0);
			this.loc = loc;
			IInventory inventory = ((IInventory) tileEntity);
			inventoryTitle = inventory.getInvName();
			for (int i = 0; i < inventory.getSizeInventory(); i++) {
				if (inventory.getStackInSlot(i) != null) {
					itemStacks.add(inventory.getStackInSlot(i));
				}
			}
		}
	}

	public InventoryPacket() {
	}

	@Override
	public void write(ByteArrayDataOutput out) {
		if (inventoryTitle == null)
			inventoryTitle = "null";
		out.writeUTF(inventoryTitle);
		out.writeUTF(loc);
		out.writeShort(itemStacks != null ? (itemStacks.isEmpty() ? (short) 0 : (short) itemStacks.size()) : (short) 0);
		if (itemStacks != null) {
			for (ItemStack i : itemStacks) {
				try {
					writeItemStack(i, out);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public void read(ByteArrayDataInput in) throws ProtocolException {
		inventoryTitle = in.readUTF();
		loc = in.readUTF();
		int length = in.readShort();
		itemStacks = new ArrayList<ItemStack>(length);
		for (int i = 0; i < length; i++) {
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
			int[] coord = UtilMethods.getArrayFromString(loc);
			if (coord == null)
				return;
			coordSet = new CoordSet(coord[0], coord[1], coord[2]);
			ClientProxy.hudRenderer.addPacketToList(this);
			tickTiming = 0;
		} else {
			throw new ProtocolException("Cannot send this packet to the server!");
		}
	}

	public String getItemStacksInfo() {
		StringBuilder temp = new StringBuilder();
		for (ItemStack tempStack : getItemStacks()) {
			temp.append(tempStack.getUnlocalizedName() + ":" + tempStack.stackSize + ",");
		}
		return temp.toString();
	}

	public ArrayList<ItemStack> getItemStacks() {
		ArrayList<ItemStack> tempStacks = new ArrayList<ItemStack>();
		boolean added = false;
		for (ItemStack itemStack : itemStacks) {
			for (ItemStack tempStack : tempStacks) {
				if (itemStack.itemID == tempStack.itemID) {
					UtilMethods.mergeItemStacks(tempStacks.get(tempStacks.indexOf(tempStack)), itemStack, true);
					added = true;
				}
			}
			if (!added) {
				tempStacks.add(itemStack);
			}
			added = false;
		}
		return tempStacks;
	}

	@Override
	public String toString() {
		return inventoryTitle + "@" + loc + " " + getItemStacksInfo();

	}

	@Override
	public boolean equals(Object other) {
		return equals(other, false);
	}

	public boolean equals(Object other, boolean includeItemStacks) {
		if (other == null)
			return false;
		if (!(other instanceof InventoryPacket))
			return false;

		InventoryPacket tempPacket = (InventoryPacket) other;
		if (!includeItemStacks) {
			return inventoryTitle.equals(tempPacket.inventoryTitle) && coordSet.equals(tempPacket.coordSet);
		}
		return inventoryTitle.equals(tempPacket.inventoryTitle) && coordSet.equals(tempPacket.coordSet) && this.itemStacks.equals(itemStacks);
	}

	public boolean isCloserThan(InventoryPacket tempP, EntityPlayer player) {
		return player.getDistance(x, y, z) < player.getDistance(tempP.x, tempP.y, tempP.z);
	}
}
