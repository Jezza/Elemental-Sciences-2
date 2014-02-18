package me.jezzadabomb.es2.client.sound;

import me.jezzadabomb.es2.common.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public enum Sounds {
    // @formatter:off
    CATALYST_PULSE("pulse"),
    SCANNING_WAVE("scanningWave");
    // @formatter:on

    private String name;

    Sounds(String name) {
        this.name = Reference.MOD_IDENTIFIER + name;
    }

    public String getName() {
        return name;
    }

    public void play(Entity entity) {
        play(entity, 1.0F, 1.0F);
    }

    public void play(Entity entity, float volume, float pitch) {
        entity.worldObj.playSoundAtEntity(entity, name, volume, pitch);
    }

    public void play(double x, double y, double z, float volume, float pitch) {
        Minecraft.getMinecraft().theWorld.playSound(x, y, z, name, volume, pitch, false);
    }

    public void play(double x, double y, double z) {
        play(x, y, z, 1.0F, 1.0F);
    }

    public void play(int x, int y, int z, float volume, float pitch) {
        play(x + 0.5F, y + 0.5F, z + 0.5F, volume, pitch);
    }

    public void play(int x, int y, int z) {
        play(x + 0.5F, y + 0.5F, z + 0.5F, 1.0F, 1.0F);
    }

    public void playAtPlayer(EntityLivingBase player) {
        play(player.posX, player.posY, player.posZ);
    }
}
