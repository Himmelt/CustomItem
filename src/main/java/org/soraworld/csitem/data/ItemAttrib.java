package org.soraworld.csitem.data;

import org.soraworld.hocon.node.Serializable;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Serializable
public class ItemAttrib extends Attrib implements DataManipulator<ItemAttrib, ItemAttrib.Immutable> {

    public static final DataQuery ACTIVE = DataQuery.of("active");
    public static final DataQuery GLOBAL = DataQuery.of("global");

    public static final DataQuery NAME = DataQuery.of("name");
    public static final DataQuery ATTACK = DataQuery.of("attack");
    public static final DataQuery MANA_ATTACK = DataQuery.of("manaAttack");
    public static final DataQuery CRIT_CHANCE = DataQuery.of("critChance");
    public static final DataQuery CRIT_DAMAGE = DataQuery.of("critDamage");
    public static final DataQuery WALK_SPEED = DataQuery.of("walkSpeed");
    public static final DataQuery BLOCK_CHANCE = DataQuery.of("blockChance");
    public static final DataQuery LAST_BLOCK = DataQuery.of("lastBlock");
    public static final DataQuery DODGE_CHANCE = DataQuery.of("dodgeChance");
    public static final DataQuery LAST_DODGE = DataQuery.of("lastDodge");
    public static final DataQuery SUCK_RATIO = DataQuery.of("suckRatio");
    public static final DataQuery FIRE_CHANCE = DataQuery.of("fireChance");
    public static final DataQuery FREEZE_CHANCE = DataQuery.of("freezeChance");
    public static final DataQuery POISON_CHANCE = DataQuery.of("poisonChance");
    public static final DataQuery BLOOD_CHANCE = DataQuery.of("bloodChance");

    public ItemAttrib() {
    }

    public ItemAttrib(Attrib attrib) {
        super(attrib);
    }

    public Optional<ItemAttrib> fill(DataHolder dataHolder, MergeFunction overlap) {
        ItemAttrib attrib = overlap.merge(this, dataHolder.get(ItemAttrib.class).orElse(null));
        copy(attrib);
        return Optional.of(this);
    }

    public Optional<ItemAttrib> from(DataContainer con) {
        if (con.contains(GLOBAL)) {
            reset();

            con.getInt(GLOBAL).ifPresent(i -> globalId = i);
            con.getBoolean(ACTIVE).ifPresent(b -> active = b);

            con.getString(NAME).ifPresent(s -> name = s);
            con.getInt(ATTACK).ifPresent(i -> attack = i);
            con.getInt(MANA_ATTACK).ifPresent(i -> manaAttack = i);
            con.getFloat(CRIT_CHANCE).ifPresent(f -> critChance = f);
            con.getFloat(CRIT_DAMAGE).ifPresent(f -> critDamage = f);
            con.getFloat(WALK_SPEED).ifPresent(f -> walkspeed = f);
            con.getFloat(BLOCK_CHANCE).ifPresent(f -> blockChance = f);
            con.getLong(LAST_BLOCK).ifPresent(l -> lastBlock = l);
            con.getFloat(DODGE_CHANCE).ifPresent(f -> dodgeChance = f);
            con.getLong(LAST_DODGE).ifPresent(l -> lastDodge = l);
            con.getFloat(SUCK_RATIO).ifPresent(f -> suckRatio = f);
            con.getFloat(FIRE_CHANCE).ifPresent(f -> fireChance = f);
            con.getFloat(FREEZE_CHANCE).ifPresent(f -> freezeChance = f);
            con.getFloat(POISON_CHANCE).ifPresent(f -> poisonChance = f);
            con.getFloat(BLOOD_CHANCE).ifPresent(f -> bloodChance = f);
            return Optional.of(this);
        } else return Optional.empty();
    }

    public <E> ItemAttrib set(Key<? extends BaseValue<E>> key, E value) {
        return this;
    }

    public <E> Optional<E> get(Key<? extends BaseValue<E>> key) {
        return Optional.empty();
    }

    public <E, V extends BaseValue<E>> Optional<V> getValue(Key<V> key) {
        return Optional.empty();
    }

    public boolean supports(Key<?> key) {
        return false;
    }

