package com.electricgrey.mine.bukkit.linked;

import com.elmakers.mine.bukkit.persistence.annotation.PersistClass;
import com.elmakers.mine.bukkit.persistence.annotation.PersistField;

import org.bukkit.util.BlockVector;

@PersistClass(name = "ChestName", schema="linked")
public class ChestName {
    BlockVector id;
    String groupName;
    
    /** Default constructor for persistence */
    public ChestName(){}
    
    /** For creating new objects */
    public ChestName(BlockVector v, String groupName) {
        this.id = v;
        this.groupName = groupName;
    }
    
    /** The chest associated with the data */
    @PersistField(id=true)
    public BlockVector getId() {
        return id;
    }
    public void setId(BlockVector b) {
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