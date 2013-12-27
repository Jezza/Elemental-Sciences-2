package me.jezzadabomb.es2.client.sound;

import me.jezzadabomb.es2.common.core.ESLogger;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class SoundHandler {
    @ForgeSubscribe
    public void onSoundsLoad(SoundLoadEvent event){
        for(Sounds sound : Sounds.values()){
            try {
                event.manager.soundPoolSounds.addSound(Sounds.SOUNDS_LOCATION + sound.getName() + ".ogg");
            }
            catch (Exception e) {
                ESLogger.warning("Failed loading sound file: " + sound.getName());
            }
        }
    }
}
