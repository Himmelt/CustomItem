package org.soraworld.csitem.task;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import org.soraworld.csitem.data.ItemAttrib;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import java.util.UUID;
import java.util.function.Consumer;

public class PlayerTask implements Consumer<Task> {

    private final Player player;
    //private static AttribManager manager;

    private IAttributeInstance MAX_HEALTH;
    private IAttributeInstance KNOCK_RESIST;
    private IAttributeInstance MOVE_SPEED;
    //private IAttributeInstance FLY_SPEED;
    private IAttributeInstance ATTACK_DAMAGE;
    private IAttributeInstance ATTACK_SPEED;
    private IAttributeInstance ARMOR;
    private IAttributeInstance ARMOR_TOUGHNESS;
    private IAttributeInstance LUCK;

    private static final UUID maxHealthUUID = UUID.fromString("6bea37f2-a767-477e-8386-3fa83a77d34d");
    private static final UUID knockResistUUID = UUID.fromString("034d1b02-b3ca-4d7e-a0ef-b96109064c7e");
    private static final UUID moveSpeedUUID = UUID.fromString("7b9ce4d5-004d-43cf-8c64-f9dac926baf9");
    //private static final UUID flySpeedUUID = UUID.fromString("2cb98a07-be95-4922-a1d3-6a592cdca56d");
    private static final UUID attackDamageUUID = UUID.fromString("db005974-a296-44f2-b3df-ecfb62bc700b");
    private static final UUID attackSpeedUUID = UUID.fromString("535fad40-95e4-42c7-9dfa-a8a240bc5001");
    private static final UUID armorUUID = UUID.fromString("16726b57-31a9-4410-b718-b5a114e0f977");
    private static final UUID armorToughnessUUID = UUID.fromString("9da0f07b-0633-45d8-bd8a-c212667e372a");
    private static final UUID luckUUID = UUID.fromString("ddec09ee-e2a2-4d0a-b8cd-0abc00c22b3b");

    public PlayerTask(Player player) {
        this.player = player;
        EntityPlayer mce = (EntityPlayer) player;
        MAX_HEALTH = mce.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
        KNOCK_RESIST = mce.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
        MOVE_SPEED = mce.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        //FLY_SPEED = mce.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED);
        ATTACK_DAMAGE = mce.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        ATTACK_SPEED = mce.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED);
        ARMOR = mce.getEntityAttribute(SharedMonsterAttributes.ARMOR);
        ARMOR_TOUGHNESS = mce.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS);
        LUCK = mce.getEntityAttribute(SharedMonsterAttributes.LUCK);
    }

    public void accept(Task task) {
        player.sendMessage(Text.of(task.getName()));
        if (player.isOnline()) {
            final State state = new State();

            // Check ItemInHand
            player.getItemInHand(HandTypes.MAIN_HAND).ifPresent(stack -> fetchState(stack, state));

            // Check Armors
            player.getHelmet().ifPresent(stack -> fetchState(stack, state));
            player.getChestplate().ifPresent(stack -> fetchState(stack, state));
            player.getLeggings().ifPresent(stack -> fetchState(stack, state));
            player.getBoots().ifPresent(stack -> fetchState(stack, state));

            updateModifier(state);
        } else task.cancel();
    }

    private void fetchState(ItemStack stack, State state) {
        stack.get(ItemAttrib.class).ifPresent(attrib -> {
            state.attackDamage += attrib.attack;
            state.moveSpeed += attrib.walkspeed;
        });
    }

    public void updateModifier(State state) {
        MAX_HEALTH.removeModifier(maxHealthUUID);
        KNOCK_RESIST.removeModifier(knockResistUUID);
        MOVE_SPEED.removeModifier(moveSpeedUUID);
        //FLY_SPEED.removeModifier(flySpeedUUID);
        ATTACK_DAMAGE.removeModifier(attackDamageUUID);
        ATTACK_SPEED.removeModifier(attackSpeedUUID);
        ARMOR.removeModifier(armorUUID);
        ARMOR_TOUGHNESS.removeModifier(armorToughnessUUID);
        LUCK.removeModifier(luckUUID);

        if (state.maxHealth != 0) MAX_HEALTH.applyModifier(new AttributeModifier(maxHealthUUID, "maxHealth", state.maxHealth, 0));
        if (state.knockResist != 0) KNOCK_RESIST.applyModifier(new AttributeModifier(knockResistUUID, "knockResist", state.knockResist, 0));
        if (state.moveSpeed != 0) MOVE_SPEED.applyModifier(new AttributeModifier(moveSpeedUUID, "moveSpeed", state.moveSpeed, 0));
        //if (state.flySpeed != 0) FLY_SPEED.applyModifier(new AttributeModifier(flySpeedUUID, "flySpeed", state.flySpeed, 0));
        if (state.attackDamage != 0) ATTACK_DAMAGE.applyModifier(new AttributeModifier(attackDamageUUID, "attackDamage", state.attackDamage, 0));
        if (state.attackSpeed != 0) ATTACK_SPEED.applyModifier(new AttributeModifier(attackSpeedUUID, "attackSpeed", state.attackSpeed, 0));
        if (state.armor != 0) ARMOR.applyModifier(new AttributeModifier(armorUUID, "armor", state.armor, 0));
        if (state.armorToughness != 0) ARMOR_TOUGHNESS.applyModifier(new AttributeModifier(armorToughnessUUID, "armorToughness", state.armorToughness, 0));
        if (state.luck != 0) LUCK.applyModifier(new AttributeModifier(luckUUID, "luck", state.luck, 0));
    }

    private class State {
        double maxHealth = 0.0D;
        double knockResist = 0.0D;
        double moveSpeed = 0.0D;
        //double flySpeed = 0.0D;
        double attackDamage = 0.0D;
        double attackSpeed = 0.0D;
        double armor = 0.0D;
        double armorToughness = 0.0D;
        double luck = 0.0D;
    }
}
