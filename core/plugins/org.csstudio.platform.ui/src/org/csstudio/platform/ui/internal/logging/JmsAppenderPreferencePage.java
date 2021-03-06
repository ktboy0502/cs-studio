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
package org.csstudio.platform.ui.internal.logging;

import org.csstudio.platform.CSSPlatformPlugin;
import org.csstudio.platform.logging.CentralLogger;
import org.csstudio.platform.ui.internal.localization.Messages;
import org.csstudio.platform.ui.security.PasswordFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;

/**
 * A preference page for the css JMS log appender.
 * 
 * @author Alexander Will, Sven Wende
 */
public class JmsAppenderPreferencePage extends AbstractAppenderPreferencePage {

	/**
	 * Default constructor.
	 */
	public JmsAppenderPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
		setMessage(Messages.JmsAppenderPreferencePage_PAGE_TITLE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
    protected final void createFieldEditors() {
		addField(new RadioGroupFieldEditor(
				CentralLogger.PROP_LOG4J_JMS_THRESHOLD, Messages
						.JmsAppenderPreferencePage_LOG_LEVEL, 1,
				new String[][] { { "DEBUG", "DEBUG" }, { "INFO", "INFO" }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						{ "WARN", "WARN" }, { "ERROR", "ERROR" }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						{ "FATAL", "FATAL" } }, getFieldEditorParent(), true)); //$NON-NLS-1$ //$NON-NLS-2$

		addField(new StringFieldEditor(
				CentralLogger.PROP_LOG4J_JMS_PATTERN,
				Messages.JmsAppenderPreferencePage_PATTERN, getFieldEditorParent()));

		addField(new StringFieldEditor(CentralLogger.PROP_LOG4J_JMS_URL,
				Messages.JmsAppenderPreferencePage_URL,
				getFieldEditorParent()));

		addField(new StringFieldEditor(
				CentralLogger.PROP_LOG4J_JMS_TOPIC,
				Messages.JmsAppenderPreferencePage_TOPIC, getFieldEditorParent()));

		addField(new PasswordFieldEditor(
				CentralLogger.PROP_LOG4J_JMS_USER,
				Messages.JmsAppenderPreferencePage_USER, getFieldEditorParent(),
				CSSPlatformPlugin.getDefault().getBundle().getSymbolicName(), false));

		addField(new PasswordFieldEditor(
				CentralLogger.PROP_LOG4J_JMS_PASSWORD,
				Messages.JmsAppenderPreferencePage_PASSWORD, getFieldEditorParent(),
				CSSPlatformPlugin.getDefault().getBundle().getSymbolicName(), true));
	}
}
