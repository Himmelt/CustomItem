package org.soraworld.csitem.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.soraworld.csitem.data.Attrib;
import org.soraworld.csitem.data.ItemAttrib;
import org.soraworld.csitem.manager.AttribManager;
import org.soraworld.violet.command.Args;
import org.soraworld.violet.command.SpigotCommand;
import org.soraworld.violet.command.Sub;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.soraworld.csitem.manager.AttribManager.offerAttrib;
import static org.soraworld.csitem.manager.CSIManager.*;
import static org.soraworld.csitem.nms.ItemUtil.createItemStack;

public final class CommandCSI {

    @Sub(path = "global", virtual = true, perm = "admin", aliases = {"g"}, tabs = {"id", "create", "remove"})
    public static void global(SpigotCommand self, CommandSender sender, Args args) {
    }

    @Sub(path = "global.id", perm = "admin", onlyPlayer = true, usage = "/csi global id [id]")
    public static void global_id(SpigotCommand self, CommandSender sender, Args args) {
        getSetInt(
                (AttribManager) self.manager,
                (Player) sender,
                args, "GlobalId",
                0, Integer.MAX_VALUE,
                (attrib, value) -> attrib.globalId = value,
                attrib -> attrib.globalId
        );
    }

    @Sub(path = "global.info", perm = "admin", usage = "/csi global info <id|name>")
    public static void global_info(SpigotCommand self, CommandSender sender, Args args) {
        AttribManager manager = ((AttribManager) self.manager);
        if (args.notEmpty()) {
            String first = args.first();
            if (first.matches("\\d+")) {
                int id = Integer.valueOf(first);
                if (hasGlobal(id)) {
                    manager.showAttribInfo(sender, id);
                } else manager.sendKey(sender, "global.idNotExist");
            } else if (hasGlobal(first)) {
                manager.showAttribInfo(sender, first);
            } else manager.sendKey(sender, "global.idNotExist");
        } else manager.sendKey(sender, "emptyArgs");
    }

    @Sub(path = "global.create", perm = "admin", usage = "/csi global create <name|id>")
    public static void global_create(SpigotCommand self, CommandSender sender, Args args) {
        AttribManager manager = ((AttribManager) self.manager);
        if (args.notEmpty()) {
            String first = args.first();
            if (first.matches("\\d+")) {
                if (createGlobal(Integer.valueOf(first))) {
                    manager.saveItems();
                    manager.sendKey(sender, "global.createSuccess");
                } else manager.sendKey(sender, "global.alreadyExist");
            } else if (createGlobal(first)) {
                manager.saveItems();
                manager.sendKey(sender, "global.createSuccess");
            } else manager.sendKey(sender, "global.alreadyExist");
        } else manager.sendKey(sender, "emptyArgs");
    }

    @Sub(path = "global.remove", perm = "admin", usage = "/csi global remove <name|id>")
    public static void global_remove(SpigotCommand self, CommandSender sender, Args args) {
        AttribManager manager = ((AttribManager) self.manager);
        if (args.notEmpty()) {
            String first = args.first();
            if (first.matches("\\d+")) {
                if (removeAttrib(Integer.valueOf(first))) {
                    manager.saveItems();
                    manager.sendKey(sender, "global.removeSuccess");
                } else manager.sendKey(sender, "global.idNotExist");
            } else if (removeAttrib(first)) {
                manager.saveItems();
                manager.sendKey(sender, "global.removeSuccess");
            } else manager.sendKey(sender, "global.idNotExist");
        } else manager.sendKey(sender, "emptyArgs");
    }

    /**
     * 给与指定id的物品(全局).
     */
    @Sub(path = "global.give", perm = "admin", usage = "/csi give <player|@p> <id|name> <item id|name> [amount] [damage]")
    public static void global_give(SpigotCommand self, CommandSender sender, Args args) {
        give(self, (AttribManager) self.manager, sender, args, true);
    }

