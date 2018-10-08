package org.soraworld.csitem.manager;

import org.soraworld.violet.manager.SpongeManager;
import org.soraworld.violet.plugin.SpongePlugin;
import org.soraworld.violet.util.ChatColor;

import java.nio.file.Path;

public class PluginManager extends SpongeManager {
    public PluginManager(SpongePlugin plugin, Path path) {
        super(plugin, path);
    }

    public ChatColor defChatColor() {
        return ChatColor.AQUA;
    }
}
