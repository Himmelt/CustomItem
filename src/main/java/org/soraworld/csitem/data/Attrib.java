package org.soraworld.csitem.data;

import org.soraworld.hocon.node.*;

@Serializable
public class Attrib {

    public int globalId = -1;
    public boolean active = false;

    @Setting
    public String name = "";
    @Setting
    public int attack = 0;
    @Setting
    public int manaAttack = 0;
    @Setting
    public float critChance = 0, critDamage = 0;
    @Setting
    public float walkspeed = 0;
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

    public Attrib(int id) {
        globalId = id;
    }

    public Attrib(Attrib other) {
        copy(other);
    }

    public void copy(Attrib old) {
        if (old != null) {
            globalId = old.globalId;
            active = old.active;

            name = old.name;
            attack = old.attack;
            manaAttack = old.manaAttack;
            critChance = old.critChance;
            critDamage = old.critDamage;
            walkspeed = old.walkspeed;
            blockChance = old.blockChance;
            dodgeChance = old.dodgeChance;
            suckRatio = old.suckRatio;
            fireChance = old.fireChance;
            freezeChance = old.freezeChance;
            poisonChance = old.poisonChance;
            bloodChance = old.bloodChance;
        } else reset();
    }

    public void reset() {
        globalId = -1;
        active = false;

        name = "";
        attack = 0;
        manaAttack = 0;
        critChance = 0;
        critDamage = 0;
        walkspeed = 0;
        blockChance = 0;
        dodgeChance = 0;
        suckRatio = 0;
        fireChance = 0;
        freezeChance = 0;
        poisonChance = 0;
        bloodChance = 0;
    }

    public String toString() {
        return "{global:" + globalId + ",active:" + active + ",name:" + name + ",attack:" + attack + ",walkspeed:" + walkspeed + "}";
    }

    public static Attrib deserialize(Node node, int id) {
        Attrib attrib = new Attrib(id);
        if (node instanceof NodeMap) {
            ((NodeMap) node).modify(attrib);
        }
        return attrib;
    }

    public static NodeMap serialize(Attrib attrib, Options options) {
        NodeMap node = new NodeMap(options);
        if (attrib != null) {
            if (attrib.name != null && !attrib.name.isEmpty()) node.set("name", attrib.name);
            if (attrib.attack != 0) node.set("attack", attrib.attack);
            if (attrib.manaAttack != 0) node.set("manaAttack", attrib.manaAttack);
            if (attrib.critChance != 0) node.set("critChance", attrib.critChance);
            if (attrib.critDamage != 0) node.set("critDamage", attrib.critDamage);
            if (attrib.walkspeed != 0) node.set("walkspeed", attrib.walkspeed);
            if (attrib.blockChance != 0) node.set("blockChance", attrib.blockChance);
            if (attrib.dodgeChance != 0) node.set("dodgeChance", attrib.dodgeChance);
            if (attrib.suckRatio != 0) node.set("suckRatio", attrib.suckRatio);
            if (attrib.fireChance != 0) node.set("fireChance", attrib.fireChance);
            if (attrib.freezeChance != 0) node.set("freezeChance", attrib.freezeChance);
            if (attrib.poisonChance != 0) node.set("poisonChance", attrib.poisonChance);
            if (attrib.bloodChance != 0) node.set("bloodChance", attrib.bloodChance);
        }
        return node;
    }
}