    public ItemAttrib copy() {
        return new ItemAttrib(this);
    }

    public Set<Key<?>> getKeys() {
        return new HashSet<>();
    }

    public Set<ImmutableValue<?>> getValues() {
        return new HashSet<>();
    }

    public Immutable asImmutable() {
        return new Immutable(this);
    }

    public int getContentVersion() {
        return 0;
    }

    public DataContainer toContainer() {
        return DataContainer.createNew()
                .set(GLOBAL, globalId)
                .set(ACTIVE, active)

                .set(NAME, name)
                .set(ATTACK, attack)
                .set(MANA_ATTACK, manaAttack)
                .set(CRIT_CHANCE, critChance)
                .set(CRIT_DAMAGE, critDamage)
                .set(WALK_SPEED, walkspeed)
                .set(BLOCK_CHANCE, blockChance)
                .set(LAST_BLOCK, lastBlock)
                .set(DODGE_CHANCE, dodgeChance)
                .set(LAST_DODGE, lastDodge)
                .set(SUCK_RATIO, suckRatio)
                .set(FIRE_CHANCE, fireChance)
                .set(FREEZE_CHANCE, freezeChance)
                .set(POISON_CHANCE, poisonChance)
                .set(BLOOD_CHANCE, bloodChance);
    }

    public static class Immutable extends Attrib implements ImmutableDataManipulator<Immutable, ItemAttrib> {
        public Immutable(Attrib attrib) {
            super(attrib);
        }

        public ItemAttrib asMutable() {
            return new ItemAttrib(this);
        }

        public int getContentVersion() {
            return 0;
        }

        public DataContainer toContainer() {
            return asMutable().toContainer();
        }

        public <E> Optional<E> get(Key<? extends BaseValue<E>> key) {
            return Optional.empty();
        }

        public <E, V extends BaseValue<E>> Optional<V> getValue(Key<V> key) {
            return Optional.empty();
        }

        public boolean supports(Key<?> key) {
            return false;
        }

        public Set<Key<?>> getKeys() {
            return new HashSet<>();
        }

        public Set<ImmutableValue<?>> getValues() {
            return new HashSet<>();
        }
    }

    public static class Builder implements DataManipulatorBuilder<ItemAttrib, Immutable> {
        public ItemAttrib create() {
            return new ItemAttrib();
        }

        public Optional<ItemAttrib> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        public Optional<ItemAttrib> build(DataView con) throws InvalidDataException {
            if (con.contains(NAME)) {
                ItemAttrib attrib = new ItemAttrib();

                con.getInt(GLOBAL).ifPresent(i -> attrib.globalId = i);
                con.getBoolean(ACTIVE).ifPresent(b -> attrib.active = b);

                con.getString(NAME).ifPresent(s -> attrib.name = s);
                con.getInt(ATTACK).ifPresent(i -> attrib.attack = i);
                con.getInt(MANA_ATTACK).ifPresent(i -> attrib.manaAttack = i);
                con.getFloat(CRIT_CHANCE).ifPresent(f -> attrib.critChance = f);
                con.getFloat(CRIT_DAMAGE).ifPresent(f -> attrib.critDamage = f);
                con.getFloat(WALK_SPEED).ifPresent(f -> attrib.walkspeed = f);
                con.getFloat(BLOCK_CHANCE).ifPresent(f -> attrib.blockChance = f);
                con.getLong(LAST_BLOCK).ifPresent(l -> attrib.lastBlock = l);
                con.getFloat(DODGE_CHANCE).ifPresent(f -> attrib.dodgeChance = f);
                con.getLong(LAST_DODGE).ifPresent(l -> attrib.lastDodge = l);
                con.getFloat(SUCK_RATIO).ifPresent(f -> attrib.suckRatio = f);
                con.getFloat(FIRE_CHANCE).ifPresent(f -> attrib.fireChance = f);
                con.getFloat(FREEZE_CHANCE).ifPresent(f -> attrib.freezeChance = f);
                con.getFloat(POISON_CHANCE).ifPresent(f -> attrib.poisonChance = f);
                con.getFloat(BLOOD_CHANCE).ifPresent(f -> attrib.bloodChance = f);
                return Optional.of(attrib);
            } else return Optional.empty();
        }
    }
}
