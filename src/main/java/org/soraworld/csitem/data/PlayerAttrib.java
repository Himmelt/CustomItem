package org.soraworld.csitem.data;

public class PlayerAttrib extends Attrib {
    public void append(Attrib attrib) {
        this.critChance += attrib.critChance;
        this.critDamage += attrib.critDamage;
        this.blockChance += attrib.blockChance;
        this.dodgeChance += attrib.dodgeChance;
        this.dodgeX += attrib.dodgeX;
        this.dodgeZ += attrib.dodgeZ;
        this.suckRatio += attrib.suckRatio;
        this.fireChance += attrib.fireChance;
        this.freezeChance += attrib.freezeChance;
        this.poisonChance += attrib.poisonChance;
        this.bloodChance += attrib.bloodChance;
    }
}
