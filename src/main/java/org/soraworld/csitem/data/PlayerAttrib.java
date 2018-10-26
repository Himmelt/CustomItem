package org.soraworld.csitem.data;

public class PlayerAttrib extends Attrib {
    public void append(Attrib attrib) {
        this.critChance += attrib.critChance;
        this.critDamage += attrib.critDamage;
        this.blockChance += attrib.blockChance;
        this.lastBlock += attrib.lastBlock;
        this.dodgeChance += attrib.dodgeChance;
        this.lastDodge += attrib.lastDodge;
        this.suckRatio += attrib.suckRatio;
        this.fireChance += attrib.fireChance;
        this.freezeChance += attrib.freezeChance;
        this.poisonChance += attrib.poisonChance;
        this.bloodChance += attrib.bloodChance;
    }
}
