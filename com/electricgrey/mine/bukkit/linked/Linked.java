package com.electricgrey.mine.bukkit.linked;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.server.InventoryLargeChest;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
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
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();

    public final Map<Block, String> chestNames = new HashMap<Block, String>();
    public final Map<String, InventoryLargeChest> chestGroups = new HashMap<String, InventoryLargeChest>();
    public final Map<Player, String> nextActions = new HashMap<Player, String>();

    public void onDisable() {
        // TODO: Write data to disk

        // EXAMPLE: Custom code, here we just output some info so we can check
        // all is well
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is disabled.");

    }

    public void onEnable() {
        // TODO: Read data from disk

        // Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_DAMAGED, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_INTERACT, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Priority.Normal, this);

        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled.");
    }
}