package org.soraworld.csitem.command;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.soraworld.csitem.data.Attrib;
import org.soraworld.csitem.manager.AttribManager;
import org.soraworld.csitem.nbt.NBTUtil;
import org.soraworld.violet.command.Args;
import org.soraworld.violet.command.SpigotCommand;
import org.soraworld.violet.command.Sub;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.soraworld.csitem.manager.CSIManager.*;
import static org.soraworld.csitem.nbt.NBTUtil.*;

public final class CommandCSI {

    @Sub(onlyPlayer = true)
    public static void nbt(SpigotCommand self, CommandSender sender, Args args) {
        AttribManager manager = ((AttribManager) self.manager);
        Player player = (Player) sender;
        ItemStack stack = player.getInventory().getItemInMainHand();
        NBTTagCompound tag = getOrCreateTag(stack, "attrib");
        tag.setString("key1", player.getLocation().toString());
        manager.send(player, "key1 set to " + player.getLocation().toString());
    }

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
                if (hasAttrib(id)) {
                    manager.showAttribInfo(sender, id);
                } else manager.sendKey(sender, "global.idNotExist");
            } else if (hasAttrib(first)) {
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
                if (createAttrib(Integer.valueOf(first))) {
                    manager.saveItems();
                    manager.sendKey(sender, "global.createSuccess");
                } else manager.sendKey(sender, "global.alreadyExist");
            } else if (createAttrib(first)) {
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
                Attrib attrib = getOrCreateAttrib(stack);
                if ("GlobalId".equals(Name)) {
                    try {
                        attrib.globalId = Integer.valueOf(args.first());
                        offerAttrib(stack, attrib);
                        manager.sendKey(player, "set" + Name, attrib.globalId);
                        //player.setItemInHand(HandTypes.MAIN_HAND, stack);
                    } catch (NumberFormatException ignored) {
                        manager.sendKey(player, "invalidInt");
                    }
                    return;
                }
                if (attrib.globalId > 0) {
                    Attrib global = getGlobalAttrib(attrib.globalId);
                    if (global != null) {
                        try {
                            int value = Integer.valueOf(args.first());
                            value = value < min ? min : value > max ? max : value;
                            consumer.accept(global, value);
                            manager.saveItems();
                            manager.sendKey(player, "global.set" + Name, value);
                        } catch (NumberFormatException ignored) {
                            manager.sendKey(player, "invalidInt");
                        }
                    } else manager.sendKey(player, "global.idNotExist");
                    return;
                }
                try {
                    int value = Integer.valueOf(args.first());
                    value = value < min ? min : value > max ? max : value;
                    consumer.accept(attrib, value);
                    offerAttrib(stack, attrib);
                    manager.sendKey(player, "set" + Name, value);
                    //player.setItemInHand(HandTypes.MAIN_HAND, stack);
                } catch (NumberFormatException ignored) {
                    manager.sendKey(player, "invalidInt");
                }
            } else {
                Attrib attrib = NBTUtil.getAttrib(stack);
                if (attrib != null) {
                    if (attrib.globalId > 0) {
                        Attrib global = getGlobalAttrib(attrib.globalId);
                        if (global != null) {
                            manager.sendKey(player, "global.get" + Name, fun.apply(global));
                        } else manager.sendKey(player, "idNotExist");
                    } else manager.sendKey(player, "get" + Name, fun.apply(attrib));
                } else manager.sendKey(player, "noAttrib");
            }
        } else manager.sendKey(player, "emptyHand");
    }

    private static void getSetFloat(AttribManager manager, Player player, Args args, String Name, float min, float max, BiConsumer<Attrib, Float> consumer, Function<Attrib, Float> fun) {
        ItemStack stack = player.getInventory().getItemInMainHand();
        if (stack != null && stack.getType() != Material.AIR) {
            if (args.notEmpty()) {
                Attrib attrib = getOrCreateAttrib(stack);
                if (attrib.globalId > 0) {
                    Attrib global = getGlobalAttrib(attrib.globalId);
                    if (global != null) {
                        try {
                            float value = Float.valueOf(args.first());
                            value = value < min ? min : value > max ? max : value;
                            consumer.accept(global, value);
                            manager.saveItems();
                            manager.sendKey(player, "global.set" + Name, value);
                        } catch (NumberFormatException ignored) {
                            manager.sendKey(player, "invalidInt");
                        }
                    } else manager.sendKey(player, "idNotExist");
                } else {
                    try {
                        float value = Float.valueOf(args.first());
                        value = value < min ? min : value > max ? max : value;
                        consumer.accept(attrib, value);
                        offerAttrib(stack, attrib);
                        manager.sendKey(player, "set" + Name, value);
                        //player.setItemInHand(HandTypes.MAIN_HAND, stack);
                    } catch (NumberFormatException ignored) {
                        manager.sendKey(player, "invalidFloat");
                    }
                }
            } else {
                Attrib attrib = NBTUtil.getAttrib(stack);
                if (attrib != null) {
                    if (attrib.globalId > 0) {
                        Attrib global = getGlobalAttrib(attrib.globalId);
                        if (global != null) {
                            manager.sendKey(player, "global.get" + Name, fun.apply(global));
                        } else manager.sendKey(player, "idNotExist");
                    } else manager.sendKey(player, "get" + Name, fun.apply(attrib));

                } else manager.sendKey(player, "noAttrib");
            }
        } else manager.sendKey(player, "emptyHand");
    }
}
