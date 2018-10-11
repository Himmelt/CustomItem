package org.soraworld.csitem;

import org.soraworld.csitem.command.CommandCustomItem;
import org.soraworld.csitem.data.ItemAttrib;
import org.soraworld.csitem.listener.EventListener;
import org.soraworld.csitem.manager.PluginManager;
import org.soraworld.violet.Violet;
import org.soraworld.violet.command.SpongeBaseSubs;
import org.soraworld.violet.command.SpongeCommand;
import org.soraworld.violet.manager.SpongeManager;
import org.soraworld.violet.plugin.SpongePlugin;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Plugin(
        id = CustomItem.PLUGIN_ID,
        name = CustomItem.PLUGIN_NAME,
        version = CustomItem.PLUGIN_VERSION,
        description = "CustomItem Plugin for sponge.",
        dependencies = {
                @Dependency(
                        id = Violet.PLUGIN_ID,
                        version = Violet.PLUGIN_VERSION
                )
        }
)
public class CustomItem extends SpongePlugin {

    public static final String PLUGIN_ID = "customitem";
    public static final String PLUGIN_NAME = "CustomItem";
    public static final String PLUGIN_VERSION = "1.0.0";

    @Listener
    public void onInit(GameInitializationEvent event) {
        super.onInit(event);
        DataRegistration.builder()
                .dataClass(ItemAttrib.class)
                .immutableClass(ItemAttrib.Immutable.class)
                .builder(new ItemAttrib.Builder())
                .dataName("ItemAttrib Data")
                .manipulatorId("attrib")
                .buildAndRegister(container);
    }

    protected SpongeManager registerManager(Path path) {
        return new PluginManager(this, path);
    }

    protected List<Object> registerListeners() {
        ArrayList<Object> listeners = new ArrayList<>();
        if (manager instanceof PluginManager) {
            PluginManager manager = (PluginManager) this.manager;
            listeners.add(new EventListener(manager));
        }
        return listeners;
    }

    protected void registerCommands() {
        SpongeCommand command = new SpongeCommand(getId(), manager.defAdminPerm(), false, manager, "csitem", "csi");
        command.extractSub(SpongeBaseSubs.class);
        command.extractSub(CommandCustomItem.class);
        command.setUsage("/csitem ....");
        register(this, command);
    }
}
