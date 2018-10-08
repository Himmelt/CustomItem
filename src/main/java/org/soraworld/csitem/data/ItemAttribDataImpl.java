package org.soraworld.csitem.data;

import org.soraworld.csitem.api.ImmutableItemAttribData;
import org.soraworld.csitem.api.ItemAttribData;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;

import java.util.Optional;

public class ItemAttribDataImpl extends AbstractData<ItemAttribData, ImmutableItemAttribData> implements ItemAttribData {
    protected void registerGettersAndSetters() {
    }

    public Optional<ItemAttribData> fill(DataHolder dataHolder, MergeFunction overlap) {
        return Optional.empty();
    }

    public Optional<ItemAttribData> from(DataContainer container) {
        return Optional.empty();
    }

    public ItemAttribData copy() {
        return null;
    }

    public ImmutableItemAttribData asImmutable() {
        return new ImmutableAttribDataImpl();
    }

    public int getContentVersion() {
        return 0;
    }
}
