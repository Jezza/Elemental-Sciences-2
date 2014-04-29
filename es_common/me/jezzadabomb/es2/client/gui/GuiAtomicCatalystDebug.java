package me.jezzadabomb.es2.client.gui;

import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.containers.ContainerAtomicCatalystDebug;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.network.PacketDispatcher;
import me.jezzadabomb.es2.common.core.network.packet.client.AtomicCatalystDebugPacket;
import me.jezzadabomb.es2.common.core.utils.AtomicCatalystAttribute;
import me.jezzadabomb.es2.common.core.utils.helpers.MathHelper;
import me.jezzadabomb.es2.common.lib.GuiTextureMaps;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GuiAtomicCatalystDebug extends GuiContainerES {

    EntityPlayer player;
    ItemStack itemStack;

    AtomicCatalystAttribute attribute;

    public GuiAtomicCatalystDebug(EntityPlayer player, ItemStack itemStack) {
        super(new ContainerAtomicCatalystDebug(player, itemStack));
        this.player = player;
        this.itemStack = itemStack;

        if (itemStack.hasTagCompound())
            this.attribute = AtomicCatalystAttribute.readFromNBT(itemStack.getTagCompound());

        xSize = 136;
        ySize = 136;
    }

    @Override
    public void drawDefaultBackground() {
    }

    @Override
    public void initGui() {
        super.initGui();

        int yOffset = 30;

        for (int i = 0; i <= 2; i++) {
            addButton(guiLeft + 20          , guiTop + 10 + (i * yOffset), 20, 20, "<");
            addButton(guiLeft + xSize - 40  , guiTop + 10 + (i * yOffset), 20, 20, ">");
        }
        addButton(guiLeft + xSize / 2 - 20, guiTop + ySize - 30, 40, 20, "Done");
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        int strength = attribute.getStrength();
        int speed = attribute.getSpeed();
        int fortune = attribute.getFortune();

        switch (button.id) {
            case 0:
                strength--;
                break;
            case 1:
                strength++;
                break;
            case 2:
                speed--;
                break;
            case 3:
                speed++;
                break;
            case 4:
                fortune--;
                break;
            case 5:
                fortune++;
                break;
            case 6:
                PacketDispatcher.sendToServer(new AtomicCatalystDebugPacket(attribute));
                attribute.writeToNBT(itemStack.getTagCompound());
                mc.thePlayer.closeScreen();
        }
        fortune = MathHelper.clipInt(fortune, 0, 5);
        speed = MathHelper.clipInt(speed, 0, 5);
        strength = MathHelper.clipInt(strength, 1, 7);

        attribute.updateValues(fortune, speed, strength);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        int yOffset = -30;
        int index = 0;
        drawCentredText(0, 8 - index++ * yOffset, "Fo: " + attribute.getFortune());
        drawCentredText(0, 8 + index++ * yOffset, "Sp: " + attribute.getSpeed());
        drawCentredText(0, 8 + index++ * yOffset, "St: " + attribute.getStrength());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        RenderUtils.bindTexture(GuiTextureMaps.GUI_ATOMIC_CATALYST_DEBUG);

        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
