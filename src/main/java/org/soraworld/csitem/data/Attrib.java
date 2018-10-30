package org.soraworld.csitem.data;

import org.soraworld.hocon.node.*;

@Serializable
public class Attrib {

    public int globalId = 0;
    private boolean active = false;
    @Setting
    public int level = 0;
    @Setting
    public int points = 0;
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
    public long lastBlock = 0;
    @Setting
    public float dodgeChance = 0;
    public long lastDodge = 0;
    /**
     * 闪避 向玩家朝向(面前方为正方向)位移量 .
     */
    public double dodgeX;
    /**
     * 闪避 向玩家侧向(左手方为正方向)位移量 .
     */
    public double dodgeZ;
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
        this.globalId = id;
    }

    public Attrib(int id, String name) {
        this.globalId = id;
        this.name = name;
    }

    public Attrib(Attrib other) {
        copy(other);
    }

    public Attrib toLocal() {
        Attrib local = new Attrib();
        local.copy(this);
        local.globalId = 0;
        local.active = false;
        return local;
    }

    public void copy(Attrib old) {
        if (old != null) {
            globalId = old.globalId;
            active = old.active;

            level = old.level;
            points = old.points;
            name = old.name;
            attack = old.attack;
            manaAttack = old.manaAttack;
            critChance = old.critChance;
            critDamage = old.critDamage;
            walkspeed = old.walkspeed;
            blockChance = old.blockChance;
            lastBlock = old.lastBlock;
            dodgeChance = old.dodgeChance;
            lastDodge = old.lastDodge;
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

        level = 0;
        points = 0;
        name = "";
        attack = 0;
        manaAttack = 0;
        critChance = 0;
        critDamage = 0;
        walkspeed = 0;
        blockChance = 0;
        lastBlock = 0;
        dodgeChance = 0;
        lastDodge = 0;
        suckRatio = 0;
        fireChance = 0;
        freezeChance = 0;
        poisonChance = 0;
        bloodChance = 0;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void active() {
        if (!active) {
            // TODO active

            this.active = true;
        }
    }

    public String toString() {
        return "{\nglobal:" + globalId + ",\n  active:" + active + ",\n  name:" + name + ",\n  attack:" + attack + ",\n  walkspeed:" + walkspeed + "\n}";
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
            if (attrib.level != 0) node.set("level", attrib.level);
            if (attrib.points != 0) node.set("points", attrib.points);
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
