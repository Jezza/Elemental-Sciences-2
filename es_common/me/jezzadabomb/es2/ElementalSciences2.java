package me.jezzadabomb.es2;

import java.io.File;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.api.RenderBlackList;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.handlers.ConfigHandler;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.packets.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, dependencies = "required-after:Forge@[9.11.1.953,)")
@NetworkMod(channels = { Reference.CHANNEL_NAME }, clientSideRequired = true, serverSideRequired = true, packetHandler = PacketHandler.class)
public class ElementalSciences2 {

    @Instance(Reference.MOD_ID)
    public static ElementalSciences2 instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    public static CreativeTabs creativeTab = new CreativeTabs(Reference.MOD_ID) {
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return ModItems.atomicCatalyst;
        }
    };

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ESLogger.init();
        ConfigHandler.init(event.getModConfigurationDirectory().getAbsolutePath() + File.separator + Reference.CHANNEL_NAME.toLowerCase() + File.separator);
        ModBlocks.init();
        ModItems.init();
        proxy.runClientSide();
        proxy.runServerSide();
        proxy.initEventHandlers();
        proxy.initSoundHandler();
    }

    @EventHandler
    public void load(FMLInitializationEvent event) {
        proxy.registerTileEntities();
    }

    @EventHandler
    public void postLoaded(FMLPostInitializationEvent event) {
    }

    @EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        proxy.registerTickHandlers();
    }

}
