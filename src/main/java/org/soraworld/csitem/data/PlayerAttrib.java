package org.soraworld.csitem.data;

public class PlayerAttrib extends ItemAttrib {
    public void append(Attrib attrib) {
        this.critChance += attrib.critChance;
        this.critDamage += attrib.critDamage;
        this.blockChance += attrib.blockChance;
        // TODO fix
        //this.lastBlock += attrib.lastBlock;
        this.dodgeChance += attrib.dodgeChance;
        // TODO fix
        //this.lastDodge += attrib.lastDodge;
        this.dodgeX += attrib.dodgeX;
        this.dodgeZ += attrib.dodgeZ;
        this.suckRatio += attrib.suckRatio;
        this.fireChance += attrib.fireChance;
        this.freezeChance += attrib.freezeChance;
        this.poisonChance += attrib.poisonChance;
        this.bloodChance += attrib.bloodChance;
    }
}
