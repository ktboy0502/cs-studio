/* 
 * Copyright (c) 2006 Stiftung Deutsches Elektronen-Synchroton, 
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
package org.csstudio.platform.internal.model;

import static org.junit.Assert.*;

import org.csstudio.platform.model.IProcessVariable;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link ProcessVariableFactory}.
 * 
 * @author Sven Wende
 * 
 */
public final class ProcessVariableFactoryTest {

	/**
	 * Sample factory.
	 */
	private ProcessVariableFactory _factory;

	/**
	 * Sample process variable.
	 */
	private IProcessVariable _processVariable;

	/**
	 */
	@Before
	public void setUp() {
		_factory = new ProcessVariableFactory();
		_processVariable = new ProcessVariable("pv"); //$NON-NLS-1$
	}

	/**
	 * Test method for
	 * {@link org.csstudio.platform.internal.model.ProcessVariableFactory#createStringRepresentationFromItem(org.csstudio.platform.model.IProcessVariable)}.
	 */
	@Test
	public void testCreateStringRepresentationFromItemIProcessVariable() {
		String s = _factory
				.createStringRepresentationFromItem(_processVariable);
		assertNotNull(s);
	}

	/**
	 * Test method for
	 * {@link org.csstudio.platform.internal.model.ProcessVariableFactory#createItemFromStringRepresentation(java.lang.String)}.
	 */
	@Test
	public void testCreateItemFromStringRepresentationString() {
		String s = _factory
				.createStringRepresentationFromItem(_processVariable);
		assertNotNull(s);
		IProcessVariable processVariable = _factory
				.createItemFromStringRepresentation(s);
		assertNotNull(processVariable);
		
		assertEquals(_processVariable.getName(), processVariable.getName());

	}

}
