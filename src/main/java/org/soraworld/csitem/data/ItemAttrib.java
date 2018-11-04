package org.soraworld.csitem.data;

import org.soraworld.csitem.manager.CSIManager;

public class ItemAttrib extends Attrib {

    public long lastBlock = 0;
    public long lastDodge = 0;

    public boolean isGlobal() {
        return false;
    }

    public void active() {
        if (!isActive()) {
            // TODO active

            setActive(true);
        }
    }

    public Attrib getGlobal() {
        return CSIManager.getGlobal(globalId);
    }
}
