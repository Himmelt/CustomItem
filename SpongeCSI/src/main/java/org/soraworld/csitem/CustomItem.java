package org.soraworld.csitem;

import org.soraworld.csitem.command.CommandCSI;
import org.soraworld.csitem.data.ItemAttrib;
import org.soraworld.csitem.listener.EventListener;
import org.soraworld.csitem.manager.AttribManager;
import org.soraworld.violet.Violet;
import org.soraworld.violet.command.SpongeBaseSubs;
import org.soraworld.violet.command.SpongeCommand;
import org.soraworld.violet.manager.SpongeManager;
import org.soraworld.violet.plugin.SpongePlugin;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Plugin(
        id = CustomItems.PLUGIN_ID,
        name = CustomItems.PLUGIN_NAME,
        version = CustomItems.PLUGIN_VERSION,
        description = "CustomItem Plugin for sponge.",
        dependencies = {
                @Dependency(
                        id = Violet.PLUGIN_ID,
                        version = Violet.PLUGIN_VERSION
                )
        }
)
public class CustomItem extends SpongePlugin {

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
        return new AttribManager(this, path);
    }

    protected List<Object> registerListeners() {
        ArrayList<Object> listeners = new ArrayList<>();
        if (manager instanceof AttribManager) {
            AttribManager manager = (AttribManager) this.manager;
            listeners.add(new EventListener(manager));
        }
        return listeners;
    }

    protected void registerCommands() {
        SpongeCommand command = new SpongeCommand(getId(), manager.defAdminPerm(), false, manager, "csitem", "csi");
        command.extractSub(SpongeBaseSubs.class);
        command.extractSub(CommandCSI.class);
        command.setUsage("/csitem ....");
        register(this, command);
    }

    public void afterEnable() {
        for (Player player : Sponge.getServer().getOnlinePlayers()) {
            ((AttribManager) manager).createPlayerTask(player);
        }
    }
}
