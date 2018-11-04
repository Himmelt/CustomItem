package org.soraworld.csitem.manager;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.soraworld.csitem.data.Attrib;
import org.soraworld.csitem.nms.NBTUtil;
import org.soraworld.hocon.node.Setting;
import org.soraworld.violet.manager.SpigotManager;
import org.soraworld.violet.plugin.SpigotPlugin;
import org.soraworld.violet.util.ChatColor;

import java.nio.file.Path;

import static org.soraworld.csitem.manager.CSIManager.getGlobal;
import static org.soraworld.csitem.task.PlayerTickTask.createTask;

public class AttribManager extends SpigotManager {

    @Setting(comment = "comment.firstGlobal")
    private boolean firstGlobal = true;
    @Setting(comment = "comment.updateTicks")
    private int updateTicks = 10;

    private final CSIManager csi;

    public AttribManager(SpigotPlugin plugin, Path path) {
        super(plugin, path);
        csi = new CSIManager(path.resolve("globalItems.conf"), options);
    }

    public boolean load() {
        csi.loadItems();
        return super.load();
    }

    public boolean save() {
        csi.saveItems();
        return super.save();
    }

    public void afterLoad() {
        Bukkit.getServer().getOnlinePlayers().forEach(this::createPlayerTask);
    }

    public void loadItems() {
        csi.loadItems();
    }

    public void saveItems() {
        csi.saveItems();
    }

    public ChatColor defChatColor() {
        return ChatColor.AQUA;
    }

    public void createPlayerTask(Player player) {
        createTask(player, plugin, updateTicks);
    }

    public void showAttribInfo(CommandSender sender, int id) {
        // TODO show info
        Attrib global = CSIManager.getGlobal(id);
        if (global != null) {
            sendKey(sender, "\n" + global.toString());
        } else sendKey(sender, "global.idNotExist");
    }

    public void showAttribInfo(CommandSender sender, String name) {
        // TODO show info
        Attrib global = getGlobal(name);
        if (global != null) {
            sendKey(sender, global.toString());
        } else sendKey(sender, "global.idNotExist");
    }

    public void listItems(CommandSender sender, int page) {
        if (page < 1) page = 1;
        sendKey(sender, "list.head");
        int i = 1;
        for (Integer key : CSIManager.items.keySet()) {
            if (i <= page * 10) {
                if (i >= page * 10 - 9) {
                    Attrib attrib = CSIManager.items.get(key);
                    sendKey(sender, "list.line", attrib.globalId, attrib.name, attrib.level, attrib.points);
                }
                i++;
            } else break;
        }
        sendKey(sender, "list.foot", page, CSIManager.items.size() / 10 + 1);
    }

    public static Attrib getOrCreateAttrib(org.bukkit.inventory.ItemStack stack) {
        NBTTagCompound tag = NBTUtil.getOrCreateTag(stack, "attrib");
        Attrib attrib = new Attrib();
        attrib.globalId = tag.getInt("globalId");
        nbt2attrib(tag, attrib);
        return attrib;
    }

    public static Attrib getAttrib(org.bukkit.inventory.ItemStack stack) {
        NBTTagCompound tag = NBTUtil.getTag(stack, "attrib");
        if (tag == null) return null;
        Attrib attrib = new Attrib();
        attrib.globalId = tag.getInt("globalId");
        nbt2attrib(tag, attrib);
        return attrib;
    }

    public static void offerAttrib(org.bukkit.inventory.ItemStack stack, final Attrib attrib) {
        NBTTagCompound tag = NBTUtil.getOrCreateTag(stack, "attrib");
        tag.setInt("globalId", attrib.globalId);
        if (attrib.globalId <= 0) {
            tag.setInt("globalId", attrib.globalId);
            tag.setBoolean("active", attrib.isActive());
            tag.setInt("level", attrib.level);
            tag.setInt("points", attrib.points);
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
            tag.setDouble("dodgeX", attrib.dodgeX);
            tag.setDouble("dodgeX", attrib.dodgeZ);
            tag.setFloat("suckRatio", attrib.suckRatio);
            tag.setFloat("fireChance", attrib.fireChance);
            tag.setFloat("freezeChance", attrib.freezeChance);
            tag.setFloat("blockChance", attrib.blockChance);
        }
    }

    private static void nbt2attrib(NBTTagCompound tag, Attrib attrib) {
        if (attrib.globalId <= 0) {
            attrib.setActive(tag.getBoolean("active"));
            attrib.level = tag.getInt("level");
            attrib.points = tag.getInt("points");
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
            attrib.dodgeX = tag.getDouble("dodgeX");
            attrib.dodgeZ = tag.getDouble("dodgeZ");
            attrib.suckRatio = tag.getFloat("suckRatio");
            attrib.fireChance = tag.getFloat("fireChance");
            attrib.freezeChance = tag.getFloat("freezeChance");
            attrib.blockChance = tag.getFloat("blockChance");
        }
    }
}
