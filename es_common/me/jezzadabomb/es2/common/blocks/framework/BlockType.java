package me.jezzadabomb.es2.common.blocks.framework;

import java.util.HashSet;

import me.jezzadabomb.es2.common.core.ESLogger;

public enum BlockType {
    NORMAL, MODEL, GLASS;

    private HashSet<String> overrideSet;

    private boolean isOpaqueCube = false;
    private boolean renderAsNormalBlock = false;
    private boolean shouldSideBeRendered = false;
    private int renderType = 0;

    private BlockType() {
        overrideSet = new HashSet<String>();
        switch (ordinal()) {
            case 0:
                break;
            case 1:
                initModel();
                break;
            case 2:
                initGlass();
                break;
            default:
                break;
        }
    }

    private void initModel() {
        renderAsNormalBlock = false;
        overrideSet.add("renderAsNormalBlock");
        isOpaqueCube = false;
        overrideSet.add("isOpaqueCube");
        renderType = -1;
        overrideSet.add("getRenderType");
    }

    private void initGlass() {
        renderAsNormalBlock = false;
        overrideSet.add("renderAsNormalBlock");
        isOpaqueCube = false;
        overrideSet.add("isOpaqueCube");
    }

    public boolean hasOverride(String override) {
        return overrideSet.contains(override);
    }

    public boolean isOpaqueCube() {
        return isOpaqueCube;
    }

    public boolean renderAsNormalBlock() {
        return renderAsNormalBlock;
    }

    public int getRenderType() {
        return renderType;
    }
}
