package com.electricgrey.mine.bukkit.linked;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.InventoryLargeChest;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockDamageLevel;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockInteractEvent;
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

    /**
     * Redirect certain events (to more separate the two distinct functions they
     * are used for)
     */
    @Override
    public void onBlockDamage(BlockDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        BlockDamageLevel level = event.getDamageLevel();

        if (level == BlockDamageLevel.STARTED) {
            blockTouched(event);
        }
    }

    @Override
    public void onBlockInteract(BlockInteractEvent event) {
        LivingEntity e = event.getEntity();
        Block b = event.getBlock();

        if (!(e instanceof Player)) {
            return;
        }

        Player p = (Player) e;

        if (b.getType() == Material.CHEST) {
            ChestName chestName = plugin.persistence.get(b, ChestName.class);
            if (chestName != null) {
                String groupName = chestName.getGroupName();
                SharedInventory sharedInventory = plugin.persistence.get(groupName, SharedInventory.class);
                InventoryLargeChest virtChest = sharedInventory.getInv();

                EntityPlayer ep = ((CraftPlayer) p).getHandle();
                // ep.a is a obfuscated method name. It will show the virtual
                // chest to the player.
                ep.a(virtChest);
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        Block b = event.getBlock();
        Player p = event.getPlayer();

        if (b.getType() == Material.CHEST) {
            String groupName = unlinkChest(b);
            p.sendMessage("You unlinked that chest from " + groupName);
        }
    }

    private void blockTouched(BlockDamageEvent event) {
        Player p = event.getPlayer();
        Block b = event.getBlock();

        if (!plugin.nextActions.containsKey(p)) {
            return;
        }

        String action = plugin.nextActions.get(p);
        if (action.startsWith("link,")) {
            String groupName = action.split(",")[1];
            if (b.getType() == Material.CHEST) {
                p.sendMessage("Linked to " + groupName + ".");
                linkChest(b, groupName);
            } else {
                p.sendMessage("Linking to group " + groupName + " canceled.");
            }
            plugin.nextActions.remove(p);
        } else if (action.equals("unlink")) {
            if (b.getType() == Material.CHEST) {
                String groupName = unlinkChest(b);
                if (groupName != null) {
                    p.sendMessage("Chest succesfully unlinked from " + groupName + ".");
                } else {
                    p.sendMessage("That chest isn't linked to anything. Action canceled.");
                }
            } else {
                p.sendMessage("That's not a chest. Action canceled.");
                plugin.nextActions.remove(p);
            }
        }
    }

    /**
     * @param b
     * @return
     */
    private String unlinkChest(Block b) {
        ChestName cn = plugin.persistence.get(b, ChestName.class);
        if (cn != null) {
            String groupName = cn.getGroupName();
            plugin.persistence.remove(b);
            return groupName;
        }
        return null;
    }

    private void linkChest(Block b, String groupName) {
        plugin.persistence.put(new ChestName(b, groupName));

        SharedInventory sharedInv = plugin.persistence.get(groupName, SharedInventory.class);
        InventoryLargeChest virtChest;
        if (sharedInv == null) {
            TileEntityVirtualChest chest1, chest2;
            chest1 = new TileEntityVirtualChest();
            chest2 = new TileEntityVirtualChest();
            virtChest = new InventoryLargeChest("Linked Chest", chest1, chest2);
            sharedInv = new SharedInventory(groupName, virtChest);
            plugin.persistence.put(sharedInv);
        } else {
            virtChest = sharedInv.getInv();
        }
    }
}