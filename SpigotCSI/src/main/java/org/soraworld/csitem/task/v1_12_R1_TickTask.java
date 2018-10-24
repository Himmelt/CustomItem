package org.soraworld.csitem.task;

import net.minecraft.server.v1_12_R1.AttributeInstance;
import net.minecraft.server.v1_12_R1.AttributeModifier;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

class v1_12_R1_TickTask extends PlayerTickTask {

    private final AttributeInstance MAX_HEALTH;
    private final AttributeInstance KNOCK_RESIST;
    private final AttributeInstance MOVE_SPEED;
    private final AttributeInstance FLY_SPEED;
    private final AttributeInstance ATTACK_DAMAGE;
    private final AttributeInstance ATTACK_SPEED;
    private final AttributeInstance ARMOR;
    private final AttributeInstance ARMOR_TOUGHNESS;
    private final AttributeInstance LUCK;


    v1_12_R1_TickTask(Player player) {
        super(player);
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        MAX_HEALTH = handle.getAttributeInstance(GenericAttributes.maxHealth);
        KNOCK_RESIST = handle.getAttributeInstance(GenericAttributes.c);// knockbackResistance
        MOVE_SPEED = handle.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
        FLY_SPEED = handle.getAttributeInstance(GenericAttributes.e);// flyingSpeed
        ATTACK_DAMAGE = handle.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE);// attackDamage
        ATTACK_SPEED = handle.getAttributeInstance(GenericAttributes.g);// attackSpeed
        ARMOR = handle.getAttributeInstance(GenericAttributes.h);// armor
        ARMOR_TOUGHNESS = handle.getAttributeInstance(GenericAttributes.i);// armorToughness
        LUCK = handle.getAttributeInstance(GenericAttributes.j);// luck
    }

    public void updateModifier(State state) {
        MAX_HEALTH.b(maxHealthUUID);// remove
        KNOCK_RESIST.b(knockResistUUID);
        MOVE_SPEED.b(moveSpeedUUID);
        FLY_SPEED.b(flySpeedUUID);
        ATTACK_DAMAGE.b(attackDamageUUID);
        ATTACK_SPEED.b(attackSpeedUUID);
        ARMOR.b(armorUUID);
        ARMOR_TOUGHNESS.b(armorToughnessUUID);
        LUCK.b(luckUUID);

        // apply
        if (state.maxHealth != 0) MAX_HEALTH.b(new AttributeModifier(maxHealthUUID, "maxHealth", state.maxHealth, 0));
        if (state.knockResist != 0) KNOCK_RESIST.b(new AttributeModifier(knockResistUUID, "knockResist", state.knockResist, 0));
        if (state.moveSpeed != 0) MOVE_SPEED.b(new AttributeModifier(moveSpeedUUID, "moveSpeed", state.moveSpeed, 0));
        if (state.flySpeed != 0) FLY_SPEED.b(new AttributeModifier(flySpeedUUID, "flySpeed", state.flySpeed, 0));
        if (state.attackDamage != 0) ATTACK_DAMAGE.b(new AttributeModifier(attackDamageUUID, "attackDamage", state.attackDamage, 0));
        if (state.attackSpeed != 0) ATTACK_SPEED.b(new AttributeModifier(attackSpeedUUID, "attackSpeed", state.attackSpeed, 0));
        if (state.armor != 0) ARMOR.b(new AttributeModifier(armorUUID, "armor", state.armor, 0));
        if (state.armorToughness != 0) ARMOR_TOUGHNESS.b(new AttributeModifier(armorToughnessUUID, "armorToughness", state.armorToughness, 0));
        if (state.luck != 0) LUCK.b(new AttributeModifier(luckUUID, "luck", state.luck, 0));
    }
}
