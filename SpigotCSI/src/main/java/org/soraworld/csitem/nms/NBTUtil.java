package org.soraworld.csitem.nms;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

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
    }

    public static void setTag(org.bukkit.inventory.ItemStack stack, NBTTagCompound tag) {
        try {
            ((net.minecraft.server.v1_12_R1.ItemStack) handle.get(stack)).setTag(tag);
        } catch (Throwable ignored) {
        }
    }

    public static NBTTagCompound getOrCreateTag(ItemStack stack, String path) {
        if (stack == null) return new NBTTagCompound();
        if (!(stack instanceof CraftItemStack)) {
            stack = CraftItemStack.asCraftCopy(stack);
        }
        try {
            NBTTagCompound tag = ((net.minecraft.server.v1_12_R1.ItemStack) handle.get(stack)).getTag();
            if (tag == null) {
                tag = new NBTTagCompound();
                setTag(stack, tag);
            }
            if (path != null && !path.isEmpty()) {
                NBTTagCompound child = tag.getCompound(path);
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
        if (stack == null) return new NBTTagCompound();
        if (!(stack instanceof CraftItemStack)) {
            stack = CraftItemStack.asCraftCopy(stack);
        }
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
}
