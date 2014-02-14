package me.jezzadabomb.es2.client.sound;

import me.jezzadabomb.es2.common.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public enum Sounds {
    CATALYST_PULSE("pulse"),
    SCANNING_WAVE("scanningWave");
    
    private final String SOUNDS_LOCATION = Reference.MOD_ID.toLowerCase() + ":";
    private String name;
    
    Sounds(String name){
        this.name = SOUNDS_LOCATION + name;
    }
    
    public String getName(){
        return name;
    }
    
    public void play(World world, double x, double y, double z, float volume, float pitch){
//        Minecraft.getMinecraft().sndManager.playSound(SOUNDS_LOCATION + name, (float)x, (float)y, (float)z, volume, pitch);
    }

    public void play(int x, int y, int z, float volume, float pitch){
//        Minecraft.getMinecraft().sndManager.playSound(SOUNDS_LOCATION + name, (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, volume, pitch);
    }
    
    public void play(double x, double y, double z){
//        Minecraft.getMinecraft().sndManager.playSound(SOUNDS_LOCATION + name, (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, 1.0F, 0.0F);
    }
    
    public void playAtPlayer(EntityLivingBase player){
        play(player.posX, player.posY, player.posZ);
    }
}
