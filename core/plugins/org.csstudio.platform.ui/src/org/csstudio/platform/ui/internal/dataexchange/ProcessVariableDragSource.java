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
 package org.csstudio.platform.ui.internal.dataexchange;

import java.util.ArrayList;
import java.util.Iterator;

import org.csstudio.platform.model.IProcessVariable;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Control;

/** Support dragging Process Variable names out of a GUI item.
 *  <p>
 *  The user of this class has to provide the following:
 *  <ol>
 *  <li>A GUI item from which to drag data, for example a 'Table' or 'Tree'
 *      widget, but any SWT Control would work.
 *  <li>A selection provider that offers PVs via IProcessVariableName.
 *      In the table or tree example, that can be the associated JFace
 *      TableViewer or TreeViewer. In other cases, you might have to
 *      implement an ISelectionProvider.
 *  </ol>
 *  @author Kay Kasemir
 */
public class ProcessVariableDragSource implements DragSourceListener
{
    private ISelectionProvider selection_provider;
	private DragSource source;
    private ArrayList<IProcessVariable> pvs
        = new ArrayList<IProcessVariable>();
	
    /** Create a PV drag source for the given GUI item.
     *  <p>
     *  The current selection should implement or adapt to IProcessVariable,
     *  because that is how the PV name to 'drag' will be requested
     *  from the current selection.
     *  <p>
     *  @param control The GUI element from which "drag" should be supported.
     *  @param selection_provider Interface to whatever provides the current selection
     *  in your application.
     */
	public ProcessVariableDragSource(Control control,
            ISelectionProvider selection_provider)
	{
	    this.selection_provider = selection_provider;
		source = new DragSource(control, DND.DROP_COPY);
        source.setTransfer(new Transfer[]
        {
            ProcessVariableNameTransfer.getInstance(),
            TextTransfer.getInstance()
        });
		source.addDragListener(this);
	}

	/** Used internally by the system to ask if we allow drag-and-drop.
     *  <p>
     *  User should be able to leave this as is.
     *  <p>
     *  Check if there are any PV items in the current selection.
     *  Remember them, or cancel the drag request.
     */
	@SuppressWarnings("unchecked")
    public void dragStart(DragSourceEvent event)
	{
        pvs.clear();
        // Get all PVs from the current selection.
        ISelection sel = selection_provider.getSelection();
        if (sel instanceof IStructuredSelection)
        {  
            Iterator items = ((IStructuredSelection)sel).iterator();
            while (items.hasNext())
            {
                Object item = items.next();
                // Get PV ...
                if (item instanceof IProcessVariable)
                    pvs.add((IProcessVariable) item);
                else if (item instanceof IAdaptable)
                {   // or adapt to PV
                    IProcessVariable pv_item =
                        (IProcessVariable)
                        ((IAdaptable)item).getAdapter(IProcessVariable.class);
                    if (pv_item != null)
                        pvs.add(pv_item);
                }
                // else: Can't use that item
            }
        }
        if (pvs.size() < 1)
            event.doit = false;
	}

	/** Used by the system to ask for the drag-and-drop data.
     *  <p>
     *  User should be able to leave as is.
     */
	public void dragSetData(DragSourceEvent event)
	{
        if (ProcessVariableNameTransfer.getInstance()
                        .isSupportedType(event.dataType))
        {
            event.data = pvs;
        }
        else if (TextTransfer.getInstance().isSupportedType(event.dataType))
		{
            StringBuilder buf = new StringBuilder();
			for (int i = 0; i < pvs.size(); ++i)
			{
				if (i > 0)
					buf.append(", "); //$NON-NLS-1$
				buf.append(pvs.get(i).getName());
			}
			event.data = buf.toString();
		}
	}

	/** Used by the system to tell us that drag-and-drop is done.
     *  <p>
     *  User should be able to leave as is.
     */
	public void dragFinished(DragSourceEvent event)
	{
		pvs.clear();
	}
}
