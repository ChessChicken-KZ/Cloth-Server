package net.minecraft.clothutils;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ItemStack;

import java.io.File;

/**
 * Extended Player controlling class
 * @author ChessChicken-KZ
 */
public class PlayerControlHandler {
    private final MinecraftServer minecraftServer;

    public PlayerControlHandler(MinecraftServer server)
    {
        minecraftServer = server;
    }

    /**
     * Returns a 4 strings.
     * First line: 'Player: $player'
     * Second line: 'X:$player.posX Y:$player.posY Z:$player.posZ'
     * Third line: 'Armor: armor info'
     * Fourth line: 'holding itemstack info'
     * @param player
     * @return
     */
    public String[] getPlayerInfo(String player)
    {
        String[] toSend;
        EntityPlayerMP toR = minecraftServer.configManager.getPlayerEntity(player);
        if(toR != null)
            toSend = new String[]{
                    "Player: "+player,
                    "X:"+toR.posX+ " Y:"+toR.posY+ " Z:"+toR.posZ,
                    "Armor: " + getItemStackID(toR.inventory.armorInventory, 0) + "," + getItemStackID(toR.inventory.armorInventory, 1) + "," + getItemStackID(toR.inventory.armorInventory, 2) + "," +  getItemStackID(toR.inventory.armorInventory, 3),
                    "Holding: " + (toR.func_172_B() != null ? ("ID:"+toR.func_172_B().itemID + " -- SIZE:" + toR.func_172_B().stackSize) : "0"),

            };
        else
            toSend = new String[] { "Cannot find player "+player};
        return toSend;
    }

    /**
     * Returns a 5 strings.
     * First line: 'Inventory of player $player'
     * Second line: 0-9 ID + STACKSIZE
     * Third line: 10-18 ID + STACKSIZE
     * Fourth line: 19-27 ID + STACKSIZE
     * Fifth line: 28 - 36 ID + STACKSIZE
     * @param player
     * @return strings
     */
    public String[] getBPIL(String player)
    {
        String[] toSend;
        EntityPlayerMP toR = minecraftServer.configManager.getPlayerEntity(player);
        if(toR != null)
        {
            toSend = new String[5];
            toSend[0] = "Inventory of player "+player;
            for(int i = 0; i < 4; i++)
            {
                toSend[i+1] = divideMassive(toR.inventory.mainInventory, 1+i);
            }
        }
        else
            toSend = new String[] { "Cannot find player "+player};
        return toSend;
    }

    /**
     * Changes the player slot into other ItemStack
     * @param player
     * @param slot
     * @param idEdit
     * @param countEdit
     * @param metaEdit
     * @return
     */
    public boolean changePlayerSlot(String player, int slot, String idEdit, int countEdit, int metaEdit)
    {
        EntityPlayerMP toR = minecraftServer.configManager.getPlayerEntity(player);

        if(toR != null)
            if(slot >= 0 && slot <= 35)
            {
                int j;
                if(!isNumber(idEdit))
                {
                    BlockMappingsManager BlockMappings = new BlockMappingsManager(new File("blocks.mappings"));
                    FallbackIdMaps fallbackIdMaps = new FallbackIdMaps();
                    j = BlockMappings.getIdForString(idEdit, fallbackIdMaps.GetIDForNamespacedBlockName(idEdit)); // This is literally the only change between give and giveID
                }else
                    j = Integer.parseInt(idEdit);
                toR.inventory.mainInventory[slot] = new ItemStack(j, countEdit, metaEdit);
                return true;
            }

        return false;
    }

    /**
     * Deletes itemstack in slot if it exists.
     * @param player
     * @param slot
     * @return
     */
    public boolean destroyPlayerSlot(String player, int slot)
    {
        EntityPlayerMP toR = minecraftServer.configManager.getPlayerEntity(player);

        if(toR != null)
            if(toR.inventory.mainInventory[slot] != null)
            {
                toR.inventory.mainInventory[slot] = new ItemStack(0);
                return true;
            }

        return false;
    }




    private int getItemStackID(ItemStack[] armor, int num)
    {
        if(armor[num] != null)
            return armor[num].itemID;
        return 0;
    }

    private String divideMassive(ItemStack[] itemStacks, int line)
    {
        int q1 = (line * 9);
        int a1 = q1  - 9;
        StringBuilder builder = new StringBuilder();
        for(int i = a1; i < q1; i++)
        {
            builder.append((itemStacks[i] != null) ? ("ID:"+itemStacks[i].itemID+" S:"+itemStacks[i].stackSize) : "");
            builder.append("   ");
        }
        return builder.toString();
    }

    private boolean isNumber(String number) {
        if (number == null)
            return false;

        try {
            double d = Double.parseDouble(number);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}