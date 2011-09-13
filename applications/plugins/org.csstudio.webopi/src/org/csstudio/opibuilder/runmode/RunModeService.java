/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.opibuilder.runmode;

import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;

import org.csstudio.opibuilder.OPIBuilderPlugin;
import org.csstudio.opibuilder.runmode.OPIRunnerPerspective.Position;
import org.csstudio.opibuilder.util.ErrorHandlerUtil;
import org.csstudio.opibuilder.util.MacrosInput;
import org.csstudio.ui.util.thread.UIBundlingThread;
import org.eclipse.core.runtime.IPath;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.rap.swt.SWT;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.widgets.ExternalBrowser;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

/**The service for running of OPI.
 * @author Xihui Chen
 *
 */
public class RunModeService {

	public enum TargetWindow{
		NEW_WINDOW,
		SAME_WINDOW,
		RUN_WINDOW;
	}

	private IWorkbenchWindow runWorkbenchWindow;


	private static RunModeService instance;

	public static RunModeService getInstance(){
		if(instance == null)
			instance = new RunModeService();
		return instance;
	}


	public IWorkbenchWindow getRunWorkbenchWindow(){
		return runWorkbenchWindow;
	}
//
//	
//	
//
//	public void replaceActiveEditorContent(IRunnerInput input) throws PartInitException{
//		IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().
//			getActivePage().getActiveEditor();
//		activeEditor.init(activeEditor.getEditorSite(),input);
//	}
	
	public static void replaceOPIRuntimeContent(
			final IOPIRuntime opiRuntime, final IEditorInput input) throws PartInitException{
		opiRuntime.setOPIInput(input);
	}

	/**Run an OPI file with necessary parameters. This function should be called when open an OPI
	 * from another OPI.
	 * @param path
	 * @param targetWindow
	 * @param displayOpenManager
	 * @param macrosInput
	 */
	public void runOPI(IPath path, TargetWindow targetWindow, DisplayOpenManager displayOpenManager,
			MacrosInput macrosInput){
		runOPI(path, targetWindow, displayOpenManager, macrosInput, null);
	}

	/**Run an OPI file in the target window.
	 * @param path
	 * @param targetWindow
	 */
	public void runOPI(IPath path, TargetWindow targetWindow, Rectangle windowSize){
		runOPI(path, targetWindow, null, null, windowSize);
	}

	/**Run an OPI file.
	 * @param path the file to be ran. If displayModel is not null, this will be ignored.
	 * @param displayModel the display model to be ran. null for file input only.
	 * @param displayOpenManager the manager help to manage the opened displays. null if the OPI is not
	 * replacing the current active display.
	 */
	public void runOPI(final IPath path, final TargetWindow target,
			final DisplayOpenManager displayOpenManager, final MacrosInput macrosInput, final Rectangle windowBounds){
		final RunnerInput runnerInput = new RunnerInput(path, displayOpenManager, macrosInput);
		UIBundlingThread.getInstance().addRunnable(new Runnable(){
			 public void run() {

				IWorkbenchWindow targetWindow = null;
				switch (target) {
				case NEW_WINDOW:
					HttpServletRequest request = RWT.getRequest();
			    	String url = request.getRequestURL().toString();
			    	//to allow multilple browser instances, session id is not allowed
			    	if(url.contains(";jsessionid")) //$NON-NLS-1$
			    		url = url.substring(0, url.indexOf(";jsessionid"));//$NON-NLS-1$			    	
			    	ExternalBrowser.open("_blank", url+"?opi=" + path.toString(), SWT.None);
			    	
//					targetWindow = createNewWindow(windowBounds);
					break;
				case RUN_WINDOW:
					if(runWorkbenchWindow == null){
						runWorkbenchWindow = createNewWindow(windowBounds);
						runWorkbenchWindow.addPageListener(new IPageListener(){
							public void pageClosed(IWorkbenchPage page) {
								runWorkbenchWindow = null;
							}

							public void pageActivated(IWorkbenchPage page) {
							    // NOP
							}

							public void pageOpened(IWorkbenchPage page) {
                                // NOP
							}
						});
					}else{
						for(IEditorReference editor :
							runWorkbenchWindow.getActivePage().getEditorReferences()){
							try {
								if(editor.getEditorInput().equals(runnerInput))
									editor.getPage().closeEditor(editor.getEditor(false), false);
							} catch (PartInitException e) {
						         OPIBuilderPlugin.getLogger().log(Level.WARNING,
						                    "Cannot close editor", e); //$NON-NLS-1$
							}
						}
					}
					targetWindow = runWorkbenchWindow;
					break;
				case SAME_WINDOW:
				default:
					targetWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
					break;
				}



				if(targetWindow != null){
					try {
						Shell shell = targetWindow.getShell();
						if(shell.getMinimized())
							shell.setMinimized(false);
						targetWindow.getShell().forceActive();
						targetWindow.getShell().forceFocus();						
						targetWindow.getActivePage().openEditor(
								runnerInput, OPIRunner.ID); //$NON-NLS-1$
//						targetWindow.getShell().moveAbove(null);
					} catch (PartInitException e) {
						OPIBuilderPlugin.getLogger().log(Level.WARNING,
						        "Failed to run OPI " + path.lastSegment(), e);
					}
				}

				}
			});
	}


