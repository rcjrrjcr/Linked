package com.electricgrey.mine.bukkit.linked;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

/**
 * Linked block listener
 * 
 * @author Mythmon
 */
public class LinkedBlockListener extends BlockListener {
    Linked plugin;

    public LinkedBlockListener(final Linked plugin) {
        this.plugin = plugin;
    }
    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        Block b = event.getBlock();
        Player p = event.getPlayer();

        if (b.getType() == Material.CHEST && plugin.chestNames.containsKey(b)) {
            String groupName = plugin.unlinkChest(b);
            p.sendMessage("You unlinked that chest from " + groupName);
        }
    }

    
}