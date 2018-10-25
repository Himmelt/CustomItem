package org.soraworld.csitem.listener;

import org.soraworld.csitem.data.ItemAttrib;
import org.soraworld.csitem.data.PlayerAttrib;
import org.soraworld.csitem.manager.AttribManager;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.entity.damage.DamageModifier;
import org.spongepowered.api.event.cause.entity.damage.DamageModifierTypes;
import org.spongepowered.api.event.data.ChangeDataHolderEvent;
import org.spongepowered.api.event.entity.AttackEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;

import java.util.Collections;
import java.util.Random;

public class EventListener {

    private final AttribManager manager;
    private static Random random = new Random(System.currentTimeMillis());

    public EventListener(AttribManager manager) {
        this.manager = manager;
    }

    @Listener
    public void onPlayerInteract(InteractItemEvent.Primary event, @First Player player) {
        player.getItemInHand(event.getHandType()).ifPresent(stack -> {
            stack.get(ItemAttrib.class).ifPresent(attrib -> {
                if (attrib.globalId > 0) {
                    player.sendMessage(Text.of("using global " + attrib.globalId));
                } else if (!attrib.active) {
                    attrib.active = true;
                    stack.offer(attrib);
                    player.setItemInHand(event.getHandType(), stack);
                    player.sendMessage(Text.of("item activated!"));
                }
            });
        });
    }

    @Listener
    public void onInventoryClick(ClickInventoryEvent event) {
    }

    //@Listener
    public void onPlayerItemDamage(ChangeDataHolderEvent.ValueChange event) {

    }

    @Listener
    public void onAttackEntity(AttackEntityEvent event, @Getter("getTargetEntity") Entity target, @First Entity cause) {
        if (cause instanceof Player) {
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
        }
    }

    @Listener
    public void onPlayerLogin(ClientConnectionEvent.Join event) {
        manager.createPlayerTask(event.getTargetEntity());
    }

    @Listener
    public void onRespawnPlayer(RespawnPlayerEvent event) {
        manager.createPlayerTask(event.getTargetEntity());
    }

    private static PlayerAttrib getPlayerAttrib(Player player) {
        PlayerAttrib pa = new PlayerAttrib();
        // TODO positive & negative attributes
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
        });
        return pa;
    }
}
