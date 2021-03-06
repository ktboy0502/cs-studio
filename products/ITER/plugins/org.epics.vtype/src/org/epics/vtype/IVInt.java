/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.vtype;

/**
 * Immutable VInt implementation.
 *
 * @author carcassi
 */
class IVInt extends IVNumeric implements VInt {
    
    private final Integer value;

    IVInt(Integer value, Alarm alarm, Time time, Display display) {
        super(alarm, time, display);
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return VTypeToString.toString(this);
    }

}
