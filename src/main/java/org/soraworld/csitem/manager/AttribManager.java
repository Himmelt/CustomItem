package org.soraworld.csitem.manager;

import org.soraworld.csitem.data.Attrib;
import org.soraworld.csitem.task.PlayerTask;
import org.soraworld.hocon.node.FileNode;
import org.soraworld.hocon.node.Setting;
import org.soraworld.violet.manager.SpongeManager;
import org.soraworld.violet.plugin.SpongePlugin;
import org.soraworld.violet.util.ChatColor;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.soraworld.csitem.CustomItem.PLUGIN_ID;
import static org.soraworld.csitem.data.Attrib.deserialize;
import static org.soraworld.csitem.data.Attrib.serialize;

public class AttribManager extends SpongeManager {

    @Setting(comment = "comment.firstGlobal")
    private boolean firstGlobal = true;
    @Setting(comment = "comment.updateTicks")
    private byte updateTicks = 10;

    private final Path itemsFile;
    private static HashMap<String, Integer> names = new HashMap<>();
    private static HashMap<Integer, Attrib> items = new HashMap<>();
    private static int NEXT_ID = 0;

    public AttribManager(SpongePlugin plugin, Path path) {
        super(plugin, path);
        itemsFile = path.resolve("globalItems.conf");
    }

    public boolean load() {
        loadItems();
        return super.load();
    }

    public boolean save() {
        saveItems();
        return super.save();
    }

    public ChatColor defChatColor() {
        return ChatColor.AQUA;
    }

    public void loadItems() {
        FileNode node = new FileNode(itemsFile.toFile(), options);
        try {
            node.load(false);
            items.clear();
            int maxId = -1;
            for (String key : node.keys()) {
                try {
                    int id = Integer.valueOf(key);
                    Attrib attrib = deserialize(node.get(key), id);
                    if (attrib != null) {
                        items.put(attrib.globalId, attrib);
                        if (attrib.globalId > maxId) maxId = attrib.globalId;
                        if (attrib.name != null && !attrib.name.isEmpty()) {
                            names.put(attrib.name, attrib.globalId);
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            NEXT_ID = maxId + 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveItems() {
        FileNode node = new FileNode(itemsFile.toFile(), options);
        for (Map.Entry<Integer, Attrib> entry : items.entrySet()) {
            node.set(entry.getKey().toString(), serialize(entry.getValue(), options));
        }
        try {
            node.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createPlayerTask(Player player) {
        Task.builder()
                .execute(new PlayerTask(player))
                .delayTicks(updateTicks)
                .intervalTicks(updateTicks)
                .name(PLUGIN_ID + "-" + player.getName())
                .submit(plugin);
    }

    public static boolean hasAttrib(int id) {
        return items.containsKey(id);
    }

    public static boolean hasAttrib(String name) {
        return names.containsKey(name) && items.containsKey(names.get(name));
    }

    public static Attrib getAttrib(int globalId) {
        return items.get(globalId);
    }

    public static Attrib getOrCreate(int id) {
        return items.computeIfAbsent(id, Attrib::new);
    }

    public static Attrib getOrCreate(int id, String name) {
        return items.computeIfAbsent(id, integer -> new Attrib(integer, name));
    }

    public static boolean createAttrib(int id) {
        return createAttrib(id, "");
    }

    public static boolean createAttrib(int id, String name) {
        if (!items.containsKey(id)) {
            items.put(id, new Attrib(id, name));
            return true;
        }
        return false;
    }

    public static Attrib getOrCreate(String name) {
        return getOrCreate(names.computeIfAbsent(name, s -> NEXT_ID++), name);
    }

    public static boolean createAttrib(String name) {
        if (!names.containsKey(name)) {
            names.put(name, NEXT_ID);
            return createAttrib(NEXT_ID++, name);
        }
        return createAttrib(names.get(name), name);
    }

    public static boolean removeAttrib(int id) {
        if (items.containsKey(id)) {
            Attrib attrib = items.remove(id);
            names.remove(attrib.name);
            return true;
        }
        return false;
    }

    public static boolean removeAttrib(String name) {
        if (names.containsKey(name)) {
            items.remove(names.remove(name));
            return true;
        }
        return false;
    }

    public void showAttribInfo(CommandSource sender, int id) {
        // TODO show info
    }

    public void showAttribInfo(CommandSource sender, String name) {
        // TODO show info
    }
}
