package org.soraworld.csitem.manager;

import org.soraworld.csitem.data.Attrib;
import org.soraworld.hocon.node.FileNode;
import org.soraworld.hocon.node.Options;

import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

import static org.soraworld.csitem.data.ItemAttrib.deserialize;
import static org.soraworld.csitem.data.ItemAttrib.serialize;

public class CSIManager {

    private final Path itemsFile;
    private final Options options;

    private static final TreeMap<String, Integer> names = new TreeMap<>();
    static final TreeMap<Integer, Attrib> items = new TreeMap<>();
    private static int NEXT_ID = 1;

    public CSIManager(Path file, Options options) {
        this.itemsFile = file;
        this.options = options;
    }

    public void loadItems() {
        FileNode node = new FileNode(itemsFile.toFile(), options);
        try {
            node.load(false);
            items.clear();
            int maxId = 0;
            for (String key : node.keys()) {
                try {
                    int id = Integer.valueOf(key);
                    if (id <= 0) continue;
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
            //e.printStackTrace();
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
            //e.printStackTrace();
        }
    }

    public static boolean hasGlobal(int id) {
        return items.containsKey(id);
    }

    public static boolean hasGlobal(String name) {
        return names.containsKey(name) && items.containsKey(names.get(name));
    }

    public static Attrib getGlobal(int id) {
        return items.get(id);
    }

    public static Attrib getGlobal(String name) {
        return items.get(names.getOrDefault(name, -1));
    }

/*
    public static Attrib getCreateGlobal(int id) {
        if (id <= 0) return null;
        return items.computeIfAbsent(id, Attrib::new);
    }

    public static Attrib getCreateGlobal(String name) {
        return getCreateGlobal(names.computeIfAbsent(name, s -> NEXT_ID++), name);
    }

    private static Attrib getCreateGlobal(int id, String name) {
        if (id <= 0) return null;
        return items.computeIfAbsent(id, i -> new Attrib(i, name));
    }
*/

    public static boolean createGlobal(int id) {
        if (id <= 0) return false;
        return createGlobal(id, null);
    }

    /**
     * 根据名字创建全局物品.
     *
     * @param name 名称 非空
     * @return 是否成功
     */
    public static boolean createGlobal(String name) {
        //if (name == null || name.isEmpty()) return false;
        if (!names.containsKey(name)) {
            names.put(name, NEXT_ID);
            return createGlobal(NEXT_ID++, name);
        }
        return createGlobal(names.get(name), name);
    }

    private static boolean createGlobal(int id, String name) {
        if (id <= 0) return false;
        if (!items.containsKey(id)) {
            items.put(id, new Attrib(id, name));
            return true;
        }
        return false;
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
}
