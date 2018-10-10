package org.soraworld.csitem.listener;

import org.soraworld.csitem.manager.PluginManager;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.AttackEntityEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.cause.First;

public class EventListener {

    private final PluginManager manager;

    public EventListener(PluginManager manager) {
        this.manager = manager;
    }

    @Listener
    public void onAttackEntity(AttackEntityEvent event, @Getter("getTargetEntity") Entity target, @First Entity cause) {
        System.out.println("Attack:" + cause + " Target:" + target);
    }
}
