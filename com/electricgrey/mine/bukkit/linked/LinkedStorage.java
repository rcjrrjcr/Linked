package com.electricgrey.mine.bukkit.linked;

import java.io.*;
import java.sql.*;

import net.minecraft.server.IInventory;
import net.minecraft.server.InventoryLargeChest;
import net.minecraft.server.ItemStack;

import org.bukkit.block.Block;

public class LinkedStorage {
    Linked plugin;
    File dbFile;
        
    public LinkedStorage(Linked plugin) {
        this.plugin = plugin;
        
        try {
            Class.forName("org.sqlite.JDBC");

            boolean newFile = false;
            dbFile = new File(plugin.getDataFolder(), "db.sqlite");
            Connection conn = dbConnect();
            Statement stmt = conn.createStatement();
            
            // create tables, if needed
            String query = "CREATE TABLE IF NOT EXISTS `loctoinv` (" +
                           "`loc` VARCHAR PRIMARY KEY," + 
                           "`inventory_id` INT NOT NULL);";
            stmt.execute(query);

            query = "CREATE TABLE IF NOT EXISTS `inventory` (" +
                    "`id` PRIMARY KEY ASC AUTOINCREMENT," + 
                    "`name` VARCHAR NOT NULL);";
            stmt.execute(query);

            query = "CREATE TABLE IF NOT EXISTS `stack` (" +
                    "`id` PRIMARY KEY ASC AUTOINCREMENT," +
                    "`inventory_id` INT NOT NULL" + 
                    "`type` INT NOT NULL," +
                    "`amount` INT NOT NULL," +
                    "`durability` INT NOT NULL," +
                    "`data-type` INT," +
                    "`data-value` INT);";
            stmt.execute(query);
            conn.close();
            
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Cannot find SQLite.");
        } catch (SQLException e) {
            System.out.println("Error: SQL Error in linked."); 
        }
    }

    private Connection dbConnect() throws SQLException {
        String conStr = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        Connection connection = DriverManager.getConnection(conStr);
        return connection;
    }
    
    public IInventory getInventory(Block b) {
        Connection conn = null;
        try {
            conn = dbConnect();
            String loc = b.getLocation().toVector().toString();
            PreparedStatement stmt = conn.prepareStatement("SELECT inventory.name stack.* FROM loctoinv, inventory, stack" +
            		"WHERE loctoinv.loc=? LIMIT 1" +
            		"AND loctoinv.inventory_id = inventory.id" +
            		"AND inventory.id = stack.inventory_id;");
            stmt.setString(0, loc);
            stmt.execute();
            ResultSet res = stmt.getResultSet();
            
            String groupName = res.getString("inventory.name");
            IInventory inv = new InventoryLargeChest(groupName, new TileEntityVirtualChest(), new TileEntityVirtualChest());
            res.beforeFirst();
            for (int i=0; res.next(); i++) {
                int type = res.getInt("stack.type");
                int amount = res.getInt("stack.amount");
                int damage = res.getInt("stack.damage");
                ItemStack s = new ItemStack(type, amount, damage);
                inv.a(i, s);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        
        return null;
    }
}
