package org.soraworld.csitem.data;

import org.soraworld.hocon.node.Serializable;
import org.soraworld.hocon.node.Setting;

@Serializable
public class Attrib {
    @Setting
    public String name = "defaultName";
    @Setting
    public int attack = 0;
    @Setting
    public int manaAttack = 0;
    @Setting
    public float critChance = 0, critDamage = 0;
    @Setting
    public float walkSpeed = 0;
    @Setting
    public float blockChance = 0;
    @Setting
    public float dodgeChance = 0;
    @Setting
    public float suckRatio = 0;
    @Setting
    public float fireChance = 0;
    @Setting
    public float freezeChance = 0;
    @Setting
    public float poisonChance = 0;
    @Setting
    public float bloodChance = 0;

    public Attrib() {
    }

    public Attrib(Attrib other) {
        copy(other);
    }

    public void copy(Attrib other) {
        if (other != null) {
            name = other.name;
            attack = other.attack;
            manaAttack = other.manaAttack;
            critChance = other.critChance;
            critDamage = other.critDamage;
            walkSpeed = other.walkSpeed;
            blockChance = other.blockChance;
            dodgeChance = other.dodgeChance;
            suckRatio = other.suckRatio;
            fireChance = other.fireChance;
            freezeChance = other.freezeChance;
            poisonChance = other.poisonChance;
            bloodChance = other.bloodChance;
        } else reset();
    }

    public void reset() {
        attack = 0;
        manaAttack = 0;
        critChance = 0;
        critDamage = 0;
        walkSpeed = 0;
        blockChance = 0;
        dodgeChance = 0;
        suckRatio = 0;
        fireChance = 0;
        freezeChance = 0;
        poisonChance = 0;
        bloodChance = 0;
    }
}
