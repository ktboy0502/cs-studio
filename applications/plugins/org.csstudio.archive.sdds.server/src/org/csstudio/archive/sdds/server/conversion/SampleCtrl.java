
/* 
 * Copyright (c) 2010 Stiftung Deutsches Elektronen-Synchrotron, 
 * Member of the Helmholtz Association, (DESY), HAMBURG, GERMANY.
 *
 * THIS SOFTWARE IS PROVIDED UNDER THIS LICENSE ON AN "../AS IS" BASIS. 
 * WITHOUT WARRANTY OF ANY KIND, EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED 
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR PARTICULAR PURPOSE AND 
 * NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE 
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR 
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE. SHOULD THE SOFTWARE PROVE DEFECTIVE 
 * IN ANY RESPECT, THE USER ASSUMES THE COST OF ANY NECESSARY SERVICING, REPAIR OR 
 * CORRECTION. THIS DISCLAIMER OF WARRANTY CONSTITUTES AN ESSENTIAL PART OF THIS LICENSE. 
 * NO USE OF ANY SOFTWARE IS AUTHORIZED HEREUNDER EXCEPT UNDER THIS DISCLAIMER.
 * DESY HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, 
 * OR MODIFICATIONS.
 * THE FULL LICENSE SPECIFYING FOR THE SOFTWARE THE REDISTRIBUTION, MODIFICATION, 
 * USAGE AND OTHER RIGHTS AND OBLIGATIONS IS INCLUDED WITH THE DISTRIBUTION OF THIS 
 * PROJECT IN THE FILE LICENSE.HTML. IF THE LICENSE IS NOT INCLUDED YOU MAY FIND A COPY 
 * AT HTTP://WWW.DESY.DE/LEGAL/LICENSE.HTM
 *
 */

package org.csstudio.archive.sdds.server.conversion;

/**
 * @author Markus Moeller
 *
 */
public class SampleCtrl
{
    /** Precision */
    private int precision;
     
    /** LOPR */ 
    private double displayHigh;
     
    /** HOPR */
    private double displayLow;
     
    /** HIHI */
    private double highAlarm;
     
    /** HIGH */
    private double highWarning;
     
    /** LOW  */
    private double lowWarning;
     
    /** LOLO */
    private double lowAlarm;
     
    /** Length of string 'units' */
    private int unitsLength;
     
    /** Units */
    private String units;

    /**
     * Standard constructor that sets all fields to zero.
     * 
     */
    public SampleCtrl()
    {
        this.precision = 0;
        this.displayHigh = 0.0;
        this.displayLow = 0.0;
        this.highAlarm = 0.0;
        this.highWarning = 0.0;
        this.lowWarning = 0.0;
        this.lowAlarm = 0.0;
        this.unitsLength = 0;
        this.units = "";
    }
    
    /**
     * @param precision
     * @param displayHigh
     * @param displayLow
     * @param highAlarm
     * @param highWarning
     * @param lowWarning
     * @param lowAlarm
     * @param unitsLength
     * @param units
     */
    public SampleCtrl(int precision, double displayHigh, double displayLow,
            double highAlarm, double highWarning, double lowWarning,
            double lowAlarm, int unitsLength, String units)
    {
        this.precision = precision;
        this.displayHigh = displayHigh;
        this.displayLow = displayLow;
        this.highAlarm = highAlarm;
        this.highWarning = highWarning;
        this.lowWarning = lowWarning;
        this.lowAlarm = lowAlarm;
        this.unitsLength = unitsLength;
        this.units = units;
    }
    
    public int getPrecision()
    {
        return precision;
    }

    public void setPrecision(int precision)
    {
        this.precision = precision;
    }
    
    public double getDisplayHigh()
    {
        return displayHigh;
    }
    
    public void setDisplayHigh(double displayHigh)
    {
        this.displayHigh = displayHigh;
    }
    
    public double getDisplayLow()
    {
        return displayLow;
    }
    
    public void setDisplayLow(double displayLow)
    {
        this.displayLow = displayLow;
    }
    
    public double getHighAlarm()
    {
        return highAlarm;
    }
    
    public void setHighAlarm(double highAlarm)
    {
        this.highAlarm = highAlarm;
    }
    
    public double getHighWarning()
    {
        return highWarning;
    }
    
    public void setHighWarning(double highWarning)
    {
        this.highWarning = highWarning;
    }
    
    public double getLowWarning()
    {
        return lowWarning;
    }
    
    public void setLowWarning(double lowWarning)
    {
        this.lowWarning = lowWarning;
    }
    
    public double getLowAlarm()
    {
        return lowAlarm;
    }
    
    public void setLowAlarm(double lowAlarm)
    {
        this.lowAlarm = lowAlarm;
    }
    
    public int getUnitsLength()
    {
        return unitsLength;
    }
    
    public void setUnitsLength(int unitsLength)
    {
        this.unitsLength = unitsLength;
    }
    
    public String getUnits()
    {
        return units;
    }
    
    public void setUnits(String units)
    {
        this.units = units;
        if(units != null) {
            
            this.unitsLength = units.length();
        }
    }
}