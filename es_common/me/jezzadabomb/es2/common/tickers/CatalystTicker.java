package me.jezzadabomb.es2.common.tickers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class CatalystTicker {

    public static Map<Integer, LinkedBlockingQueue<VirtualBreaker>> breakList = new HashMap<Integer, LinkedBlockingQueue<VirtualBreaker>>();

    @SubscribeEvent
    public void worldTick(TickEvent.WorldTickEvent event) {
        breakTicks(event.world);
    }

    private void breakTicks(World world) {
        int dim = world.provider.dimensionId;
        LinkedBlockingQueue<VirtualBreaker> queue = (LinkedBlockingQueue<VirtualBreaker>) breakList.get(Integer.valueOf(dim));

        if (queue != null) {
            boolean didSomething = false;
            int limit = 0;
            while (!didSomething) {
                VirtualBreaker vb = (VirtualBreaker) queue.poll();

                if (vb != null) {
                    Block block = world.getBlock(vb.x, vb.y, vb.z);
                    int md = world.getBlockMetadata(vb.x, vb.y, vb.z);

                    boolean skip = vb.block.isWood(world, vb.x, vb.y, vb.z);
                    if ((vb.block.equals(block)) && ((vb.meta == md) || skip)) {
                        if (limit++ > vb.speed * 3)
                            didSomething = true;
                        ArrayList<ItemStack> ret = vb.block.getDrops(world, vb.x, vb.y, vb.z, md, vb.fortune);
                        for (ItemStack is : ret)
                            if (!vb.player.capabilities.isCreativeMode)
                                if (!vb.player.inventory.addItemStackToInventory(is))
                                    world.spawnEntityInWorld(new EntityItem(world, vb.x + 0.5D, vb.y + 0.5D, vb.z + 0.5D, is));
                        world.func_147480_a(vb.x, vb.y, vb.z, false);

                        if (vb.lifespan > 0)
                            for (int xx = -1; xx <= 1; xx++)
                                for (int yy = -1; yy <= 1; yy++)
                                    for (int zz = -1; zz <= 1; zz++) {
                                        if (((xx == 0) && (yy == 0) && (zz == 0)) || (!(world.getBlock(vb.x + xx, vb.y + yy, vb.z + zz).equals(vb.block)) && ((world.getBlockMetadata(vb.x + xx, vb.y + yy, vb.z + zz) != vb.meta) || !skip)))
                                            continue;
                                        queue.offer(new VirtualBreaker(vb.x + xx, vb.y + yy, vb.z + zz, vb.block, vb.meta, vb.lifespan - 1, vb.player, vb.fortune, vb.speed));
                                    }

                    }
                } else {
                    didSomething = true;
                }
            }
            breakList.put(Integer.valueOf(dim), queue);
        }

    }

    public static void addBreaker(World world, int x, int y, int z, Block block, int meta, int life, EntityPlayer player, int fortune, int speed) {
        int dim = world.provider.dimensionId;
        if ((block == null) || (block.getBlockHardness(world, x, y, z) < 0.0F))
            return;
        LinkedBlockingQueue<VirtualBreaker> queue = (LinkedBlockingQueue<VirtualBreaker>) breakList.get(Integer.valueOf(dim));
        if (queue == null) {
            breakList.put(Integer.valueOf(dim), new LinkedBlockingQueue<VirtualBreaker>());
            queue = (LinkedBlockingQueue<VirtualBreaker>) breakList.get(Integer.valueOf(dim));
        }
        queue.offer(new VirtualBreaker(x, y, z, block, meta, life, player, fortune, speed - 1));
        breakList.put(Integer.valueOf(dim), queue);
    }

    public static class VirtualBreaker {
        Block block;
        int x = 0;
        int y = 0;
        int z = 0;
        int meta = 0;
        int lifespan = 0;
        int fortune = 0;
        int speed = 0;
        EntityPlayer player = null;

        VirtualBreaker(int x, int y, int z, Block block, int meta, int life, EntityPlayer p, int fortune, int speed) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.block = block;
            this.meta = meta;
            this.lifespan = life;
            this.fortune = fortune;
            this.speed = speed;
            this.player = p;
        }
    }

}
