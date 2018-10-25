package org.soraworld.csitem.nbt;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;

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

    public static NBTTagCompound getOrCreateTag(org.bukkit.inventory.ItemStack stack) {
        try {
            NBTTagCompound tag = ((net.minecraft.server.v1_12_R1.ItemStack) handle.get(stack)).getTag();
            if (tag == null) {
                tag = new NBTTagCompound();
                setTag(stack, tag);
            }
            return tag;
        } catch (Throwable ignored) {
            return new NBTTagCompound();
        }
    }
}
