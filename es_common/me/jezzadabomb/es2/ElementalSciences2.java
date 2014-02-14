package me.jezzadabomb.es2;

import me.jezzadabomb.es2.common.Entities;
import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.config.ConfigHandler;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.network.PacketHandler;
import me.jezzadabomb.es2.common.network.PacketPipeline;
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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, dependencies = "required-after:Forge@[9.11.1.953,)")
public class ElementalSciences2 {

    @Instance(Reference.MOD_ID)
    public static ElementalSciences2 instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    public static CreativeTabs creativeTab = new CreativeTabs(Reference.MOD_ID) {
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return ModItems.glasses;
        }
    };

    public static final PacketPipeline packetPipeline = new PacketPipeline();
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ESLogger.init();
        ConfigHandler.init(event.getSuggestedConfigurationFile());
        ModBlocks.init();
        ModItems.init();
        proxy.runClientSide();
        proxy.runServerSide();
    }

    @EventHandler
    public void load(FMLInitializationEvent event) {
        proxy.registerTileEntities();
        Entities.init();
        packetPipeline.initalise();
        PacketHandler.init();
    }

    @EventHandler
    public void postLoaded(FMLPostInitializationEvent event) {
        packetPipeline.postInitialise();
    }

    @EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        proxy.registerTickHandlers();
    }

}
