package org.soraworld.csitem.manager;

import org.soraworld.csitem.task.PlayerTickTask;
import org.soraworld.hocon.node.Setting;
import org.soraworld.violet.manager.SpongeManager;
import org.soraworld.violet.plugin.SpongePlugin;
import org.soraworld.violet.util.ChatColor;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

import java.nio.file.Path;

import static org.soraworld.csitem.CustomItems.PLUGIN_ID;

public class AttribManager extends SpongeManager {

    @Setting(comment = "comment.firstGlobal")
    private boolean firstGlobal = true;
    @Setting(comment = "comment.updateTicks")
    private int updateTicks = 10;

    private final CSIManager csi;

    public AttribManager(SpongePlugin plugin, Path path) {
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
        Sponge.getScheduler().getScheduledTasks(plugin).forEach(Task::cancel);
        for (Player player : Sponge.getServer().getOnlinePlayers()) {
            createPlayerTask(player);
        }
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
        Task.builder()
                .execute(new PlayerTickTask(player))
                .delayTicks(updateTicks)
                .intervalTicks(updateTicks)
                .name(PLUGIN_ID + "-" + player.getName())
                .submit(plugin);
    }

    public void showAttribInfo(CommandSource sender, int id) {
        // TODO show info
    }

    public void showAttribInfo(CommandSource sender, String name) {
        // TODO show info
    }
}
