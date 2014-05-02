package me.jezzadabomb.es2.common.core.utils;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Welcome to the Shad MultiCore.
 * 
 * @author Jeremy
 * 
 */
public class DimensionalPattern {

    private static HashMap<String, DimensionalPattern> dimensionalMap = new HashMap<String, DimensionalPattern>();

    private HashMap<Character, BlockState> comparisonMap = new HashMap<Character, BlockState>();
    private ArrayList<Layer> layers = new ArrayList<Layer>();

    private DimensionalPattern(IPatternComponent... components) {
        for (IPatternComponent component : components) {
            if (component instanceof Layer)
                layers.add((Layer) component);
            if (component instanceof BlockState) {
                BlockState blockState = (BlockState) component;
                comparisonMap.put(blockState.getType(), blockState);
            }
        }
    }

    /**
     * This method reuses same id patterns, thus reducing both duplicates in the system, and creates an easy way to get them. If you wish to replace/update a pattern, use updatePattern();
     * 
     * @param id
     *            The id the method will either store, if it hasn't been used, or call it from the list.
     * @param objects
     *            The pattern contents
     * @return
     */
    public static DimensionalPattern createPattern(String id, IPatternComponent... components) {
        if (dimensionalMap.containsKey(id))
            return dimensionalMap.get(id);

        if (components.length == 0)
            return null;

        DimensionalPattern pattern = new DimensionalPattern(components);

        dimensionalMap.put(id, pattern);

        return pattern;
    }

    /**
     * Used to update a pattern, without calling back a previous one. Don't really know why you would need this, but just in case. ;)
     * 
     * @param id
     * @param objects
     * @return
     */
    public static DimensionalPattern updatePattern(String id, IPatternComponent... components) {
        if (components.length == 0)
            return null;

        DimensionalPattern pattern = new DimensionalPattern(components);

        dimensionalMap.put(id, pattern);

        return pattern;
    }

    /**
     * Used to determine if the pattern has formed, starts from the lowest possible point, so give it the corner. EG, if it was the core of a 3x3x3, hasFormed(worldObj, xCoord - 1, yCoord - 1, zCoord - 1);
     * 
     * @param world
     * @param x
     * @param y
     * @param z
     * @return
     */
    public boolean hasFormed(IBlockAccess world, int x, int y, int z) {
        if (world == null)
            return false;

        int layerPos = 0;
        for (Layer layer : layers) {

            int rowPos = 0;
            for (Row row : layer.rows) {

                for (int depth = 0; depth < row.sections.length(); depth++) {
                    char type = row.sections.charAt(depth);
                    if (type == '*')
                        continue;
                    if (type == ' ') {
                        if (!world.isAirBlock(x + rowPos, y + layerPos, z + depth))
                            return false;
                        continue;
                    }
                    BlockState blockState = comparisonMap.get(Character.valueOf(type));
                    if (blockState == null || !blockState.isMatched(world, x + rowPos, y + layerPos, z + depth))
                        return false;
                }
                rowPos++;
            }
            layerPos++;
        }

        return true;
    }

    public boolean convert(World world, int x, int y, int z, Flag flag) {
        if (world == null)
            return false;

        int layerPos = 0;
        for (Layer layer : layers) {

            int rowPos = 0;
            for (Row row : layer.rows) {

                for (int depth = 0; depth < row.sections.length(); depth++) {
                    char type = row.sections.charAt(depth);
                    if (type == '*')
                        continue;
                    if (type == ' ') {
                        if (flag.isIgnoring())
                            world.setBlockToAir(x + rowPos, y + layerPos, z + depth);
                        continue;
                    }
                    BlockState blockState = comparisonMap.get(Character.valueOf(type));

                    if (blockState == null)
                        return false;

                    if (flag.isIgnoring()) {
                        world.setBlock(x + rowPos, y + layerPos, z + depth, blockState.block, blockState.meta, 3);
                        continue;
                    }

                    if (flag.isAir()) {
                        if (world.isAirBlock(x + rowPos, y + layerPos, z + depth)) {
                            world.setBlock(x + rowPos, y + layerPos, z + depth, blockState.block, blockState.meta, 3);
                            continue;
                        }
                    }
                }
                rowPos++;
            }
            layerPos++;
        }
        return true;
    }

    /**
     * Accessor Methods
     * 
     * @param rows
     * @return
     */
    public static Layer createLayer(Row... rows) {
        return new Layer(rows);
    }

    /**
     * Accessor Methods
     * 
     * @param sections
     * @return
     */
    public static Row createRow(String section) {
        return new Row(section);
    }

    public static BlockState createBlockState(char type, Block block, int meta) {
        return new BlockState(type, block, meta);
    }

    /**
     * Helper class
     * 
     */
    public static class Layer implements IPatternComponent {

        Row[] rows;

        private Layer(Row... rows) {
            this.rows = rows;
        }

        public Row[] getRows() {
            return rows;
        }
    }

    /**
     * Helper class
     * 
     */
    public static class Row implements IPatternComponent {

        String sections;

        private Row(String sections) {
            this.sections = sections;
        }

        public String getSections() {
            return sections;
        }
    }

    /**
     * The class that contains a block and a meta
     * 
     */
    public static class BlockState implements IPatternComponent {

        char type;
        Block block;
        int meta;

        private BlockState(char type, Block block, int meta) {
            this.type = type;
            this.block = block;
            this.meta = meta;
        }

        public char getType() {
            return type;
        }

        public boolean isMatched(IBlockAccess world, int x, int y, int z) {
            return world.getBlock(x, y, z).equals(block) && world.getBlockMetadata(x, y, z) == meta;
        }
    }

    public static enum Flag {
        //@formatter:off
        AIR(0),
        IGNORE(1);
        //@formatter:on

        int flag;

        private Flag(int flag) {
            this.flag = flag;
        }

        public boolean isAir() {
            return flag == 0;
        }

        public boolean isIgnoring() {
            return flag == 1;
        }
    }

    public static interface IPatternComponent {
    }
}
