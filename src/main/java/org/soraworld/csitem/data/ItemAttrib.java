package org.soraworld.csitem.data;

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

public class ItemAttrib implements DataManipulator<ItemAttrib, ItemAttrib.Immutable> {

    public static final DataQuery NAME = DataQuery.of("name");

    public String name = "def name";

    public ItemAttrib(String name) {
        this.name = name;
    }

    public ItemAttrib() {
    }

    public String toString() {
        return "[attrib - name : " + name + " ]";
    }

    public Optional<ItemAttrib> fill(DataHolder dataHolder, MergeFunction overlap) {
        ItemAttrib attrib = overlap.merge(this, dataHolder.get(ItemAttrib.class).orElse(null));
        this.name = attrib.name;
        return Optional.of(this);
    }

    public Optional<ItemAttrib> from(DataContainer container) {
        if (container.contains(NAME)) {
            container.getString(NAME).ifPresent(s -> name = s);
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
        return new ItemAttrib(name);
    }

    public Set<Key<?>> getKeys() {
        return new HashSet<>();
    }

    public Set<ImmutableValue<?>> getValues() {
        return new HashSet<>();
    }

    public Immutable asImmutable() {
        return new Immutable(name);
    }

    public int getContentVersion() {
        return 0;
    }

    public DataContainer toContainer() {
        return DataContainer.createNew()
                .set(NAME, name);
    }

    public static class Immutable implements ImmutableDataManipulator<Immutable, ItemAttrib> {
        public String name = "def im name";

        public Immutable() {
        }

        public Immutable(String name) {
            this.name = name;
        }

        public ItemAttrib asMutable() {
            return new ItemAttrib(name);
        }

        public int getContentVersion() {
            return 0;
        }

        public DataContainer toContainer() {
            return DataContainer.createNew()
                    .set(NAME, name);
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

        public Optional<ItemAttrib> build(DataView container) throws InvalidDataException {
            if (container.contains(NAME)) {
                ItemAttrib attrib = new ItemAttrib();
                container.getString(NAME).ifPresent(s -> attrib.name = s);
                return Optional.of(attrib);
            } else return Optional.empty();
        }
    }
}
