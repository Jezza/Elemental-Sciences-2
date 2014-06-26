package me.jezzadabomb.es2.client.hud;

import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;

public class InventoryInstance {

    private String name;
    private int x;
    private int y;
    private int z;

    public InventoryInstance(String name, int x, int y, int z) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public boolean isAtXYZ(int x, int y, int z) {
        return this.x == x && this.y == y && this.z == z;
    }

    public boolean isName(String name) {
        if (name == null)
            return false;
        if (this.name == null)
            return false;
        return this.name.equals(name);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof InventoryInstance))
            return false;
        InventoryInstance inventory = (InventoryInstance) other;
        return (inventory.x == x && inventory.y == y && inventory.z == z && isName(inventory.name));
    }

    @Override
    public String toString() {
        return "Name: " + name + new CoordSet(x, y, z);
    }
}
