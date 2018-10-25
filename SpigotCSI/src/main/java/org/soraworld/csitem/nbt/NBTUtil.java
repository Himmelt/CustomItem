package org.soraworld.csitem.nbt;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.soraworld.csitem.data.Attrib;

import java.lang.reflect.Field;

public class NBTUtil {

    public static final int TAG_END = 0;
    public static final int TAG_BYTE = 1;
    public static final int TAG_SHORT = 2;
    public static final int TAG_INT = 3;
    public static final int TAG_LONG = 4;
    public static final int TAG_FLOAT = 5;
    public static final int TAG_DOUBLE = 6;
    public static final int TAG_BYTE_A = 7;
    public static final int TAG_STRING = 8;
    public static final int TAG_LIST = 9;
    public static final int TAG_COMP = 10;
    public static final int TAG_INT_A = 11;
    public static final String ATTRIBS = "attribs";

    private static final Field handle;
    private static final boolean support;

    static {
        Field _handle = null;
        boolean _support = false;
        try {
            _handle = CraftItemStack.class.getDeclaredField("handle");
            _handle.setAccessible(true);
            _support = true;
        } catch (Throwable ignored) {
        }
        handle = _handle;
        support = _support;
    }

    public static void setTag(org.bukkit.inventory.ItemStack stack, NBTTagCompound tag) {
        try {
            ((net.minecraft.server.v1_12_R1.ItemStack) handle.get(stack)).setTag(tag);
        } catch (Throwable ignored) {
        }
    }

    public static NBTTagCompound getOrCreateTag(ItemStack stack, String path) {
        try {
            NBTTagCompound tag = ((net.minecraft.server.v1_12_R1.ItemStack) handle.get(stack)).getTag();
            System.out.println("root tag:" + tag);
            if (tag == null) {
                tag = new NBTTagCompound();
                setTag(stack, tag);
            }
            if (path != null && !path.isEmpty()) {
                NBTTagCompound child = tag.getCompound(path);
                System.out.println("child tag:" + tag);
                tag.set(path, child);
                return child;
            }
            return tag;
        } catch (Throwable e) {
            e.printStackTrace();
            return new NBTTagCompound();
        }
    }

    public static NBTTagCompound getTag(ItemStack stack, String path) {
        try {
            NBTTagCompound tag = ((net.minecraft.server.v1_12_R1.ItemStack) handle.get(stack)).getTag();
            if (tag == null) return null;
            if (path != null && !path.isEmpty()) {
                NBTTagCompound child = tag.getCompound(path);
                if (child == null) return null;
                return child;
            }
            return tag;
        } catch (Throwable e) {
            e.printStackTrace();
            return new NBTTagCompound();
        }
    }

    public static Attrib getOrCreateAttrib(org.bukkit.inventory.ItemStack stack) {
        NBTTagCompound tag = getOrCreateTag(stack, "attrib");
        Attrib attrib = new Attrib();
        attrib.globalId = tag.getInt("globalId");
        attrib.active = tag.getBoolean("active");
        attrib.name = tag.getString("name");
        attrib.attack = tag.getInt("attack");
        attrib.manaAttack = tag.getInt("manaAttack");
        attrib.critChance = tag.getFloat("critChance");
        attrib.critDamage = tag.getFloat("critDamage");
        attrib.walkspeed = tag.getFloat("walkspeed");
        attrib.blockChance = tag.getFloat("blockChance");
        attrib.lastBlock = tag.getLong("lastBlock");
        attrib.dodgeChance = tag.getFloat("dodgeChance");
        attrib.lastDodge = tag.getLong("lastDodge");
        attrib.suckRatio = tag.getFloat("suckRatio");
        attrib.fireChance = tag.getFloat("fireChance");
        attrib.freezeChance = tag.getFloat("freezeChance");
        attrib.blockChance = tag.getFloat("blockChance");
        return attrib;
    }

    public static Attrib getAttrib(org.bukkit.inventory.ItemStack stack) {
        NBTTagCompound tag = getTag(stack, "attrib");
        if (tag == null || !tag.hasKey("active")) return null;
        Attrib attrib = new Attrib();
        attrib.globalId = tag.getInt("globalId");
        attrib.active = tag.getBoolean("active");
        attrib.name = tag.getString("name");
        attrib.attack = tag.getInt("attack");
        attrib.manaAttack = tag.getInt("manaAttack");
        attrib.critChance = tag.getFloat("critChance");
        attrib.critDamage = tag.getFloat("critDamage");
        attrib.walkspeed = tag.getFloat("walkspeed");
        attrib.blockChance = tag.getFloat("blockChance");
        attrib.lastBlock = tag.getLong("lastBlock");
        attrib.dodgeChance = tag.getFloat("dodgeChance");
        attrib.lastDodge = tag.getLong("lastDodge");
        attrib.suckRatio = tag.getFloat("suckRatio");
        attrib.fireChance = tag.getFloat("fireChance");
        attrib.freezeChance = tag.getFloat("freezeChance");
        attrib.blockChance = tag.getFloat("blockChance");
        return attrib;
    }

    public static void offerAttrib(org.bukkit.inventory.ItemStack stack, Attrib attrib) {
        NBTTagCompound tag = getOrCreateTag(stack, "attrib");
        tag.setInt("globalId", attrib.globalId);
        tag.setBoolean("active", attrib.active);
        tag.setString("name", attrib.name);
        tag.setInt("attack", attrib.attack);
        tag.setInt("manaAttack", attrib.manaAttack);
        tag.setFloat("critChance", attrib.critChance);
        tag.setFloat("critDamage", attrib.critDamage);
        tag.setFloat("walkspeed", attrib.walkspeed);
        tag.setFloat("blockChance", attrib.blockChance);
        tag.setLong("lastBlock", attrib.lastBlock);
        tag.setFloat("dodgeChance", attrib.dodgeChance);
        tag.setLong("lastDodge", attrib.lastDodge);
        tag.setFloat("suckRatio", attrib.suckRatio);
        tag.setFloat("fireChance", attrib.fireChance);
        tag.setFloat("freezeChance", attrib.freezeChance);
        tag.setFloat("blockChance", attrib.blockChance);
    }
}
