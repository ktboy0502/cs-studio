/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.formula;

import org.epics.pvmanager.ExpressionLanguage;
import org.epics.vtype.VDouble;
import org.epics.vtype.VNumber;
import org.epics.vtype.ValueFactory;
import org.epics.util.time.Timestamp;


/**
 *
 * @author carcassi
 */
abstract class TwoArgNumericFunction implements ExpressionLanguage.TwoArgFunction<VDouble, VNumber, VNumber>  {

    @Override
    public VDouble calculate(VNumber arg1, VNumber arg2) {
        if (arg1 == null || arg2 == null) {
            return null;
        }
        return ValueFactory.newVDouble(calculate(arg1.getValue().doubleValue(), arg2.getValue().doubleValue()), ValueFactory.newTime(Timestamp.now()));
    }
    
    abstract double calculate(double arg1, double arg2);
    
}
