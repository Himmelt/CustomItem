package org.soraworld.csitem.command;

import org.soraworld.csitem.data.ItemAttrib;
import org.soraworld.csitem.manager.AttribManager;
import org.soraworld.violet.command.Args;
import org.soraworld.violet.command.SpongeCommand;
import org.soraworld.violet.command.Sub;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.function.BiConsumer;
import java.util.function.Function;

public final class CommandCustomItem {

    @Sub
    public static void test(SpongeCommand self, CommandSource sender, Args args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.getItemInHand(HandTypes.MAIN_HAND).ifPresent(stack -> {
                stack.getOrCreate(ItemAttrib.class).ifPresent(attrib -> {
                    if (args.notEmpty()) attrib.name = args.first();
                    stack.offer(attrib);
                    player.sendMessage(Text.of(attrib));
                    player.sendMessage(Text.of("content-version:" + attrib.getContentVersion()));
                    player.setItemInHand(HandTypes.MAIN_HAND, stack);
                });
            });
        }
    }

    @Sub(perm = "admin", onlyPlayer = true, usage = "/csi attack [damage]")
    public static void attack(SpongeCommand self, CommandSource sender, Args args) {
        getSetInt(
                (AttribManager) self.manager,
                (Player) sender,
                args, "Attack",
                0, Integer.MAX_VALUE,
                (attrib, value) -> attrib.attack = value,
                attrib -> attrib.attack
        );
    }

    @Sub(path = "manattack", perm = "admin", onlyPlayer = true, usage = "/csi manattack [damage]")
    public static void manaAttack(SpongeCommand self, CommandSource sender, Args args) {
        getSetInt(
                (AttribManager) self.manager,
                (Player) sender,
                args, "ManaAttack",
                0, Integer.MAX_VALUE,
                (attrib, value) -> attrib.manaAttack = value,
                attrib -> attrib.manaAttack
        );
    }

    @Sub(path = "critchance", perm = "admin", onlyPlayer = true, usage = "/csi critchance [chance%]")
    public static void critChance(SpongeCommand self, CommandSource sender, Args args) {
        getSetInt(
                (AttribManager) self.manager,
                (Player) sender,
                args, "CritChance",
                0, 100,
                (attrib, value) -> attrib.critChance = value / 100.0F,
                attrib -> (int) (attrib.critChance * 100)
        );
    }

    @Sub(path = "critdamage", perm = "admin", onlyPlayer = true, usage = "/csi critdamage [damage]")
    public static void critDamage(SpongeCommand self, CommandSource sender, Args args) {
        getSetFloat(
                (AttribManager) self.manager,
                (Player) sender,
                args, "CritDamage",
                0, Float.MAX_VALUE,
                (attrib, value) -> attrib.critDamage = value,
                attrib -> attrib.critDamage
        );
    }

    /**
     * 行走速度
     * 建议使用位置：全部
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/csi walkspeed [speed]")
    public static void walkspeed(SpongeCommand self, CommandSource sender, Args args) {
        getSetFloat(
                (AttribManager) self.manager,
                (Player) sender,
                args, "WalkSpeed",
                0, 1,
                (attrib, value) -> attrib.walkspeed = value,
                attrib -> attrib.walkspeed
        );
    }

    @Sub(perm = "admin", onlyPlayer = true, usage = "/csi block [chance%]")
    public static void block(SpongeCommand self, CommandSource sender, Args args) {
        getSetInt(
                (AttribManager) self.manager,
                (Player) sender,
                args, "BlockChance",
                0, 100,
                (attrib, value) -> attrib.blockChance = value / 100.0F,
                attrib -> (int) (attrib.blockChance * 100)
        );
    }

    @Sub(perm = "admin", onlyPlayer = true, usage = "/csi dodge [chance%]")
    public static void dodge(SpongeCommand self, CommandSource sender, Args args) {
        getSetInt(
                (AttribManager) self.manager,
                (Player) sender,
                args, "DodgeChance",
                0, 100,
                (attrib, value) -> attrib.dodgeChance = value / 100.0F,
                attrib -> (int) (attrib.dodgeChance * 100)
        );
    }

    @Sub(perm = "admin", onlyPlayer = true, usage = "/csi suck [ratio%]")
    public static void suck(SpongeCommand self, CommandSource sender, Args args) {
        getSetInt(
                (AttribManager) self.manager,
                (Player) sender,
                args, "SuckRatio",
                0, Integer.MAX_VALUE,
                (attrib, value) -> attrib.suckRatio = value / 100.0F,
                attrib -> (int) (attrib.suckRatio * 100)
        );
    }

    @Sub(perm = "admin", onlyPlayer = true, usage = "/csi fire [chance%]")
    public static void fire(SpongeCommand self, CommandSource sender, Args args) {
        getSetInt(
                (AttribManager) self.manager,
                (Player) sender,
                args, "FireChance",
                0, 100,
                (attrib, value) -> attrib.fireChance = value / 100.0F,
                attrib -> (int) (attrib.fireChance * 100)
        );
    }

    @Sub(perm = "admin", onlyPlayer = true, usage = "/csi freeze [chance%]")
    public static void freeze(SpongeCommand self, CommandSource sender, Args args) {
        getSetInt(
                (AttribManager) self.manager,
                (Player) sender,
                args, "FreezeChance",
                0, 100,
                (attrib, value) -> attrib.freezeChance = value / 100.0F,
                attrib -> (int) (attrib.freezeChance * 100)
        );
    }

    @Sub(perm = "admin", onlyPlayer = true, usage = "/csi poison [chance%]")
    public static void poison(SpongeCommand self, CommandSource sender, Args args) {
        getSetInt(
                (AttribManager) self.manager,
                (Player) sender,
                args, "PoisonChance",
                0, 100,
                (attrib, value) -> attrib.poisonChance = value / 100.0F,
                attrib -> (int) (attrib.poisonChance * 100)
        );
    }

    @Sub(perm = "admin", onlyPlayer = true, usage = "/csi blood [chance%]")
    public static void blood(SpongeCommand self, CommandSource sender, Args args) {
        getSetInt(
                (AttribManager) self.manager,
                (Player) sender,
                args, "BloodChance",
                0, 100,
                (attrib, value) -> attrib.bloodChance = value / 100.0F,
                attrib -> (int) (attrib.bloodChance * 100)
        );
    }

    private static void getSetInt(AttribManager manager, Player player, Args args, String Name, int min, int max, BiConsumer<ItemAttrib, Integer> consumer, Function<ItemAttrib, Integer> fun) {
        player.getItemInHand(HandTypes.MAIN_HAND).ifPresent(stack -> {
            // TODO check empty slot air ?
            if (args.notEmpty()) {
                stack.getOrCreate(ItemAttrib.class).ifPresent(attrib -> {
                    try {
                        int value = Integer.valueOf(args.first());
                        value = value < min ? min : value > max ? max : value;
                        consumer.accept(attrib, value);
                        stack.offer(attrib);
                        manager.sendKey(player, "set" + Name, value);
                        player.setItemInHand(HandTypes.MAIN_HAND, stack);
                    } catch (NumberFormatException ignored) {
                        manager.sendKey(player, "invalidInt");
                    }
                });
            } else {
                stack.get(ItemAttrib.class).ifPresent(attrib -> manager.sendKey(player, "get" + Name, fun.apply(attrib)));
                //manager.sendKey(player, "noAttrib");
            }
        });
        //manager.sendKey(player, "emptyHand");
    }

    private static void getSetFloat(AttribManager manager, Player player, Args args, String Name, float min, float max, BiConsumer<ItemAttrib, Float> consumer, Function<ItemAttrib, Float> fun) {
        player.getItemInHand(HandTypes.MAIN_HAND).ifPresent(stack -> {
            // TODO check empty slot air ?
            if (args.notEmpty()) {
                stack.getOrCreate(ItemAttrib.class).ifPresent(attrib -> {
                    try {
                        float value = Float.valueOf(args.first());
                        value = value < min ? min : value > max ? max : value;
                        consumer.accept(attrib, value);
                        stack.offer(attrib);
                        manager.sendKey(player, "set" + Name, value);
                        player.setItemInHand(HandTypes.MAIN_HAND, stack);
                    } catch (NumberFormatException ignored) {
                        manager.sendKey(player, "invalidFloat");
                    }
                });
            } else {
                stack.get(ItemAttrib.class).ifPresent(attrib -> manager.sendKey(player, "get" + Name, fun.apply(attrib)));
                //manager.sendKey(player, "noAttrib");
            }
        });
        //manager.sendKey(player, "emptyHand");
    }
}
