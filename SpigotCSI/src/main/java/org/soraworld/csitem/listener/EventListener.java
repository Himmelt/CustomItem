package org.soraworld.csitem.listener;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.soraworld.csitem.data.Attrib;
import org.soraworld.csitem.data.PlayerAttrib;
import org.soraworld.csitem.manager.AttribManager;

import java.util.Random;

import static org.soraworld.csitem.nbt.NBTUtil.*;

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
            Attrib attrib = getOrCreateAttrib(stack);
            if (attrib.globalId > 0) {
                player.sendMessage("using global " + attrib.globalId);
            } else if (!attrib.active) {
                attrib.active = true;
                offerAttrib(stack, attrib);
                //player.setItemInHand(event.getHandType(), stack);
                player.sendMessage("item activated!");
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
    }

    @EventHandler
    public void onAttackEntity(EntityDamageByEntityEvent event) {
        Entity cause = event.getDamager();
        Entity target = event.getEntity();
        /*if (cause instanceof Player) {
            Player attacker = (Player) cause;
            attacker.getItemInHand(HandTypes.MAIN_HAND).ifPresent(stack -> {
                stack.get(ItemAttrib.class).ifPresent(attrib -> {
                    if (attrib.critChance > 0 && attrib.critChance > random.nextFloat()) {
                        event.addDamageModifierAfter(
                                DamageModifier.builder()
                                        .cause(Cause.builder()
                                                .append(manager.getPlugin())
                                                .build(EventContext.empty()))
                                        .item(stack)
                                        .type(DamageModifierTypes.CRITICAL_HIT)
                                        .build(),
                                operand -> attrib.critDamage,
                                Collections.singleton(DamageModifierTypes.CRITICAL_HIT)
                        );
                    }
                });
            });
        }
        if (target instanceof Player) {
            Player victim = (Player) target;
            PlayerAttrib attrib = getPlayerAttrib(victim);
            if (attrib.dodgeChance > 0 && attrib.dodgeChance > random.nextFloat()) {
                // TODO check & need cancel event ?
                victim.getOrCreate(PotionEffectData.class).ifPresent(data -> {
                    // TODO check override exist effects ?
                    data.addElement(PotionEffect.builder()
                            .potionType(PotionEffectTypes.SPEED)
                            .duration(40).amplifier(2).ambience(true).particles(false)
                            .build());
                    victim.offer(data);
                });
                // TODO update lastDodge
                event.setBaseOutputDamage(0);
                return;
            }
            if (attrib.blockChance > 0 && attrib.blockChance > random.nextFloat()) {
                // TODO check & need cancel event ?
                victim.getOrCreate(PotionEffectData.class).ifPresent(data -> {
                    // TODO check override exist effects ?
                    data.addElement(PotionEffect.builder()
                            .potionType(PotionEffectTypes.SLOWNESS)
                            .duration(40).amplifier(2).ambience(true).particles(false)
                            .build());
                    victim.offer(data);
                });
                // TODO update lastBlock
                event.setBaseOutputDamage(0);
            }
        }*/
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        manager.createPlayerTask(event.getPlayer());
    }

    @EventHandler
    public void onRespawnPlayer(PlayerRespawnEvent event) {
        manager.createPlayerTask(event.getPlayer());
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        // TODO 为防止 生成实体 导致丢失 ItemStack 里的自定义NBT
        // TODO 所以 禁止丢弃含有 自定义Tag的 ItemStack
        // TODO 或 在生成实体时，存储ItemStack,捡起来时或传输到容器时再覆盖 ItemStack
        ItemStack stack = event.getItemDrop().getItemStack();

        if (stack != null) {
            event.getPlayer().sendMessage("PlayerDropItemEvent hashcode " + stack.hashCode());
            NBTTagCompound tag = getOrCreateTag(stack, "attrib");
            event.getPlayer().sendMessage("PlayerDropItemEvent has key " + tag.hasKey("key1"));
        }
    }

    @EventHandler
    public void onDestroyChest(EntityPickupItemEvent event) {
        ItemStack stack = event.getItem().getItemStack();
        System.out.println("EntityPickupItemEvent hashcode: " + stack.hashCode());
        NBTTagCompound tag = getOrCreateTag(stack, "attrib");
        System.out.println("EntityPickupItemEvent has key " + tag.hasKey("key1"));
    }

    @EventHandler
    public void onDestroyChest(PlayerPickupItemEvent event) {

    }

    @EventHandler
    public void onDestroyChest(InventoryPickupItemEvent event) {
        ItemStack stack = event.getItem().getItemStack();
        System.out.println("InventoryPickupItemEvent hashcode: " + stack.hashCode());
        NBTTagCompound tag = getOrCreateTag(stack, "attrib");
        System.out.println("InventoryPickupItemEvent has key " + tag.hasKey("key1"));
    }

    @EventHandler
    public void onItemSpawnEvent(ItemSpawnEvent event) {
        ItemStack stack = event.getEntity().getItemStack();
        System.out.println("ItemSpawnEvent hashcode: " + stack.hashCode());
        NBTTagCompound tag = getOrCreateTag(stack, "attrib");
        System.out.println("ItemSpawnEvent has key " + tag.hasKey("key1"));
    }

    @EventHandler
    public void onItemMergeEvent(ItemMergeEvent event) {

    }

    private static PlayerAttrib getPlayerAttrib(Player player) {
        PlayerAttrib pa = new PlayerAttrib();
        /*// TODO positive & negative attributes
        // Check ItemInHand
        player.getItemInHand(HandTypes.MAIN_HAND).ifPresent(stack -> {
            stack.get(ItemAttrib.class).ifPresent(attrib -> {
                pa.critChance = attrib.critChance;
                pa.critDamage = attrib.critDamage;
                pa.blockChance = attrib.blockChance;
                pa.lastBlock = attrib.lastBlock;
                pa.dodgeChance = attrib.dodgeChance;
                pa.lastDodge = attrib.lastDodge;
                pa.suckRatio = attrib.suckRatio;
                pa.fireChance = attrib.fireChance;
                pa.freezeChance = attrib.freezeChance;
                pa.poisonChance = attrib.poisonChance;
                pa.bloodChance = attrib.bloodChance;
            });
        });

        // Check Armors
        player.getHelmet().ifPresent(stack -> {
            // TODO fetch positive attributes state
            stack.get(ItemAttrib.class).ifPresent(attrib -> {
                pa.critChance += attrib.critChance;
                pa.critDamage += attrib.critDamage;
                pa.blockChance += attrib.blockChance;
                pa.lastBlock += attrib.lastBlock;
                pa.dodgeChance += attrib.dodgeChance;
                pa.lastDodge += attrib.lastDodge;
                pa.suckRatio += attrib.suckRatio;
                pa.fireChance += attrib.fireChance;
                pa.freezeChance += attrib.freezeChance;
                pa.poisonChance += attrib.poisonChance;
                pa.bloodChance += attrib.bloodChance;
            });
        });
        player.getChestplate().ifPresent(stack -> {
            // TODO fetch positive attributes state
            stack.get(ItemAttrib.class).ifPresent(attrib -> {
                pa.critChance += attrib.critChance;
                pa.critDamage += attrib.critDamage;
                pa.blockChance += attrib.blockChance;
                pa.lastBlock += attrib.lastBlock;
                pa.dodgeChance += attrib.dodgeChance;
                pa.lastDodge += attrib.lastDodge;
                pa.suckRatio += attrib.suckRatio;
                pa.fireChance += attrib.fireChance;
                pa.freezeChance += attrib.freezeChance;
                pa.poisonChance += attrib.poisonChance;
                pa.bloodChance += attrib.bloodChance;
            });
        });
        player.getLeggings().ifPresent(stack -> {
            // TODO fetch positive attributes state
            stack.get(ItemAttrib.class).ifPresent(attrib -> {
                pa.critChance += attrib.critChance;
                pa.critDamage += attrib.critDamage;
                pa.blockChance += attrib.blockChance;
                pa.lastBlock += attrib.lastBlock;
                pa.dodgeChance += attrib.dodgeChance;
                pa.lastDodge += attrib.lastDodge;
                pa.suckRatio += attrib.suckRatio;
                pa.fireChance += attrib.fireChance;
                pa.freezeChance += attrib.freezeChance;
                pa.poisonChance += attrib.poisonChance;
                pa.bloodChance += attrib.bloodChance;
            });
        });
        player.getBoots().ifPresent(stack -> {
            // TODO fetch positive attributes state
            stack.get(ItemAttrib.class).ifPresent(attrib -> {
                pa.critChance += attrib.critChance;
                pa.critDamage += attrib.critDamage;
                pa.blockChance += attrib.blockChance;
                pa.lastBlock += attrib.lastBlock;
                pa.dodgeChance += attrib.dodgeChance;
                pa.lastDodge += attrib.lastDodge;
                pa.suckRatio += attrib.suckRatio;
                pa.fireChance += attrib.fireChance;
                pa.freezeChance += attrib.freezeChance;
                pa.poisonChance += attrib.poisonChance;
                pa.bloodChance += attrib.bloodChance;
            });
        });*/
        return pa;
    }
}
