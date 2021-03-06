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

//import org.csstudio.platform.model.IArchiveDataSource;
import org.csstudio.platform.model.IProcessVariable;
//import org.csstudio.platform.model.IProcessVariableWithArchive;
//import org.csstudio.platform.model.IProcessVariableWithSample;
import org.csstudio.platform.model.IProcessVariableWithSamples;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Control;

/** Support receiving Process Variable with sample Data.
 *  (Using the design of the datatype implementations from Kay Kasemir)
 *  <p>
 *  @see ProcessVariableDropTarget
 *  @author Jan Hatje und Helge Rickens
 */
public abstract class ProcessVariableWithSamplesDropTarget extends DropTargetAdapter
{
    /**
     * Drop target.
     */
    private DropTarget _target;
	
    /** Cause that '_target' GUI element to accept PV names with 
     *  sample data sources via Drag-and-drop.
     *  @param target The SWT GUI element.
     */
	public ProcessVariableWithSamplesDropTarget(final Control target){
		this._target = new DropTarget(target, DND.DROP_MOVE | DND.DROP_COPY);
		this._target.setTransfer(new Transfer[]
        {   // Order matters:
            // Ideally, we receive PV-with-Sample:
            ProcessVariableWithSamplesTransfer.getInstance(),
            // Less desirable alternatives follow:
            ProcessVariableNameTransfer.getInstance()
        });
		this._target.addDropListener(this);
	}
    
    /** Callback for each dropped PV.
     *  Gets invoked if just a PV was dropped, without sample data.
     *  @param name The dropped PV name
     *  @param event The original event, in case you need e.g. the x/y position.
     */
    public abstract void handleDrop(IProcessVariable name,
                                    DropTargetEvent event);

    /** Callback for a dropped PV with sample data source.
     *  Gets invoked if a PV together with data source was dropped.
     *  @param name The dropped PV name
     *  @param event The original event in case you need details.
     */
    public abstract void handleDrop(IProcessVariableWithSamples name,
                                    DropTargetEvent event);
    
	/** Used internally by the system when a DnD operation enters the control.
     *  @see org.eclipse.swt.dnd.DropTargetListener#dragEnter(org.eclipse.swt.dnd.DropTargetEvent)
     *  @param event Drag enter event 
	 */
    @Override
	public final void dragEnter(final DropTargetEvent event){
		if ((event.operations & DND.DROP_COPY) != 0) {
            event.detail = DND.DROP_COPY;
        } else {
            event.detail = DND.DROP_NONE;
        }
	}

    /** Used internally by the system to drop the data.
     *  @see org.eclipse.swt.dnd.DropTargetListener#drop(org.eclipse.swt.dnd.DropTargetEvent)
     *  @param event drop
     */
    @Override
	public final void drop(final DropTargetEvent event){
		System.out.println("ProcessVariableWithSamplesDropTarget drop TargetEvent");
        if (event.data instanceof IProcessVariableWithSamples[]){
        	System.out.println("DropTargetEvent --> instanceof IProcessVariableWithSamples[]");
            IProcessVariableWithSamples[] names = (IProcessVariableWithSamples [])event.data;
            for (int i = 0; i < names.length; i++) {
                handleDrop(names[i], event);
            }
        }else if (event.data instanceof IProcessVariable[]){
        	System.out.println("DropTargetEvent --> instanceof IProcessVariable[]");
            IProcessVariable[] names = (IProcessVariable [])event.data;
            for (int i = 0; i < names.length; i++) {
                handleDrop(names[i], event);
            }
        }else{
        	System.out.println("DropTargetEvent else");
        }

	}
}
