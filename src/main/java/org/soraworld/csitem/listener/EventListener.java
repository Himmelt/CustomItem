package org.soraworld.csitem.listener;

import org.soraworld.csitem.manager.AttribManager;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.AttackEntityEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class EventListener {

    private final AttribManager manager;

    public EventListener(AttribManager manager) {
        this.manager = manager;
    }

    @Listener
    public void onAttackEntity(AttackEntityEvent event, @Getter("getTargetEntity") Entity target, @First Entity cause) {
        System.out.println("Attack:" + cause + " Target:" + target);
    }

    @Listener
    public void onPlayerLogin(ClientConnectionEvent.Join event) {
        manager.createPlayerTask(event.getTargetEntity());
    }
}
