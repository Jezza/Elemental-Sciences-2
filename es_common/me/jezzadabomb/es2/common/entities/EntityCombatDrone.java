package me.jezzadabomb.es2.common.entities;

import java.util.Random;

import io.netty.buffer.ByteBuf;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.interfaces.IMasterable;
import me.jezzadabomb.es2.common.core.network.PacketUtils;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSetD;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSetF;
import me.jezzadabomb.es2.common.core.utils.helpers.MathHelper;
import me.jezzadabomb.es2.common.tileentity.framework.TileES;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityCombatDrone extends EntityDrone implements IEntityOwnable {
    String playerName;
    EntityPlayer player;
    boolean pathed;
    int timeTicked;

    public EntityCombatDrone(World par1World) {
        super(par1World);
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return boundingBox;
    }

    @Override
    public boolean hitByEntity(Entity entity) {
        return super.hitByEntity(entity);
    }

    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
        ESLogger.info("ASDASDASD");
        return super.attackEntityFrom(par1DamageSource, par2);
    }

    @Override
    public boolean interactFirst(EntityPlayer par1EntityPlayer) {
        ESLogger.info("FIRST");
        return super.interactFirst(par1EntityPlayer);
    }

    @Override
    public String getOwnerName() {
        return playerName;
    }

    @Override
    public Entity getOwner() {
        return player;
    }

    public void setOwner(EntityPlayer player) {
        this.player = player;
        playerName = player.getDisplayName();
    }

    @Override
    public boolean preTick() {
        if (player == null && playerName != null)
            player = worldObj.getPlayerEntityByName(playerName);

        return player == null || player.isDead || player.isInvisible();
    }

    @Override
    public void droneTick() {
        if (!worldObj.isRemote)
            return;

        if (++timeTicked >= 360)
            timeTicked = 0;

        if (!pathed)
            reachedTarget(false);
    }

    @Override
    public void postTick() {

    }

    @Override
    public void reachedTarget(boolean finalTarget) {
        setSpeed(0.09F);
        double timeMath = (Math.PI * timeTicked / 180);

        double xDis = Math.sin(timeMath);
        double zDis = Math.cos(timeMath);

        addCoordSetDToQueue(new CoordSetD(player).addXYZ(xDis, 0.0F, zDis));
        pathed = !finalTarget;
    }

    @Override
    public void readDroneSpawnData(ByteBuf additionalData) {
        playerName = PacketUtils.readStringByteBuffer(additionalData);
        timeTicked = additionalData.readInt();
    }

    @Override
    public void writeDroneSpawnData(ByteBuf buffer) {
        PacketUtils.writeStringByteBuffer(buffer, playerName);
        buffer.writeInt(timeTicked);
    }

    @Override
    public void readDroneFromNBT(NBTTagCompound tag) {
        playerName = tag.getString("owner");
    }

    @Override
    public void writeDroneToNBT(NBTTagCompound tag) {
        tag.setString("owner", playerName);
    }

    @Override
    protected void addDataWatchers() {

    }

    @Override
    public boolean preWorldProcessing() {
        return true;
    }
}
