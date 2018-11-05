package org.soraworld.csitem.manager;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.soraworld.csitem.data.Attrib;
import org.soraworld.csitem.data.ItemAttrib;
import org.soraworld.csitem.data.LvlConfig;
import org.soraworld.csitem.nms.NBTUtil;
import org.soraworld.hocon.node.Setting;
import org.soraworld.violet.manager.SpigotManager;
import org.soraworld.violet.plugin.SpigotPlugin;
import org.soraworld.violet.util.ChatColor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.soraworld.csitem.manager.CSIManager.getGlobal;
import static org.soraworld.csitem.manager.CSIManager.hasGlobal;
import static org.soraworld.csitem.task.PlayerTickTask.createTask;

public class AttribManager extends SpigotManager {

    @Setting(comment = "comment.firstGlobal")
    private boolean firstGlobal = true;
    @Setting(comment = "comment.updateTicks")
    private int updateTicks = 10;
    @Setting(comment = "comment.levels")
    private final HashMap<Integer, LvlConfig> levels = new HashMap<>();
    @Setting(comment = "comment.stubborns")
    private final HashMap<String, ArrayList<String>> stubborns = new HashMap<>();

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
        createTask(player, this, updateTicks);
    }

    public void showAttribInfo(CommandSender sender, int id) {
        Attrib global = getGlobal(id);
        if (global != null) {
            sendKey(sender, "\n" + global.toString());
        } else sendKey(sender, "global.idNotExist");
    }

    public void showAttribInfo(CommandSender sender, String name) {
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

    /* get/create/offer Attrib */

    public ItemAttrib getCreateAttrib(ItemStack stack) {
        NBTTagCompound tag = NBTUtil.getOrCreateTag(stack, "attrib");
        return nbt2attrib(tag, new ItemAttrib());
    }

    public Attrib getAttrib(ItemStack stack) {
        NBTTagCompound tag = NBTUtil.getTag(stack, "attrib");
        if (tag == null) return null;
        int id = tag.getInt("globalId");

        if (id > 0 && hasGlobal(id) && firstGlobal) {
            return getGlobal(id);
        } else return nbt2attrib(tag, new ItemAttrib());
    }

    public static void offerAttrib(ItemStack stack, final Attrib attrib) {
        NBTTagCompound tag = NBTUtil.getOrCreateTag(stack, "attrib");
        tag.setInt("globalId", attrib.globalId);
        tag.setBoolean("active", attrib.isActive());
        tag.setInt("globalId", attrib.globalId);
        tag.setInt("level", attrib.level);
        tag.setInt("points", attrib.points);
        tag.setString("name", attrib.name);
        tag.setFloat("attack", attrib.attack);
        tag.setFloat("manaAttack", attrib.manaAttack);
        tag.setFloat("critChance", attrib.critChance);
        tag.setFloat("critDamage", attrib.critDamage);
        tag.setFloat("walkspeed", attrib.walkspeed);
        tag.setFloat("blockChance", attrib.blockChance);
        tag.setFloat("dodgeChance", attrib.dodgeChance);
        tag.setFloat("dodgeX", ((ItemAttrib) attrib).dodgeX);
        tag.setFloat("dodgeX", ((ItemAttrib) attrib).dodgeZ);
        tag.setFloat("suckRatio", attrib.suckRatio);
        tag.setFloat("fireChance", attrib.fireChance);
        tag.setFloat("freezeChance", attrib.freezeChance);
        tag.setFloat("poisonChance", attrib.poisonChance);
        tag.setFloat("bloodChance", attrib.bloodChance);
    }

    private static ItemAttrib nbt2attrib(NBTTagCompound tag, ItemAttrib attrib) {
        attrib.globalId = tag.getInt("globalId");
        attrib.setActive(tag.getBoolean("active"));
        attrib.level = tag.getInt("level");
        attrib.points = tag.getInt("points");
        attrib.name = tag.getString("name");
        attrib.attack = tag.getFloat("attack");
        attrib.manaAttack = tag.getFloat("manaAttack");
        attrib.critChance = tag.getFloat("critChance");
        attrib.critDamage = tag.getFloat("critDamage");
        attrib.walkspeed = tag.getFloat("walkspeed");
        attrib.blockChance = tag.getFloat("blockChance");
        attrib.dodgeChance = tag.getFloat("dodgeChance");
        attrib.dodgeX = tag.getFloat("dodgeX");
        attrib.dodgeZ = tag.getFloat("dodgeZ");
        attrib.suckRatio = tag.getFloat("suckRatio");
        attrib.fireChance = tag.getFloat("fireChance");
        attrib.freezeChance = tag.getFloat("freezeChance");
        attrib.poisonChance = tag.getFloat("poisonChance");
        attrib.bloodChance = tag.getFloat("bloodChance");
        return attrib;
    }

    public boolean shouldGlobal(ItemAttrib item) {
        return item.globalId > 0 && hasGlobal(item.globalId) && firstGlobal;
    }

    public void activeItem(Attrib item) {
        if (!item.isActive()) {
            int[] split = item.splitPoints();
            LvlConfig lvl = getLevel(item.level);
            item.attack = split[0] * lvl.attack;
            item.manaAttack = split[1] * lvl.manaAttack;
            item.critChance = split[2] * lvl.critChance;
            item.critDamage = split[3] * lvl.critDamage;
            item.walkspeed = split[4] * lvl.walkspeed;
            item.blockChance = split[5] * lvl.blockChance;
            item.dodgeChance = split[6] * lvl.dodgeChance;
            item.suckRatio = split[7] * lvl.suckRatio;
            item.fireChance = split[8] * lvl.fireChance;
            item.freezeChance = split[9] * lvl.freezeChance;
            item.poisonChance = split[10] * lvl.poisonChance;
            item.bloodChance = split[11] * lvl.bloodChance;

            applyStubborn(item);

            item.setActive(true);
        }
    }

    private void applyStubborn(Attrib item) {
        item.stubborns.forEach(s -> {
            List<String> list = this.stubborns.get(s.name);
            if (list != null) list.forEach(name -> item.apply(name, s.time));
        });
    }

    private LvlConfig getLevel(int level) {
        return levels.computeIfAbsent(level, i -> new LvlConfig());
    }
}
