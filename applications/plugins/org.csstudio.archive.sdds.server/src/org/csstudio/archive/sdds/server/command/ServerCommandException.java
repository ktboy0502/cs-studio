
/*
 * Copyright (c) 2010 Stiftung Deutsches Elektronen-Synchrotron,
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
 */

package org.csstudio.archive.sdds.server.command;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * @author Markus Moeller
 *
 */
public class ServerCommandException extends Exception {

    /** Generated serial version id */
    private static final long serialVersionUID = 1L;

    private int errorNumber;

    /**
     *
     */
    public ServerCommandException() {
        super();
    }

    /**
     *
     * @param err
     */
    public ServerCommandException(final int err) {
        super();
        this.errorNumber = err;
    }

    /**
     * @param message
     */
    public ServerCommandException(@Nonnull final String message) {
        super(message);
        this.errorNumber = 0;
    }

    /**
     *
     * @param message
     * @param err
     */
    public ServerCommandException(@Nonnull final String message, final int err) {
        super(message);
        this.errorNumber = err;
    }

    /**
     * @param cause
     */
    public ServerCommandException(@Nullable final Throwable cause) {
        super(cause);
        this.errorNumber = 0;
    }

    /**
     *
     * @param cause
     * @param err
     */
    public ServerCommandException(@Nullable final Throwable cause, final int err) {
        super(cause);
        this.errorNumber = err;
    }

    /**
     * @param message
     * @param cause
     */
    public ServerCommandException(@Nonnull final String message,
                                  @Nullable final Throwable cause) {
        super(message, cause);
        this.errorNumber = 0;
    }

    /**
     *
     * @param message
     * @param cause
     * @param err
     */
    public ServerCommandException(@Nonnull final String message,
                                  @Nullable final Throwable cause,
                                  final int err) {
        super(message, cause);
        this.errorNumber = err;
    }

    /**
     * Returns the error number of this Exception.
     *
     * @return The error number
     */
    public int getErrorNumber() {
        return errorNumber;
    }
}
