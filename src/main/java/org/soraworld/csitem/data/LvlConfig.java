package org.soraworld.csitem.data;

import org.soraworld.hocon.node.Serializable;
import org.soraworld.hocon.node.Setting;

@Serializable
public class LvlConfig {
    @Setting
    public float attack = 0;
    @Setting
    public float manaAttack = 0;
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
}