    private static void give(SpigotCommand cmd, AttribManager manager, CommandSender sender, Args args, boolean global) {
        if (args.size() >= 3) {
            String target = args.first();
            Player player = Bukkit.getPlayer(target);
            if (player != null) {
                String attribId = args.get(1);
                Attrib attrib = attribId.matches("\\d+") ? getGlobal(Integer.valueOf(attribId)) : getGlobal(attribId);
                if (attrib != null) {
                    String itemId = args.get(2);
                    int amount = 1, damage = 0;
                    if (args.size() >= 4 && args.get(3).matches("\\d+")) amount = Integer.valueOf(args.get(3));
                    if (args.size() >= 5 && args.get(4).matches("\\d+")) damage = Integer.valueOf(args.get(4));
                    amount = amount <= 0 ? 1 : amount;
                    damage = damage < 0 ? 0 : damage > 15 ? 15 : damage;
                    ItemStack stack = itemId.matches("\\d+") ? createItemStack(Integer.valueOf(itemId), amount, damage) : createItemStack(itemId, amount, damage);
                    offerAttrib(stack, global ? attrib : attrib.toLocal());
                    player.getInventory().addItem(stack);
                    manager.sendKey(player, "giveSuccess", player.getName());
                } else manager.sendKey(sender, "global.idNotExist");
            } else manager.sendKey(sender, "playerIsOffline", target);
        } else cmd.sendUsage(sender);
    }

    /**
     * 列出全局物品的id和名字，等级和品质(点数).
     */
    @Sub(perm = "admin", usage = "/csi list [page]")
    public static void list(SpigotCommand self, CommandSender sender, Args args) {
        AttribManager manager = ((AttribManager) self.manager);
        int page = (args.empty() || !args.first().matches("\\d+")) ? 1 : Integer.valueOf(args.first());
        manager.listItems(sender, page);
    }

    /**
     * 给与指定id的物品(非全局).
     */
    @Sub(perm = "admin", usage = "/csi give <player|@p> <id|name> <item id|name> [amount] [damage]")
    public static void give(SpigotCommand self, CommandSender sender, Args args) {
        give(self, (AttribManager) self.manager, sender, args, false);
    }

