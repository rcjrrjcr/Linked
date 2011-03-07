package com.electricgrey.mine.bukkit.linked;

import org.bukkit.block.Block;

import com.elmakers.mine.bukkit.persistence.annotation.PersistClass;
import com.elmakers.mine.bukkit.persistence.annotation.PersistField;

@PersistClass(name = "ChestName", schema="linked")
public class ChestName {
    Block id;
    String groupName;
    
    /** Default constructor for persistence */
    public ChestName(){}
    
    /** For creating new objects */
    public ChestName(Block b, String groupName) {
        this.id = b;
        this.groupName = groupName;
    }
    
    /** The chest associated with the data */
    @PersistField(id=true)
    public Block getId() {
        return id;
    }
    public void setId(Block b) {
        this.id = b;
    }
    
    /** The group name associated with the chest */
    @PersistField
    public String getGroupName() {
        return this.groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}