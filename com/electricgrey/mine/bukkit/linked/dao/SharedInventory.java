package com.electricgrey.mine.bukkit.linked.dao;

import java.util.LinkedList;
import java.util.List;

import com.electricgrey.mine.bukkit.linked.TileEntityVirtualChest;
import com.elmakers.mine.bukkit.persistence.annotation.PersistClass;
import com.elmakers.mine.bukkit.persistence.annotation.PersistField;

import net.minecraft.server.InventoryLargeChest;
import net.minecraft.server.ItemStack;

@PersistClass(name = "SharedInventory", schema="linked")
public class SharedInventory {
    String groupName;
    InventoryLargeChest inv;
    
    /** Default constructor for persistence */
    public SharedInventory() {
        inv = new InventoryLargeChest("Linked Chest", new TileEntityVirtualChest(), new TileEntityVirtualChest());
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
    public List<ItemStackDAO> getInvList() {
        List<ItemStackDAO> stackList = new LinkedList<ItemStackDAO>();
        for(ItemStack stack : inv.getContents()) {
            stackList.add(new ItemStackDAO(stack));
        }
        return stackList;
    }
    public void setInvList(List<ItemStackDAO> items) {
        inv = new InventoryLargeChest("Linked Chest", new TileEntityVirtualChest(), new TileEntityVirtualChest());
        for (int i = 0; i < items.size(); i++) {
            ItemStackDAO item = items.get(i);
            ItemStack is = new ItemStack(item.getId(), item.getAmount(), item.getDurability());
            inv.a(i, is);
        }
    }
    
    public InventoryLargeChest getInv() {
        inv.getContents();
        return inv;
    }
    public void setInv(InventoryLargeChest inv) {

    }
}
