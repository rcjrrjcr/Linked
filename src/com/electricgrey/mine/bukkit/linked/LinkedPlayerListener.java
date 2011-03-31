package com.electricgrey.mine.bukkit.linked;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.InventoryLargeChest;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

/**
 * Handle events for all Player related events
 * 
 * @author Mythmon
 */
public class LinkedPlayerListener extends PlayerListener {
    Linked plugin;

    public LinkedPlayerListener(Linked plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block b = event.getClickedBlock();
        Player p = event.getPlayer();
        String nextAction = plugin.nextActions.get(p);
        
    	Action a = event.getAction();
    	if(a != Action.LEFT_CLICK_BLOCK && a != Action.RIGHT_CLICK_BLOCK) return;
        if (b.getType() == Material.CHEST) {

        	if (nextAction != null)
        	{
        		if (nextAction.equalsIgnoreCase("link,"))
            	{
            		String groupName = nextAction.split(",")[1];
                    p.sendMessage("Linked to " + groupName + ".");
                    plugin.linkChest(b, groupName);
                    plugin.nextActions.remove(p);
                    return;
            	}
            	if (plugin.nextActions.get(p).equalsIgnoreCase("unlink"))
            	{
            		if (plugin.chestNames.containsKey(b)) {
                        String groupName = plugin.unlinkChest(b);
                        p.sendMessage("Chest succesfully unlinked from " + groupName + ".");
                    } else {
                        p.sendMessage("That chest isn't linked to anything. Action canceled.");
                    }
                    plugin.nextActions.remove(p);
                    return;
            	}
        	}
        	else if (a == Action.RIGHT_CLICK_BLOCK && plugin.chestNames.containsKey(b)) {
                String groupName = plugin.chestNames.get(b);
                InventoryLargeChest virtChest = plugin.chestGroups.get(groupName);

                EntityPlayer ep = ((CraftPlayer) p).getHandle();
                // ep.a is a obfuscated method name. It will show the virtual
                // chest to the player.
                ep.a(virtChest);
                event.setCancelled(true);
            }
        }
        else
        {
        	if (nextAction == null) return;
        	if (nextAction.equalsIgnoreCase("link,"))
        	{
        		p.sendMessage("Linking to group " + nextAction.split(",")[1] + " canceled.");
                plugin.nextActions.remove(p);
        	}
        	if (plugin.nextActions.get(p).equalsIgnoreCase("unlink"))
        	{
                p.sendMessage("That's not a chest. Action canceled.");
                plugin.nextActions.remove(p);        		
        	}
        }
    }
    
    /**
     * @param b
     * @return
     */
    
}