
/*
 * Copyright (c) 2012 Stiftung Deutsches Elektronen-Synchrotron,
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
 *
 * $Id: DesyKrykCodeTemplates.xml,v 1.7 2010/04/20 11:43:22 bknerr Exp $
 */

package org.csstudio.ams.delivery.sms;

import org.csstudio.ams.delivery.status.IDeviceStatus;
import org.csstudio.ams.delivery.status.IWorkerStatus;

/**
 * @author mmoeller
 * @version 1.0
 * @since 23.05.2012
 */
public class SmsWorkerStatus implements IWorkerStatus, IDeviceStatus {

    private long pollingTime;
    private boolean smsSent;
    private long maxPollingDiff;
    
    public SmsWorkerStatus(long diff) {
        // The default value has to be set to true!
        smsSent = true;
        pollingTime = System.currentTimeMillis();
        maxPollingDiff = diff;
    }
    
    public synchronized void setSmsSent(boolean sent) {
        smsSent = sent;
    }
    
    @Override
    public synchronized void setLastPollingTime(long time) {
        pollingTime = time;
    }
    
    @Override
    public synchronized long getLastPollingTime() {
        return pollingTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOk() {
        if (!smsSent) {
            return false;
        }
        return ((System.currentTimeMillis() - pollingTime) < maxPollingDiff);
    }
}
