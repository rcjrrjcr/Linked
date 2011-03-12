package com.electricgrey.mine.bukkit.linked.dao;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.bukkit.inventory.ItemStack;

import com.elmakers.mine.bukkit.persistence.annotation.PersistClass;
import com.elmakers.mine.bukkit.persistence.annotation.PersistField;

@PersistClass(name="ItemStack", schema="linked")
public class ItemStackDAO extends ItemStack {
    @PersistField(id=true, auto=true)
    public int id;

    /** This should *only* be used by Persistence initialization. */
    public ItemStackDAO() {
        super(0);
    }
    
    public ItemStackDAO(ItemStack s) {
        super(s.getTypeId(), s.getAmount(), s.getDurability(), s.getData().getData());
    }
    
    public ItemStackDAO(net.minecraft.server.ItemStack stack) {
        super(stack.id, stack.count, (short)stack.damage, (byte)0);
    }
    
    @PersistField
    public byte getDataByte() {
        return super.getData().getData();
    }
    public void setDataByte(byte data) {
        Material mat = getType();
        MaterialData md = null;
        if (mat == null) {
            md = new MaterialData(getType(), data);
        } else {
            md = mat.getNewData(data);
        }
        super.setData(md);
    }

    @PersistField
    @Override
    public int getTypeId() {
        return super.getTypeId();
    }
    @Override
    public void setTypeId(int type) {
        super.setTypeId(type);
    }
    
    @PersistField
    @Override
    public int getAmount() {
        return super.getAmount();
    }
    @Override
    public void setAmount(int amount) {
        super.setAmount(amount);
    }
    
    @PersistField
    @Override
    public short getDurability() {
        return super.getDurability();
    }
    @Override
    public void setDurability(short durability) {
        super.setDurability(durability);
    }
}
