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
package org.csstudio.platform.ui.internal.console;

import java.io.PrintStream;

import org.csstudio.platform.logging.CentralLogger;
import org.csstudio.platform.ui.CSSPlatformUiPlugin;
import org.csstudio.platform.ui.internal.localization.Messages;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * The standard css console that, if initialized, becomes the system's standard
 * output.
 * 
 * @author Alexander Will
 */
public final class Console {
	/**
	 * The only one instance of this class.
	 */
	private static Console _instance = null;

	/**
	 * The message console.
	 */
	private MessageConsole _console = null;

	/**
	 * The console output stream.
	 */
	private MessageConsoleStream _stream = null;

	/**
	 * The original system output stream.
	 */
	private PrintStream _originalSystemOut = null;

	/**
	 * Constructor is private due to singleton pattern.
	 */
	private Console() {
		initConsole();
	}

	/**
	 * Return the only one instance of this class.
	 * 
	 * @return The only one instance of this class.
	 */
	public static Console getInstance() {
		if (_instance == null) {
			_instance = new Console();
		}
		return _instance;
	}

	/**
	 * Set the text color of the associated output stream.
	 * 
	 * @param color
	 *            The text color of the associated output stream.
	 */
	public void setColor(final Color color) {
		_stream.setColor(color);
	}

	/**
	 * Reset the system output stream to its cached state.
	 */
	public void resetSystemOutputStream() {
		if (_originalSystemOut != null) {
			System.setOut(_originalSystemOut);
			
			// WARNING: It is not possible to reconfigure the logger here! This
			// method is called in the UI thread, so reconfiguring the logger
			// here would mean that the UI thread waits for the logger, which
			// could cause a deadlock.
		}
	}

	/**
	 * Initialize the console and redirect the standard System.out to it.
	 */
	private void initConsole() {
		_console = new MessageConsole(Messages.Console_CONSOLE_TITLE, null)
        { 
			@Override
			public String getHelpContextId() {
				return CSSPlatformUiPlugin.ID + ".console"; //$NON-NLS-1$
			}
		};

		_stream = _console.newMessageStream();
		
		// Values are from https://bugs.eclipse.org/bugs/show_bug.cgi?id=46871#c5
		_console.setWaterMarks(80000, 100000);
		
		ConsolePlugin consolePlugin = ConsolePlugin.getDefault();
		consolePlugin.getConsoleManager().addConsoles(
				new IConsole[] { _console });

		_originalSystemOut = System.out;
		System.setOut(new PrintStream(_stream));

		// the logging mechanism needs to be informed that the standard system
		// out has changed!
		CentralLogger.getInstance().configure();
	}
}
