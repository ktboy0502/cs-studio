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
 package org.csstudio.platform.ui.internal.dataexchange.demo;

import java.util.Vector;

import org.csstudio.platform.model.CentralItemFactory;
import org.csstudio.platform.model.IArchiveDataSource;
import org.csstudio.platform.model.IProcessVariable;
import org.csstudio.platform.ui.internal.dataexchange.ArchiveDataSourceDragSource;
import org.csstudio.platform.ui.internal.dataexchange.ArchiveDataSourceDropTarget;
import org.csstudio.platform.ui.internal.dataexchange.ProcessVariableDragSource;
import org.csstudio.platform.ui.internal.dataexchange.ProcessVariableDropTarget;
import org.csstudio.platform.ui.internal.dataexchange.ProcessVariableOrArchiveDataSourceDropTarget;
import org.csstudio.platform.ui.internal.dataexchange.ProcessVariableWithArchiveDragSource;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

/** Simple demo of dragging text as well as PVs.
 *  <p>
 *  One the left side there are several data sources
 *  which drag data as string (text), PV name, ...,
 *  and on the right side there are drop targets
 *  that expect text, PV names, ...
 *  <p>
 *  Since the idea is to always support text/string,
 *  so that one can also drag a PV name to/from for example MS Word,
 *  the system is not perfect:
 *  Each drag source _also_ provides a text,
 *  and each drop target _also_ accepts text,
 *  so one can drag everything to everything.
 *  <p>
 *  But when using the 'correct' sources and targets, it works 'better':
 *  When dragging multiple PV names to the PV drop target, they
 *  are recognized as multiple (individual PVs).
 *  When dragging them to the text drop target, one gets one string
 *  "pv1, pv2, ...".
 *  
 *  @author Kay Kasemir
 */
public class DragDropDemo
{
    private Label hello;
    private TableViewer pv_table_viewer;
    private TableViewer arch_table_viewer;
    private Text string;
    private Text pv_name;
    private Text archive_infos;
    private Label combined;
    private Text pv_or_archs;

