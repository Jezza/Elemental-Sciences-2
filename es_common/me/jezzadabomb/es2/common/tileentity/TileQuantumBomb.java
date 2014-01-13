package me.jezzadabomb.es2.common.tileentity;

import me.jezzadabomb.es2.common.core.ESLogger;
import net.minecraft.entity.player.EntityPlayer;

public class TileQuantumBomb extends TileES{

	private boolean added = false;
	
	public TileQuantumBomb(EntityPlayer player){
		if(!added){
			added = true;
			ESLogger.info(xCoord);
			ESLogger.info(yCoord);
			ESLogger.info(zCoord);
			xCoord = (int) Math.floor(player.posX);
			yCoord = (int) Math.floor(player.posY);
			zCoord = (int) Math.floor(player.posZ);
			ESLogger.info(xCoord);
			ESLogger.info(yCoord);
			ESLogger.info(zCoord);
		}
	}
	
	@Override
	public void updateEntity() {
		
	}	
}
