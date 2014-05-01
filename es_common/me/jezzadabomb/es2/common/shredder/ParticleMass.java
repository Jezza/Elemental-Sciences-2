package me.jezzadabomb.es2.common.shredder;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class ParticleMass {

    private static HashMap<String, Integer> particleMap = new HashMap<String, Integer>() {
        {

        }
    };

    public ParticleMass() {

    }

    public static float getParticleMass(Block block) {
        return particleMap.get(block.getUnlocalizedName());
    }

    public void init() {
        // Resistance = Strength
        // Hardness = Density

        particleMap.put(Blocks.brown_mushroom_block.getUnlocalizedName(), 1);
        particleMap.put(Blocks.cake.getUnlocalizedName(), 2);
        particleMap.put(Blocks.glass_pane.getUnlocalizedName(), 3);
        particleMap.put(Blocks.cactus.getUnlocalizedName(), 6);
        particleMap.put(Blocks.ice.getUnlocalizedName(), 7);
        particleMap.put(Blocks.gravel.getUnlocalizedName(), 7);
        particleMap.put(Blocks.glass.getUnlocalizedName(), 8);
        particleMap.put(Blocks.glowstone.getUnlocalizedName(), 9);
        particleMap.put(Blocks.dirt.getUnlocalizedName(), 15);
        particleMap.put(Blocks.grass.getUnlocalizedName(), 17);
        particleMap.put(Blocks.clay.getUnlocalizedName(), 17);
        particleMap.put(Blocks.hardened_clay.getUnlocalizedName(), 21);
        particleMap.put(Blocks.coal_block.getUnlocalizedName(), 43);
        particleMap.put(Blocks.cobblestone.getUnlocalizedName(), 50);
        particleMap.put(Blocks.emerald_block.getUnlocalizedName(), 55);
        particleMap.put(Blocks.iron_block.getUnlocalizedName(), 55);
        particleMap.put(Blocks.gold_block.getUnlocalizedName(), 58);
        particleMap.put(Blocks.coal_ore.getUnlocalizedName(), 60);
        particleMap.put(Blocks.diamond_block.getUnlocalizedName(), 60);
        particleMap.put(Blocks.emerald_ore.getUnlocalizedName(), 62);
        particleMap.put(Blocks.diamond_ore.getUnlocalizedName(), 63);
        particleMap.put(Blocks.gold_ore.getUnlocalizedName(), 63);
        particleMap.put(Blocks.furnace.getUnlocalizedName(), 66);
        particleMap.put(Blocks.end_stone.getUnlocalizedName(), 68);
        particleMap.put(Blocks.dragon_egg.getUnlocalizedName(), 80);
        particleMap.put(Blocks.bedrock.getUnlocalizedName(), 100);

        particleMap.put(Blocks.heavy_weighted_pressure_plate.getUnlocalizedName(), 15);

        particleMap.put(Blocks.activator_rail.getUnlocalizedName(), 10);
        particleMap.put(Blocks.anvil.getUnlocalizedName(), 10);

        particleMap.put(Blocks.beacon.getUnlocalizedName(), 10);
        particleMap.put(Blocks.bed.getUnlocalizedName(), 10);
        particleMap.put(Blocks.bookshelf.getUnlocalizedName(), 10);
        particleMap.put(Blocks.brewing_stand.getUnlocalizedName(), 10);
        particleMap.put(Blocks.brick_block.getUnlocalizedName(), 10);

        particleMap.put(Blocks.cauldron.getUnlocalizedName(), 10);
        particleMap.put(Blocks.carpet.getUnlocalizedName(), 10);
        particleMap.put(Blocks.chest.getUnlocalizedName(), 10);
        particleMap.put(Blocks.crafting_table.getUnlocalizedName(), 10);

        particleMap.put(Blocks.daylight_detector.getUnlocalizedName(), 15);
        particleMap.put(Blocks.detector_rail.getUnlocalizedName(), 15);
        particleMap.put(Blocks.dispenser.getUnlocalizedName(), 15);
        particleMap.put(Blocks.dropper.getUnlocalizedName(), 15);

        particleMap.put(Blocks.enchanting_table.getUnlocalizedName(), 15);
        particleMap.put(Blocks.ender_chest.getUnlocalizedName(), 15);

        particleMap.put(Blocks.fence.getUnlocalizedName(), 15);
        particleMap.put(Blocks.fence_gate.getUnlocalizedName(), 15);

        particleMap.put(Blocks.golden_rail.getUnlocalizedName(), 15);

        particleMap.put(Blocks.hay_block.getUnlocalizedName(), 15);
        particleMap.put(Blocks.hopper.getUnlocalizedName(), 15);
        particleMap.put(Blocks.iron_bars.getUnlocalizedName(), 15);
        particleMap.put(Blocks.iron_ore.getUnlocalizedName(), 15);

        particleMap.put(Blocks.jukebox.getUnlocalizedName(), 15);

        particleMap.put(Blocks.ladder.getUnlocalizedName(), 15);
        particleMap.put(Blocks.lapis_block.getUnlocalizedName(), 15);
        particleMap.put(Blocks.lapis_ore.getUnlocalizedName(), 15);
        particleMap.put(Blocks.leaves.getUnlocalizedName(), 15);
        particleMap.put(Blocks.leaves2.getUnlocalizedName(), 15);
        particleMap.put(Blocks.lever.getUnlocalizedName(), 15);
        particleMap.put(Blocks.light_weighted_pressure_plate.getUnlocalizedName(), 15);
        particleMap.put(Blocks.lit_pumpkin.getUnlocalizedName(), 15);
        particleMap.put(Blocks.lit_redstone_lamp.getUnlocalizedName(), 15);
        particleMap.put(Blocks.log.getUnlocalizedName(), 15);
        particleMap.put(Blocks.log2.getUnlocalizedName(), 15);

        particleMap.put(Blocks.melon_block.getUnlocalizedName(), 15);
        particleMap.put(Blocks.mob_spawner.getUnlocalizedName(), 15);
        particleMap.put(Blocks.monster_egg.getUnlocalizedName(), 15);
        particleMap.put(Blocks.mossy_cobblestone.getUnlocalizedName(), 15);
        particleMap.put(Blocks.mycelium.getUnlocalizedName(), 15);

        particleMap.put(Blocks.nether_brick.getUnlocalizedName(), 15);
        particleMap.put(Blocks.nether_brick_fence.getUnlocalizedName(), 15);
        particleMap.put(Blocks.netherrack.getUnlocalizedName(), 15);
        particleMap.put(Blocks.noteblock.getUnlocalizedName(), 15);

        particleMap.put(Blocks.obsidian.getUnlocalizedName(), 15);

        particleMap.put(Blocks.packed_ice.getUnlocalizedName(), 15);
        particleMap.put(Blocks.piston.getUnlocalizedName(), 15);
        particleMap.put(Blocks.planks.getUnlocalizedName(), 15);
        particleMap.put(Blocks.pumpkin.getUnlocalizedName(), 15);

        particleMap.put(Blocks.quartz_block.getUnlocalizedName(), 15);
        particleMap.put(Blocks.quartz_ore.getUnlocalizedName(), 15);

        particleMap.put(Blocks.rail.getUnlocalizedName(), 15);
        particleMap.put(Blocks.red_mushroom_block.getUnlocalizedName(), 15);
        particleMap.put(Blocks.redstone_block.getUnlocalizedName(), 15);
        particleMap.put(Blocks.redstone_lamp.getUnlocalizedName(), 15);
        particleMap.put(Blocks.redstone_ore.getUnlocalizedName(), 15);
        particleMap.put(Blocks.redstone_torch.getUnlocalizedName(), 15);
        particleMap.put(Blocks.reeds.getUnlocalizedName(), 15);

        particleMap.put(Blocks.sand.getUnlocalizedName(), 15);
        particleMap.put(Blocks.sandstone.getUnlocalizedName(), 15);
        particleMap.put(Blocks.sapling.getUnlocalizedName(), 15);
        particleMap.put(Blocks.snow.getUnlocalizedName(), 15);
        particleMap.put(Blocks.snow_layer.getUnlocalizedName(), 15);
        particleMap.put(Blocks.soul_sand.getUnlocalizedName(), 15);
        particleMap.put(Blocks.sponge.getUnlocalizedName(), 15);
        particleMap.put(Blocks.stained_glass.getUnlocalizedName(), 15);
        particleMap.put(Blocks.stained_glass_pane.getUnlocalizedName(), 15);
        particleMap.put(Blocks.stained_hardened_clay.getUnlocalizedName(), 15);
        particleMap.put(Blocks.sticky_piston.getUnlocalizedName(), 15);
        particleMap.put(Blocks.stone.getUnlocalizedName(), 15);
        particleMap.put(Blocks.stone_button.getUnlocalizedName(), 15);
        particleMap.put(Blocks.stone_pressure_plate.getUnlocalizedName(), 15);
        particleMap.put(Blocks.stonebrick.getUnlocalizedName(), 15);

        particleMap.put(Blocks.tallgrass.getUnlocalizedName(), 15);
        particleMap.put(Blocks.tnt.getUnlocalizedName(), 15);
        particleMap.put(Blocks.torch.getUnlocalizedName(), 15);
        particleMap.put(Blocks.trapdoor.getUnlocalizedName(), 15);
        particleMap.put(Blocks.trapped_chest.getUnlocalizedName(), 15);
        particleMap.put(Blocks.tripwire_hook.getUnlocalizedName(), 15);

        particleMap.put(Blocks.vine.getUnlocalizedName(), 15);

        particleMap.put(Blocks.waterlily.getUnlocalizedName(), 15);
        particleMap.put(Blocks.web.getUnlocalizedName(), 15);
        particleMap.put(Blocks.wooden_button.getUnlocalizedName(), 15);
        particleMap.put(Blocks.wooden_pressure_plate.getUnlocalizedName(), 15);
        particleMap.put(Blocks.wool.getUnlocalizedName(), 15);
    }
}
