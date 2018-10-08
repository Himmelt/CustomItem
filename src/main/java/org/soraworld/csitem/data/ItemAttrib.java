package org.soraworld.csitem.data;

import org.soraworld.hocon.node.Serializable;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataSerializable;

@Serializable
public class ItemAttrib implements DataSerializable {

    public int getContentVersion() {
        return 0;
    }

    public DataContainer toContainer() {
        return null;
    }
}
