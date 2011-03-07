package com.electricgrey.mine.bukkit.linked;

import com.elmakers.mine.bukkit.persistence.annotation.PersistClass;
import com.elmakers.mine.bukkit.persistence.annotation.PersistField;

import net.minecraft.server.InventoryLargeChest;

@PersistClass(name = "SharedInventory", schema="linked")
public class SharedInventory {
    String groupName;
    InventoryLargeChest inv;
    
    /** Default constructor for persistance */
    public SharedInventory() {
    }
    
    public SharedInventory(String groupName, InventoryLargeChest inv) {
        this.groupName = groupName;
        this.inv = inv;
    }
    
    @PersistField(id=true)
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    @PersistField
    public InventoryLargeChest getInv() {
        return inv;
    }
    public void setInv(InventoryLargeChest inv) {
        this.inv = inv;
    }
}
