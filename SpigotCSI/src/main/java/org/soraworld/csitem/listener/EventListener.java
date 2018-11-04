package org.soraworld.csitem.listener;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.soraworld.csitem.data.Attrib;
import org.soraworld.csitem.data.ItemAttrib;
import org.soraworld.csitem.data.PlayerAttrib;
import org.soraworld.csitem.manager.AttribManager;

import java.util.Random;

import static org.soraworld.csitem.manager.AttribManager.offerAttrib;

public class EventListener implements Listener {

    private final AttribManager manager;
    private static Random random = new Random(System.currentTimeMillis());

    public EventListener(AttribManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack stack = event.getItem();
        Player player = event.getPlayer();
        if (stack != null && stack.getType() != Material.AIR) {
            Attrib attrib = manager.getAttrib(stack);
            if (attrib != null && !attrib.isGlobal() && !attrib.isActive()) {
                attrib.active();
                offerAttrib(stack, attrib);
                manager.sendKey(player, "itemActivated");
            }
        }
    }

    @EventHandler
    public void onAttackEntity(EntityDamageByEntityEvent event) {
        Entity cause = event.getDamager();
        Entity target = event.getEntity();
        double damage = event.getDamage();
        if (cause instanceof Player) {
            Player attacker = (Player) cause;
            ItemStack stack = attacker.getInventory().getItemInMainHand();
            if (stack != null && stack.getType() != Material.AIR) {
                Attrib attrib = manager.getAttrib(stack);
                if (attrib != null) {
                    if (attrib.critChance > 0 && attrib.critChance > random.nextFloat()) {
                        damage += attrib.critDamage;
                    }
                }
            }
        }
        if (target instanceof Player) {
            Player victim = (Player) target;
            PlayerAttrib attrib = getPlayerAttrib(victim);
            if (attrib.dodgeChance > 0 && attrib.dodgeChance > random.nextFloat()) {
                // TODO check & need cancel event ?
                //victim.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 2, true, false), true);
                // TODO update lastDodge
                victim.setVelocity(dodgeVec(attrib, victim));
                event.setDamage(0.0D);
                return;
            }
            if (attrib.blockChance > 0 && attrib.blockChance > random.nextFloat()) {
                // TODO check & need cancel event ?
                victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 2, true, false), true);
                // TODO update lastBlock
                event.setDamage(0.0D);
                return;
            }
        }
        event.setDamage(damage);
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        manager.createPlayerTask(event.getPlayer());
    }

    @EventHandler
    public void onRespawnPlayer(PlayerRespawnEvent event) {
        manager.createPlayerTask(event.getPlayer());
    }

    private PlayerAttrib getPlayerAttrib(Player player) {
        PlayerAttrib pa = new PlayerAttrib();
        PlayerInventory inv = player.getInventory();
        ItemStack stack;
        for (int i = 0; i < 5; i++) {
            if (i == 0) stack = inv.getItemInMainHand();
            else if (i == 1) stack = inv.getHelmet();
            else if (i == 2) stack = inv.getChestplate();
            else if (i == 3) stack = inv.getLeggings();
            else stack = inv.getBoots();
            if (stack != null && stack.getType() != Material.AIR) {
                Attrib attrib = manager.getAttrib(stack);
                if (attrib != null) pa.append(attrib);
            }
        }
        return pa;
    }

    private static Vector dodgeVec(ItemAttrib item, Player player) {
        Vector d = player.getLocation().getDirection().normalize();
        return new Vector(item.dodgeX * d.getX() + item.dodgeZ * d.getZ(), 0, item.dodgeX * d.getZ() - item.dodgeZ * d.getX());
    }
}
