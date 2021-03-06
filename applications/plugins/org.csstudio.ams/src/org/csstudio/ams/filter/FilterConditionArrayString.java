
/* 
 * Copyright (c) 2008 Stiftung Deutsches Elektronen-Synchrotron, 
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
 */

package org.csstudio.ams.filter;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import javax.jms.MapMessage;

import org.csstudio.ams.AMSException;
import org.csstudio.ams.Log;
import org.csstudio.ams.dbAccess.configdb.AggrFilterConditionArrayStringDAO;
import org.csstudio.ams.dbAccess.configdb.AggrFilterConditionArrayStringTObject;
import org.csstudio.ams.dbAccess.configdb.FilterConditionArrayStringValuesTObject;

public class FilterConditionArrayString implements IFilterCondition  {
	
    public static final short OPERATOR_TEXT_EQUAL = 1;
	//public static final short OPERATOR_TEXT_NOT_EQUAL = 2;
	
	//public static final short OPERATOR_NUMERIC_LT = 3;
	//public static final short OPERATOR_NUMERIC_LT_EQUAL = 4;
	public static final short OPERATOR_NUMERIC_EQUAL = 5;
	//public static final short OPERATOR_NUMERIC_GT_EQUAL = 6;
	//public static final short OPERATOR_NUMERIC_GT = 7;
	//public static final short OPERATOR_NUMERIC_NOT_EQUAL = 8;

	//public static final short OPERATOR_TIME_BEFORE = 9;
	//public static final short OPERATOR_TIME_BEFORE_EQUAL = 10;
	public static final short OPERATOR_TIME_EQUAL = 11;
	//public static final short OPERATOR_TIME_AFTER_EQUAL = 12;
	//public static final short OPERATOR_TIME_AFTER = 13;
	//public static final short OPERATOR_TIME_NOT_EQUAL = 14;

	
	private AggrFilterConditionArrayStringTObject fcasObj = null;
	
	@Override
    public void init(Connection conDb, int filterConditionID,int filterRef) throws AMSException
	{
		try
		{
			fcasObj = AggrFilterConditionArrayStringDAO.select(conDb, filterConditionID);
			if (fcasObj == null) 
				throw new AMSException("FilterConditionArrayString.FilterConditionID=" + filterConditionID + " not found.");

		}
		catch (SQLException ex)
		{
			throw new AMSException(ex);
		}
	}
	
	private int numericCompare(String value, String compValue) throws Exception
	{
		double dVal = 0.0;
		try
		{
			dVal = Double.parseDouble(value);
		}
		catch(Exception e)
		{
			Log.log(Log.WARN, "fcs(id/operator) = " 
					+ fcasObj.getArrayString().getFilterConditionRef() + "/" + fcasObj.getArrayString().getOperator() 
					+ " ! Input value '" + value + "' is not a number!");
			return -1;
		}
		double dCompVal = Double.parseDouble(compValue);

		return Double.compare(dVal, dCompVal);
	}
	
	private int timeCompare(String value, String compValue) throws Exception
	{
		Date dateValue = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US).parse(value); 
		Date dateCompValue = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US).parse(compValue); 

		return dateValue.compareTo(dateCompValue);
		
		// Date dateValue = new SimpleDateFormat("hh:mm:ss aaa", Locale.US).parse(value);
		// Date dateCompValue = new SimpleDateFormat("hh:mm:ss aaa", Locale.US).parse(compValue);

		// return dateValue.compareTo(dateCompValue);
	}
	
	@Override
    public boolean match(MapMessage map)
	{
		try
		{
			String value = map.getString(fcasObj.getArrayString().getKeyValue());
			String compValue = null;
			
			Iterator<FilterConditionArrayStringValuesTObject> iter = fcasObj.getArrayStringValues().iterator();

			while(iter.hasNext())
			{		
				compValue = iter.next().getCompValue();
				
				switch (fcasObj.getArrayString().getOperator())
				{
					// text compare
					case OPERATOR_TEXT_EQUAL:
						if(WildcardStringCompare.compare(value, compValue))
							return true;
						break;
					case OPERATOR_NUMERIC_EQUAL: 
						if(numericCompare(value, compValue) == 0)
							return true;
						break;
					case OPERATOR_TIME_EQUAL: 
						if(timeCompare(value, compValue) == 0)
							return true;
					break;
				}
			}
		}
		catch (Exception ex)
		{
    		Log.log(this, Log.WARN, ex);
    		return false;
		}
		return false;
	}
}