package org.soraworld.csitem.manager;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.soraworld.csitem.data.Attrib;
import org.soraworld.hocon.node.Setting;
import org.soraworld.violet.manager.SpigotManager;
import org.soraworld.violet.plugin.SpigotPlugin;
import org.soraworld.violet.util.ChatColor;

import java.nio.file.Path;

import static org.soraworld.csitem.manager.CSIManager.getGlobalAttrib;
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
        Attrib global = getGlobalAttrib(id);
        if (global != null) {
            sendKey(sender, "\n" + global.toString());
        } else sendKey(sender, "global.idNotExist");
    }

    public void showAttribInfo(CommandSender sender, String name) {
        // TODO show info
        Attrib global = getGlobalAttrib(name);
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
}
