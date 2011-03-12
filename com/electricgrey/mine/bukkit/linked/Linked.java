package com.electricgrey.mine.bukkit.linked;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.elmakers.mine.bukkit.plugins.persistence.PersistencePlugin;
import com.elmakers.mine.craftbukkit.persistence.Persistence;

/**
 * Plugin for Bukkit. Links together chests, so their contents are synchronized.
 * 
 * @author Mythmon
 */
public class Linked extends JavaPlugin {
    private final LinkedPlayerListener playerListener = new LinkedPlayerListener(this);
    private final LinkedBlockListener blockListener = new LinkedBlockListener(this);
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();

    public final Map<Player, String> nextActions = new HashMap<Player, String>();
    
    public Persistence persistence;

    public void onDisable() {
        // TODO: Write data to disk

        // EXAMPLE: Custom code, here we just output some info so we can check
        // all is well
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is disabled.");

    }

    public void onEnable() {
        PluginDescriptionFile pdfFile = this.getDescription();
        if (initialize() == true) {
            System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled.");
        } else {
            System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " failed to initialize.");
            this.getServer().getPluginManager().disablePlugin(this);
        }
    }
    
    private boolean initialize() {
        // Enable persistence
        Plugin checkForPersistence = this.getServer().getPluginManager().getPlugin("Persistence");
        if (checkForPersistence != null)
        {
            PersistencePlugin plugin = (PersistencePlugin) checkForPersistence;
            persistence = plugin.getPersistence();
        } else {
            System.out.println("This plugin depends on Persistence");
            return false;
        }

        // Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_DAMAGED, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_INTERACT, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Priority.Normal, this);
        
        return true;
    }

    public boolean isDebugging(final Player player) {
        if (debugees.containsKey(player)) {
            return debugees.get(player);
        } else {
            return false;
        }
    }

    public void setDebugging(final Player player, final boolean value) {
        debugees.put(player, value);
    }
}