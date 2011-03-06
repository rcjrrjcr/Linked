package com.mythmon.mine.bukkit.linked;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
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
    public void onPlayerCommandPreprocess(PlayerChatEvent event) {
        String[] cmd = event.getMessage().split(" ");
        Player p = event.getPlayer();

        if (event.isCancelled()) {
            return;
        }

        if (cmd[0].equalsIgnoreCase("/link")) {
            if (cmd.length != 2) {
                p.sendMessage("Usage: /link <groupname>");
                return;
            }
            String groupName = cmd[1];

            plugin.nextActions.put(p, "link," + groupName);
            p.sendMessage("Now left-click the chest you want to link to " + groupName + ".");

            event.setCancelled(true);
        } else if (cmd[0].equalsIgnoreCase("/unlink")) {
            if (cmd.length > 1) {
                p.sendMessage("Unlink doesn't take any parameters.");
                return;
            }

            plugin.nextActions.put(p, "unlink");
            p.sendMessage("Now left-click the chest you want to unlink.");
            event.setCancelled(true);
        }
    }
}