	public static void runOPIInView(final IPath path, 
			final DisplayOpenManager displayOpenManager, final MacrosInput macrosInput, final Position position){
		final RunnerInput runnerInput = new RunnerInput(path, displayOpenManager, macrosInput);
		UIBundlingThread.getInstance().addRunnable(new Runnable() {
			
			public void run() {
			    final IWorkbench workbench = PlatformUI.getWorkbench();
	            final IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
	            final IWorkbenchPage page = window.getActivePage();
				try {
					
					
					
					IViewReference[] viewReferences = page.getViewReferences();
					//If it is already opened.
					for(IViewReference viewReference : viewReferences){
						if(viewReference.getId().equals(OPIView.ID)){
							if(runnerInput.equals(
									((IOPIRuntime)viewReference.getView(true)).getOPIInput())){
								page.showView(
										OPIView.ID, viewReference.getSecondaryId(), IWorkbenchPage.VIEW_ACTIVATE);
								return;								
							}
						}
					}
					
					if(!(page.getPerspective().getId().equals(OPIRunnerPerspective.ID))){
						if(MessageDialog.openQuestion(window.getShell(), "Switch to OPI Runtime Perspective", 
								"To open the OPI View in expected position, you need to switch to OPI Runtime perspective."+
								"\nDo you want to switch to it now?"))
							try {
								workbench.showPerspective(OPIRunnerPerspective.ID, window);
							} catch (WorkbenchException e) {
								ErrorHandlerUtil.handleError(
										"Faile to switch to OPI Runtime perspective", e, false, true);
							}
							
					}
					
					//Open a new view
					IViewPart opiView = page.showView(
							OPIView.ID, OPIView.createNewInstance() + position.name(), IWorkbenchPage.VIEW_ACTIVATE);
					if(opiView instanceof OPIView){
						((OPIView)opiView).setOPIInput(runnerInput);
					}
				} catch (PartInitException e) {
					ErrorHandlerUtil.handleError(NLS.bind("Failed to run OPI {1} in view.", path), e);
				}
			}
		});
	}
	
	
	
	
	
	/**
	 * @param displayModel
	 */
	private IWorkbenchWindow createNewWindow(Rectangle windowBounds) {
		IWorkbenchWindow newWindow = null;
		try {
			newWindow =
				PlatformUI.getWorkbench().openWorkbenchWindow(OPIRunnerPerspective.ID, null); //$NON-NLS-1$
			if(windowBounds != null){
				if(windowBounds.x >=0 && windowBounds.y > 1)
					newWindow.getShell().setLocation(windowBounds.x, windowBounds.y);
				newWindow.getShell().setSize(windowBounds.width+45, windowBounds.height + 165);
			}

		} catch (WorkbenchException e) {
            OPIBuilderPlugin.getLogger().log(Level.WARNING, "Failed to open new window", e);
		}
		return newWindow;
	}



}