    @Sub(perm = "admin", onlyPlayer = true, usage = "/csi attack [damage]")
    public static void attack(SpigotCommand self, CommandSender sender, Args args) {
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
    public static void manaAttack(SpigotCommand self, CommandSender sender, Args args) {
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
    public static void critChance(SpigotCommand self, CommandSender sender, Args args) {
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
    public static void critDamage(SpigotCommand self, CommandSender sender, Args args) {
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
    public static void walkspeed(SpigotCommand self, CommandSender sender, Args args) {
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
    public static void block(SpigotCommand self, CommandSender sender, Args args) {
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
    public static void dodge(SpigotCommand self, CommandSender sender, Args args) {
        getSetInt(
                (AttribManager) self.manager,
                (Player) sender,
                args, "DodgeChance",
                0, 100,
                (attrib, value) -> attrib.dodgeChance = value / 100.0F,
                attrib -> (int) (attrib.dodgeChance * 100)
        );
    }

    @Sub(perm = "admin", onlyPlayer = true, usage = "/csi dodgex x")
    public static void dodgex(SpigotCommand self, CommandSender sender, Args args) {
        getSetDouble(
                (AttribManager) self.manager,
                (Player) sender,
                args, "DodgeX",
                0, 100,
                (attrib, value) -> attrib.dodgeX = value,
                attrib -> attrib.dodgeX
        );
    }

    @Sub(perm = "admin", onlyPlayer = true, usage = "/csi dodgez z")
    public static void dodgez(SpigotCommand self, CommandSender sender, Args args) {
        getSetDouble(
                (AttribManager) self.manager,
                (Player) sender,
                args, "DodgeZ",
                0, 100,
                (attrib, value) -> attrib.dodgeZ = value,
                attrib -> attrib.dodgeZ
        );
    }

    @Sub(perm = "admin", onlyPlayer = true, usage = "/csi suck [ratio%]")
    public static void suck(SpigotCommand self, CommandSender sender, Args args) {
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
    public static void fire(SpigotCommand self, CommandSender sender, Args args) {
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
    public static void freeze(SpigotCommand self, CommandSender sender, Args args) {
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
    public static void poison(SpigotCommand self, CommandSender sender, Args args) {
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
    public static void blood(SpigotCommand self, CommandSender sender, Args args) {
        getSetInt(
                (AttribManager) self.manager,
                (Player) sender,
                args, "BloodChance",
                0, 100,
                (attrib, value) -> attrib.bloodChance = value / 100.0F,
                attrib -> (int) (attrib.bloodChance * 100)
        );
    }

    private static void getSetInt(AttribManager manager, Player player, Args args, String Name, int min, int max, BiConsumer<Attrib, Integer> consumer, Function<Attrib, Integer> fun) {
        ItemStack stack = player.getInventory().getItemInMainHand();
        if (stack != null && stack.getType() != Material.AIR) {
            if (args.notEmpty()) {
                ItemAttrib item = manager.getCreateAttrib(stack);
                if ("GlobalId".equals(Name)) {
                    try {
                        item.globalId = Integer.valueOf(args.first());
                        offerAttrib(stack, item);
                        manager.sendKey(player, "set" + Name, item.globalId);
                    } catch (NumberFormatException ignored) {
                        manager.sendKey(player, "invalidInt");
                    }
                    return;
                }
                Attrib attrib = manager.shouldGlobal(item) ? item.getGlobal() : item;
                try {
                    int value = Integer.valueOf(args.first());
                    value = value < min ? min : value > max ? max : value;
                    consumer.accept(attrib, value);
                    if (attrib.isGlobal()) {
                        manager.saveItems();
                        manager.sendKey(player, "global.set" + Name, value);
                    } else {
                        offerAttrib(stack, attrib);
                        manager.sendKey(player, "set" + Name, value);
                    }
                } catch (NumberFormatException ignored) {
                    manager.sendKey(player, "invalidInt");
                }
            } else {
                Attrib attrib = manager.getAttrib(stack);
                if (attrib != null) {
                    manager.sendKey(player, (attrib.isGlobal() ? "global.get" : "get") + Name, fun.apply(attrib));
                } else manager.sendKey(player, "noAttrib");
            }
        } else manager.sendKey(player, "emptyHand");
    }

    private static void getSetFloat(AttribManager manager, Player player, Args args, String Name, float min, float max, BiConsumer<Attrib, Float> consumer, Function<Attrib, Float> fun) {
        ItemStack stack = player.getInventory().getItemInMainHand();
        if (stack != null && stack.getType() != Material.AIR) {
            if (args.notEmpty()) {
                ItemAttrib item = manager.getCreateAttrib(stack);
                Attrib attrib = manager.shouldGlobal(item) ? item.getGlobal() : item;
                try {
                    float value = Float.valueOf(args.first());
                    value = value < min ? min : value > max ? max : value;
                    consumer.accept(attrib, value);
                    if (attrib.isGlobal()) {
                        manager.saveItems();
                        manager.sendKey(player, "global.set" + Name, value);
                    } else {
                        offerAttrib(stack, attrib);
                        manager.sendKey(player, "set" + Name, value);
                    }
                } catch (NumberFormatException ignored) {
                    manager.sendKey(player, "invalidFloat");
                }
            } else {
                Attrib attrib = manager.getAttrib(stack);
                if (attrib != null) {
                    manager.sendKey(player, (attrib.isGlobal() ? "global.get" : "get") + Name, fun.apply(attrib));
                } else manager.sendKey(player, "noAttrib");
            }
        } else manager.sendKey(player, "emptyHand");
    }

    private static void getSetDouble(AttribManager manager, Player player, Args args, String Name, double min, double max, BiConsumer<Attrib, Double> consumer, Function<Attrib, Double> fun) {
        ItemStack stack = player.getInventory().getItemInMainHand();
        if (stack != null && stack.getType() != Material.AIR) {
            if (args.notEmpty()) {
                ItemAttrib item = manager.getCreateAttrib(stack);
                Attrib attrib = manager.shouldGlobal(item) ? item.getGlobal() : item;
                try {
                    double value = Double.valueOf(args.first());
                    value = value < min ? min : value > max ? max : value;
                    consumer.accept(attrib, value);
                    if (attrib.isGlobal()) {
                        manager.saveItems();
                        manager.sendKey(player, "global.set" + Name, value);
                    } else {
                        offerAttrib(stack, attrib);
                        manager.sendKey(player, "set" + Name, value);
                    }
                } catch (NumberFormatException ignored) {
                    manager.sendKey(player, "invalidDouble");
                }
            } else {
                Attrib attrib = manager.getAttrib(stack);
                if (attrib != null) {
                    manager.sendKey(player, (attrib.isGlobal() ? "global.get" : "get") + Name, fun.apply(attrib));
                } else manager.sendKey(player, "noAttrib");
            }
        } else manager.sendKey(player, "emptyHand");
    }
}
