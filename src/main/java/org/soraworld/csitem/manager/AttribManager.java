package org.soraworld.csitem.manager;

import org.soraworld.csitem.data.Attrib;
import org.soraworld.hocon.node.FileNode;
import org.soraworld.hocon.node.Setting;
import org.soraworld.violet.manager.SpongeManager;
import org.soraworld.violet.plugin.SpongePlugin;
import org.soraworld.violet.util.ChatColor;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.soraworld.csitem.data.Attrib.deserialize;
import static org.soraworld.csitem.data.Attrib.serialize;

public class AttribManager extends SpongeManager {

    @Setting(comment = "comment.firstGlobal")
    private boolean firstGlobal = true;

    private final Path itemsFile;
    private static HashMap<String, Integer> names = new HashMap<>();
    private static HashMap<Integer, Attrib> items = new HashMap<>();

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
            for (String key : node.keys()) {
                try {
                    int id = Integer.valueOf(key);
                    Attrib attrib = deserialize(node.get(key), id);
                    if (attrib != null) {
                        items.put(attrib.globalId, attrib);
                        if (attrib.name != null && !attrib.name.isEmpty()) {
                            names.put(attrib.name, attrib.globalId);
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
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
}
