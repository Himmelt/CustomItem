package org.soraworld.csitem.data;

import org.soraworld.csitem.api.ImmutableItemAttribData;
import org.soraworld.csitem.api.ItemAttribData;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;

public class ImmutableAttribDataImpl extends AbstractImmutableData<ImmutableItemAttribData, ItemAttribData> implements ImmutableItemAttribData {
    protected void registerGetters() {

    }

    public ItemAttribData asMutable() {
        return null;
    }

    public int getContentVersion() {
        return 0;
    }
}
