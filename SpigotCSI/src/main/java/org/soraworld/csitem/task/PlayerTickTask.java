package org.soraworld.csitem.task;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

import static org.soraworld.violet.nms.Version.v1_12_R1;

public class PlayerTickTask implements Runnable {

    private int taskId = Integer.MAX_VALUE;
    private final Player player;

    static final UUID maxHealthUUID = UUID.fromString("6bea37f2-a767-477e-8386-3fa83a77d34d");
    static final UUID knockResistUUID = UUID.fromString("034d1b02-b3ca-4d7e-a0ef-b96109064c7e");
    static final UUID moveSpeedUUID = UUID.fromString("7b9ce4d5-004d-43cf-8c64-f9dac926baf9");
    static final UUID flySpeedUUID = UUID.fromString("2cb98a07-be95-4922-a1d3-6a592cdca56d");
    static final UUID attackDamageUUID = UUID.fromString("db005974-a296-44f2-b3df-ecfb62bc700b");
    static final UUID attackSpeedUUID = UUID.fromString("535fad40-95e4-42c7-9dfa-a8a240bc5001");
    static final UUID armorUUID = UUID.fromString("16726b57-31a9-4410-b718-b5a114e0f977");
    static final UUID armorToughnessUUID = UUID.fromString("9da0f07b-0633-45d8-bd8a-c212667e372a");
    static final UUID luckUUID = UUID.fromString("ddec09ee-e2a2-4d0a-b8cd-0abc00c22b3b");

    public PlayerTickTask(Player player) {
        this.player = player;
    }

    public static PlayerTickTask createTask(Player player) {
        if (v1_12_R1) return new v1_12_R1_TickTask(player);
        return new PlayerTickTask(player);
    }

    public void setTaskId(int id) {
        this.taskId = id;
    }

    public void run() {
        if (player.isOnline()) {
            final State state = new State();

            // Check ItemInHand
            ItemStack stack = player.getInventory().getItemInMainHand();
            if (stack != null && !stack.getType().equals(Material.AIR)) fetchState(stack, state);

            // Check Armors
            fetchState(player.getEquipment().getHelmet(), state);
            fetchState(player.getEquipment().getChestplate(), state);
            fetchState(player.getEquipment().getLeggings(), state);
            fetchState(player.getEquipment().getBoots(), state);

            updateModifier(state);
        } else Bukkit.getScheduler().cancelTask(taskId);
    }

    private void fetchState(ItemStack stack, State state) {
        // TODO 提取属性
    }

    public void updateModifier(State state) {
    }

    class State {
        double maxHealth = 0.0D;
        double knockResist = 0.0D;
        double moveSpeed = 0.0D;
        double flySpeed = 0.0D;
        double attackDamage = 0.0D;
        double attackSpeed = 0.0D;
        double armor = 0.0D;
        double armorToughness = 0.0D;
        double luck = 0.0D;
    }
}
