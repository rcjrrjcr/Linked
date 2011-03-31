package com.electricgrey.mine.bukkit.linked;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.server.BlockChest;
import net.minecraft.server.InventoryLargeChest;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Plugin for Bukkit. Links together chests, so their contents are synchronized.
 * 
 * @author Mythmon
 */
public class Linked extends JavaPlugin {
    private final LinkedPlayerListener playerListener = new LinkedPlayerListener(this);
    private final LinkedBlockListener blockListener = new LinkedBlockListener(this);
    private final LinkedStorage storage = new LinkedStorage(this);
//    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();

    public final Map<Block, String> chestNames = new HashMap<Block, String>();
    public final Map<String, InventoryLargeChest> chestGroups = new HashMap<String, InventoryLargeChest>();
    public final Map<Player, String> nextActions = new HashMap<Player, String>();

    @Override
    public void onDisable() {
        // TODO: Write data to disk
        // EXAMPLE: Custom code, here we just output some info so we can check
        // all is well
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is disabled.");

    }

    @Override
    public void onEnable() {
        // TODO: Read data from disk
//    	chestGroups.putAll(storage.loadAllInventories());
        // Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Priority.Monitor, this);
        pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Priority.Monitor, this);

        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled.");
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
    {
    	if(!(sender instanceof Player))
    	{
    		sender.sendMessage("Linked should only be used by a player, not the console.");
    		return true;
    	}
    	else
    	{
    		Player p = (Player) sender;
    		
    		if (args[0].equalsIgnoreCase("/link")) {
                if (args.length != 2) {
                    p.sendMessage("Usage: /link <groupname>");
                    return true;
                }
                String groupName = args[1];

                nextActions.put(p, "link," + groupName);
                p.sendMessage("Now left-click the chest you want to link to " + groupName + ".");
                return true;
            } else if (args[0].equalsIgnoreCase("/unlink")) {
                if (args.length > 1) {
                    p.sendMessage("Unlink doesn't take any parameters.");
                    return true;
                }

                nextActions.put(p, "unlink");
                p.sendMessage("Now left-click the chest you want to unlink.");
                return true;
            }
    	}
		return false;
    }
    
    String unlinkChest(Block b) {
        String groupName = chestNames.get(b);
        chestNames.remove(b);
        if(!chestNames.containsValue(groupName))
        {
        	InventoryLargeChest inv = chestGroups.get(groupName);
        	if(inv.m_()!=0)
        	{
        		if(b instanceof BlockChest)
        		{
        			Chest t = (Chest) b.getState();
        			net.minecraft.server.ItemStack[] mineChestItems = inv.getContents();
        			List<org.bukkit.inventory.ItemStack> stacks = new LinkedList<org.bukkit.inventory.ItemStack>();
        			for(net.minecraft.server.ItemStack virtItemStack : mineChestItems)
        			{
            			stacks.add(new ItemStack(virtItemStack.i(),virtItemStack.count,(short) virtItemStack.damage));
        			}
        			for(ItemStack bukkitStack : stacks)
        			{
        				HashMap<Integer,ItemStack> leftover = t.getInventory().addItem(bukkitStack);
        				if(leftover == null||leftover.isEmpty())
        				{
        					break;
        				}
        				else
        				{
        					stacks.remove(bukkitStack);
        				}
        			}
        			for(ItemStack bukkitStack : stacks)
        			{
        				b.getWorld().dropItemNaturally(b.getLocation(), bukkitStack);
        			}
        		}
        	}
        		
        }
        return groupName;
    }

    /**
     * @param b
     * @param groupName
     */
    void linkChest(Block b, String groupName) {
        chestNames.put(b, groupName);

        InventoryLargeChest virtChest;
        if (!chestGroups.containsKey(groupName)) {
            TileEntityVirtualChest chest1, chest2;
            chest1 = new TileEntityVirtualChest();
            chest2 = new TileEntityVirtualChest();
            virtChest = new InventoryLargeChest("Linked Chest", chest1, chest2);
            chestGroups.put(groupName, virtChest);
        } else {
            virtChest = chestGroups.get(groupName);
        }
    }
}