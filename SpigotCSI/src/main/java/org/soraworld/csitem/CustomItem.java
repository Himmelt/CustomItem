package org.soraworld.csitem;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.soraworld.csitem.command.CommandCSI;
import org.soraworld.csitem.listener.EventListener;
import org.soraworld.csitem.manager.AttribManager;
import org.soraworld.violet.command.SpigotBaseSubs;
import org.soraworld.violet.command.SpigotCommand;
import org.soraworld.violet.manager.SpigotManager;
import org.soraworld.violet.plugin.SpigotPlugin;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class CustomItem extends SpigotPlugin {

    protected SpigotManager registerManager(Path path) {
        return new AttribManager(this, path);
    }

    protected List<Listener> registerListeners() {
        return Collections.singletonList(new EventListener((AttribManager) manager));
    }

    protected void registerCommands() {
        SpigotCommand command = new SpigotCommand(getId(), manager.defAdminPerm(), false, manager, "csitem", "csi");
        command.extractSub(SpigotBaseSubs.class);
        command.extractSub(CommandCSI.class);
        command.setUsage("/csitem ....");
        register(this, command);
    }

    public void afterEnable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ((AttribManager) manager).createPlayerTask(player);
        }
    }
}
