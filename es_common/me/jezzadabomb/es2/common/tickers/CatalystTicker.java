package me.jezzadabomb.es2.common.tickers;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class CatalystTicker implements ITickHandler {

    public static Map<Integer, LinkedBlockingQueue<VirtualBreaker>> breakList = new HashMap<Integer, LinkedBlockingQueue<VirtualBreaker>>();

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {

    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
        breakTicks((WorldServer) tickData[0]);
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.WORLD);
    }

    @Override
    public String getLabel() {
        return "ES2World";
    }

    private void breakTicks(WorldServer world) {
        int dim = world.provider.dimensionId;
        LinkedBlockingQueue<VirtualBreaker> queue = (LinkedBlockingQueue<VirtualBreaker>) breakList.get(Integer.valueOf(dim));

        if (queue != null) {
            boolean didSomething = false;
            int limit = 0;
            while (!didSomething) {
                VirtualBreaker vb = (VirtualBreaker) queue.poll();

                if (vb != null) {
                    int bi = world.getBlockId(vb.x, vb.y, vb.z);
                    int md = world.getBlockMetadata(vb.x, vb.y, vb.z);
                    boolean skip = Block.blocksList[vb.id].isWood(world, vb.x, vb.y, vb.z);
                    if ((vb.id == bi) && ((vb.meta == md) || skip)) {
                        if (limit++ > vb.speed * 3)
                            didSomething = true;
                        ArrayList<ItemStack> ret = Block.blocksList[bi].getBlockDropped(world, vb.x, vb.y, vb.z, md, vb.fortune);
                        for (ItemStack is : ret)
                            if (!vb.player.capabilities.isCreativeMode)
                                if (!vb.player.inventory.addItemStackToInventory(is))
                                    world.spawnEntityInWorld(new EntityItem(world, vb.x + 0.5D, vb.y + 0.5D, vb.z + 0.5D, is));
                        world.destroyBlock(vb.x, vb.y, vb.z, false);
                        if (vb.lifespan > 0) {
                            for (int xx = -1; xx <= 1; xx++)
                                for (int yy = -1; yy <= 1; yy++)
                                    for (int zz = -1; zz <= 1; zz++) {
                                        if (((xx == 0) && (yy == 0) && (zz == 0)) || ((world.getBlockId(vb.x + xx, vb.y + yy, vb.z + zz) != vb.idTarget) && ((world.getBlockMetadata(vb.x + xx, vb.y + yy, vb.z + zz) != vb.metaTarget) || !skip)))
                                            continue;
                                        queue.offer(new VirtualBreaker(vb.x + xx, vb.y + yy, vb.z + zz, vb.id, vb.meta, vb.idTarget, vb.metaTarget, vb.lifespan - 1, vb.player, vb.fortune, vb.speed));
                                    }
                        }
                    }
                } else {
                    didSomething = true;
                }
            }
            breakList.put(Integer.valueOf(dim), queue);
        }

    }

    public static void addBreaker(World world, int x, int y, int z, int id, int meta, int idT, int metaT, int life, EntityPlayer player, int fortune, int speed) {
        int dim = world.provider.dimensionId;
        if ((Block.blocksList[id] == null) || (Block.blocksList[id].getBlockHardness(world, x, y, z) < 0.0F)) {
            return;
        }
        LinkedBlockingQueue<VirtualBreaker> queue = (LinkedBlockingQueue<VirtualBreaker>) breakList.get(Integer.valueOf(dim));
        if (queue == null) {
            breakList.put(Integer.valueOf(dim), new LinkedBlockingQueue<VirtualBreaker>());
            queue = (LinkedBlockingQueue<VirtualBreaker>) breakList.get(Integer.valueOf(dim));
        }
        queue.offer(new VirtualBreaker(x, y, z, id, meta, idT, metaT, life, player, fortune, speed - 1));
        breakList.put(Integer.valueOf(dim), queue);
    }

    public static class VirtualBreaker {
        int x = 0;
        int y = 0;
        int z = 0;
        int id = 0;
        int meta = 0;
        int idTarget = 0;
        int metaTarget = 0;
        int lifespan = 0;
        int fortune = 0;
        int speed = 0;
        EntityPlayer player = null;

        VirtualBreaker(int x, int y, int z, int id, int meta, int idT, int metaT, int life, EntityPlayer p, int fortune, int speed) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.id = id;
            this.meta = meta;
            this.idTarget = idT;
            this.metaTarget = metaT;
            this.lifespan = life;
            this.fortune = fortune;
            this.speed = speed;
            this.player = p;
        }
    }

}
