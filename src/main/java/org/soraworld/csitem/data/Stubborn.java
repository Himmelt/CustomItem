package org.soraworld.csitem.data;

import org.soraworld.hocon.node.Serializable;
import org.soraworld.hocon.node.Setting;

/**
 * 偏执.
 */
@Serializable
public class Stubborn {
    @Setting
    public String name = "";
    @Setting
    public int time = 1;
}