    /** Create the GUI. */
    public void createGUI(final Shell parent)
    {
        parent.setLayout(new FillLayout(SWT.HORIZONTAL));

        Composite sources = new Composite(parent, 0);
        GridLayout layout = new GridLayout();
        sources.setLayout(layout);
        
        Composite targets = new Composite(parent, 0);
        layout = new GridLayout();
        targets.setLayout(layout);
        
        hello = new Label(sources, SWT.BOLD);
        hello.setText("Drag 'Hello' as a string");
        GridData gd = new GridData();
        hello.setLayoutData(gd);
        
        Label label = new Label(sources, SWT.SEPARATOR | SWT.HORIZONTAL);
        gd = new GridData();
        gd.grabExcessHorizontalSpace = true;
        gd.horizontalAlignment = SWT.FILL;
        label.setLayoutData(gd);
        
        // A lot of code to set up a table....
        // End result: A table where each data element implements
        // IProcessVariableName.
        Table pv_table = new Table(sources,
                        SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
        pv_table.setHeaderVisible(true);
        pv_table.setLinesVisible(true);
        gd = new GridData();
        gd.grabExcessVerticalSpace = true;
        gd.verticalAlignment = SWT.FILL;
        pv_table.setLayoutData(gd);
        
        TableColumn col = new TableColumn(pv_table, SWT.LEFT);
        col.setText("Drag PV Name(s) from here...");
        col.setWidth(200);
        
        pv_table_viewer = new TableViewer(pv_table);
        pv_table_viewer.setContentProvider(new ArrayContentProvider());
        pv_table_viewer.setLabelProvider(new StringLabelProvider());
        
        Vector<IProcessVariable> names = new Vector<IProcessVariable>();
        names.add(CentralItemFactory.createProcessVariable("jane"));
        names.add(CentralItemFactory.createProcessVariable("fred"));
        pv_table_viewer.setInput(names);
        
        label = new Label(sources, SWT.SEPARATOR | SWT.HORIZONTAL);
        gd = new GridData();
        gd.grabExcessHorizontalSpace = true;
        gd.horizontalAlignment = SWT.FILL;
        label.setLayoutData(gd);
        
        // Similar: A table that provides IArchiveDataSource.
        label = new Label(sources, SWT.BOLD);
        label.setText("Drag archive sources from here...");
        gd = new GridData();
        label.setLayoutData(gd);
        
        Table arch_table = new Table(sources,
                        SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
        arch_table.setHeaderVisible(true);
        arch_table.setLinesVisible(true);
        gd = new GridData();
        gd.grabExcessVerticalSpace = true;
        gd.verticalAlignment = SWT.FILL;
        arch_table.setLayoutData(gd);
        
        col = new TableColumn(arch_table, SWT.LEFT);
        col.setText("URL");
        col.setWidth(120);

        col = new TableColumn(arch_table, SWT.LEFT);
        col.setText("Key");
        col.setWidth(45);
        
        col = new TableColumn(arch_table, SWT.LEFT);
        col.setText("Name");
        col.setWidth(100);

        arch_table_viewer = new TableViewer(arch_table);
        arch_table_viewer.setContentProvider(new ArrayContentProvider());
        arch_table_viewer.setLabelProvider(new ArchiveDataSourceLabelProvider());
        
        Vector<IArchiveDataSource> archives = new Vector<IArchiveDataSource>();
        archives.add(CentralItemFactory.createArchiveDataSource("http://server1", 1, "main"));
        archives.add(CentralItemFactory.createArchiveDataSource("http://server1", 2, "alternate"));
        archives.add(CentralItemFactory.createArchiveDataSource("http://server2", 1, "Egon's"));
        arch_table_viewer.setInput(archives);
        
        label = new Label(sources, SWT.SEPARATOR | SWT.HORIZONTAL);
        gd = new GridData();
        gd.grabExcessHorizontalSpace = true;
        gd.horizontalAlignment = SWT.FILL;
        label.setLayoutData(gd);
        
        combined = new Label(sources, SWT.BOLD);
        combined.setText("Drag combined data as PV and Archive Info");
        gd = new GridData();
        combined.setLayoutData(gd);
        
        // -- Drop Targets --

        // Text for dropping text
        label = new Label(targets, 0);
        label.setText("Drop Text here:");
        gd = new GridData();
        gd.horizontalSpan = 2;
        label.setLayoutData(gd);

        string = new Text(targets, SWT.BORDER);
        gd = new GridData();
        gd.grabExcessHorizontalSpace = true;
        gd.horizontalAlignment = SWT.FILL;
        string.setLayoutData(gd);

        label = new Label(targets, SWT.SEPARATOR | SWT.HORIZONTAL);
        gd = new GridData();
        gd.grabExcessHorizontalSpace = true;
        gd.horizontalAlignment = SWT.FILL;
        label.setLayoutData(gd);
        
        // Text for dropping PV name
        label = new Label(targets, 0);
        label.setText("Drop PVs here:");
        gd = new GridData();
        gd.horizontalSpan = 2;
        label.setLayoutData(gd);

        pv_name = new Text(targets, SWT.BORDER);
        gd = new GridData();
        gd.grabExcessHorizontalSpace = true;
        gd.horizontalAlignment = SWT.FILL;
        pv_name.setLayoutData(gd);
        
        label = new Label(targets, SWT.SEPARATOR | SWT.HORIZONTAL);
        gd = new GridData();
        gd.grabExcessHorizontalSpace = true;
        gd.horizontalAlignment = SWT.FILL;
        label.setLayoutData(gd);
        
        // Text for dropping archive data source infos
        label = new Label(targets, 0);
        label.setText("Drop archive data source(s) here:");
        gd = new GridData();
        gd.horizontalSpan = 2;
        label.setLayoutData(gd);

        archive_infos = new Text(targets, SWT.BORDER);
        gd = new GridData();
        gd.grabExcessHorizontalSpace = true;
        gd.horizontalAlignment = SWT.FILL;
        archive_infos.setLayoutData(gd);
        
        // Text for dropping archive data source infos
        label = new Label(targets, 0);
        label.setText("Drop PVs or archive data source(s) here:");
        gd = new GridData();
        gd.horizontalSpan = 2;
        label.setLayoutData(gd);

        pv_or_archs = new Text(targets, SWT.BORDER);
        gd = new GridData();
        gd.grabExcessHorizontalSpace = true;
        gd.horizontalAlignment = SWT.FILL;
        pv_or_archs.setLayoutData(gd);
        
        Button clr = new Button(targets, SWT.PUSH);
        clr.setText("Clear");
        gd = new GridData();
        gd.horizontalAlignment = SWT.RIGHT;
        clr.setLayoutData(gd);
        clr.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                string.setText("");
                pv_name.setText("");
                archive_infos.setText("");
                pv_or_archs.setText("");
            }
        });
    }
    
    private void appendText(Text widget, String txt)
    {
        if (widget.getText().length() > 0)
            widget.setText(widget.getText() + ", " + txt);
        else
            widget.setText(txt);
    }
    
    /** This is the actual drag-and-drop code. */
    public void hookDragAndDrop()
    {
        new TextDragSource(hello, "Hello");
        new ProcessVariableDragSource(pv_table_viewer.getTable(), pv_table_viewer);
        new ArchiveDataSourceDragSource(arch_table_viewer.getTable(), arch_table_viewer);

        new StringDropTarget(string)
        {
            @Override
            public void handleDrop(String name)
            {
                appendText(string, " String '" + name + "'");
            }
        };
        
        new ProcessVariableDropTarget(pv_name)
        {
            @Override
            public void handleDrop(IProcessVariable name,
                            DropTargetEvent event)
            {
                appendText(pv_name, " PV '" + name.getName() + "'");
            }
        };
        
        new ArchiveDataSourceDropTarget(archive_infos)
        {
            @Override
            public void handleDrop(IArchiveDataSource archive,
                            DropTargetEvent event)
            {
                appendText(archive_infos,
                           " Archive '"
                           + archive.getUrl()
                           + "' (" + archive.getKey()
                           + ", " + archive.getName()
                           + ")");
            }
        };
        
        new ProcessVariableOrArchiveDataSourceDropTarget(pv_or_archs)
        {
            @Override
            public void handleDrop(IProcessVariable name,
                            DropTargetEvent event)
            {
                appendText(pv_or_archs, " PV '" + name.getName() + "'");
            }
            @Override
            public void handleDrop(IArchiveDataSource archive,
                            DropTargetEvent event)
            {
                appendText(pv_or_archs,
                           " Archive '"
                           + archive.getUrl()
                           + "' (" + archive.getKey()
                           + ", " + archive.getName()
                           + ")");
            }
            @Override
            public void handleDrop(IProcessVariable name,
                            IArchiveDataSource archive, DropTargetEvent event)
            {
                appendText(pv_or_archs,
                                " PV '" + name.getName() + "' & "
                                + " Archive '"
                                + archive.getUrl()
                                + "' (" + archive.getKey()
                                + ", " + archive.getName()
                                + ")");
            }
            @Override
            public void handleDrop(String name, DropTargetEvent event)
            {
                appendText(pv_or_archs, " Text " + name);
            }
        };
        ISelectionProvider combined_data = new DummyCombinedSelectionProvider();
        new ProcessVariableWithArchiveDragSource(combined, combined_data);
    }

    public static void main(String[] args)
    {
        Display display = new Display();
        final Shell shell = new Shell(display);
    
        DragDropDemo demo = new DragDropDemo();
        demo.createGUI(shell);    
        demo.hookDragAndDrop();
        
        shell.setBounds(50, 100, 700, 400);
        shell.open();
        while (!shell.isDisposed())
        {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }
}
