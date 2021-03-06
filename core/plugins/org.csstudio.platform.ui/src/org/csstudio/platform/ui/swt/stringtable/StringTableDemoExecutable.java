package org.csstudio.platform.ui.swt.stringtable;

import java.util.ArrayList;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/** main() routine for testing the string table editor as an SWT app.
 *  Icons will be missing because of missing bundle support.
 *  @author Kay Kasemir
 */
public class StringTableDemoExecutable
{
    private static boolean run = true;
    
    @SuppressWarnings("nls")
    public static void main(String[] args)
    {
        final Display display = new Display();
        final Shell shell = new Shell(display);
        shell.setBounds(400, 100, 300, 350);
        shell.setLayout(new FillLayout());

        final ArrayList<String> items = new ArrayList<String>();
        items.add("One");
        items.add("Two");
        items.add("Three");
        new StringTableEditor(shell, items);
        
        shell.open();
        // Message loop left to the application
        while (run && !shell.isDisposed())
            if (!display.readAndDispatch())
                display.sleep();
        display.dispose(); // !
    }
}
