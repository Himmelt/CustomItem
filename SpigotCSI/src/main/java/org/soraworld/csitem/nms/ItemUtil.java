package org.soraworld.csitem.nms;

import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;

import static org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack.asCraftMirror;

public class ItemUtil {

    public static org.bukkit.inventory.ItemStack createItemStack(String itemId, int amount, int damage) {
        Item item = Item.REGISTRY.get(new MinecraftKey(itemId));
        if (item != null) return asCraftMirror(new ItemStack(item, amount, damage));
        return null;
    }

    public static org.bukkit.inventory.ItemStack createItemStack(int itemId, int amount, int damage) {
        Item item = Item.getById(itemId);
        if (item != null) return asCraftMirror(new ItemStack(item, amount, damage));
        return null;
    }
}
