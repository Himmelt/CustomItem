package org.soraworld.csitem.data;

import org.soraworld.csitem.manager.CSIManager;

public class ItemAttrib extends Attrib {

    public boolean isGlobal() {
        return false;
    }

    public Attrib getGlobal() {
        return CSIManager.getGlobal(globalId);
    }
}
