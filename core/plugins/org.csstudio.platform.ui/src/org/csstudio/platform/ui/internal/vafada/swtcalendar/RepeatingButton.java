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
package org.csstudio.platform.ui.internal.vafada.swtcalendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

import java.util.ArrayList;

/**
 * Push button that repeats selection event based on timer.
 */
public class RepeatingButton extends Button {
    
    /**
     * 
     */
    public static final int DEFAULT_INITIAL_REPEAT_DELAY = 200; // Milliseconds
    
    /**
     * 
     */
    public static final int DEFAULT_REPEAT_DELAY = 50;          // Milliseconds
    
    /**
     * 
     */
    private int initialRepeatDelay = DEFAULT_INITIAL_REPEAT_DELAY;
    
    /**
     * 
     */
    private int repeatDelay = DEFAULT_REPEAT_DELAY;
    
    /**
     * 
     */
    private ArrayList<SelectionListener> selectionListeners =
        new ArrayList<SelectionListener>(3);
    
    /**
     * 
     */
    private Repeater repeater;

    /**
     * @param parent Parent container.
     * @param style  Button style.
     */
    public RepeatingButton(Composite parent, int style) {
        super(parent, style);
        addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent event) {
                cancelRepeater();

                if (event.button == 1) { // Left click
                    buttonPressed(event.stateMask, event.time);

                    repeater = new Repeater(event.stateMask);
                    getDisplay().timerExec(initialRepeatDelay, repeater);
                }
            }

            public void mouseUp(MouseEvent event) {
                if (event.button == 1) { // Left click
                    cancelRepeater();
                }
            }
        });

        addMouseTrackListener(new MouseTrackAdapter() {
            public void mouseExit(MouseEvent e) {
                cancelRepeater();
            }
        });
    }

    /**
     * 
     * 
     * @param listener 
     */
    public void addSelectionListener(SelectionListener listener) {
        selectionListeners.add(listener);
    }

    /**
     * 
     * 
     * @param listener 
     */
    public void removeSelectionListener(SelectionListener listener) {
        selectionListeners.remove(listener);
    }

    /**
     * @return Returns the initial repeat delay in milliseconds.
     */
    public int getInitialRepeatDelay() {
        return initialRepeatDelay;
    }

    /**
     * @param initialRepeatDelay The new initial repeat delay in milliseconds.
     */
    public void setInitialRepeatDelay(int initialRepeatDelay) {
        this.initialRepeatDelay = initialRepeatDelay;
    }

    /**
     * @return Returns the repeat delay in millisecons.
     */
    public int getRepeatDelay() {
        return repeatDelay;
    }

    /**
     * @param repeatDelay The new repeat delay in milliseconds.
     */
    public void setRepeatDelay(int repeatDelay) {
        this.repeatDelay = repeatDelay;
    }

    /**
     * 
     * 
     * @param time 
     * @param stateMask 
     */
    private void buttonPressed(int stateMask, int time) {
        SelectionListener[] listeners = new SelectionListener[selectionListeners.size()];
        selectionListeners.toArray(listeners);
        for (int i = 0; i < listeners.length; i++) {
            SelectionListener l = listeners[i];
            Event event = new Event();
            event.type = SWT.Selection;
            event.display = getDisplay();
            event.widget = this;
            event.stateMask = stateMask;
            event.time = time;
            l.widgetSelected(new SelectionEvent(event));
        }
    }

    /**
     * 
     */
    private void cancelRepeater() {
        if (repeater != null) {
            repeater.cancel();
            repeater = null;
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.widgets.Widget#checkSubclass()
     */
    /**
     * 
     */
    protected void checkSubclass() {
    }


    /**
     * 
     */
    private class Repeater implements Runnable {
        
        /**
         * 
         */
        private boolean canceled;
        
        /**
         * 
         */
        private int stateMask;

        /**
         * 
         * 
         * @param stateMask 
         */
        public Repeater(int stateMask) {
            super();
            this.stateMask = stateMask;
        }

        /* (non-Javadoc)
         * @see java.lang.Runnable#run()
         */
        /**
         * 
         */
        public void run() {
            if (!canceled) {
                buttonPressed(stateMask, (int) System.currentTimeMillis());

                getDisplay().timerExec(repeatDelay, this);
            }
        }

        /**
         * 
         */
        public void cancel() {
            canceled = true;
        }
    }
}