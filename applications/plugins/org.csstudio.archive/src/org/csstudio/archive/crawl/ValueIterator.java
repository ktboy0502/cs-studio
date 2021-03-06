package org.csstudio.archive.crawl;

import org.csstudio.data.values.IValue;

/** In principle this is like
 *  <code>Iterator&lt;Value&gt;[N]</code>, but allows next() to
 *  throw an exception.
 *  @author Kay Kasemir
 */
public interface ValueIterator
{
    /** Returns <tt>true</tt> if the iteration has more elements. (In other
     *  words, returns <tt>true</tt> if <tt>next</tt> would return an element
     *  rather than throwing an exception.)
     *
     *  @return <tt>true</tt> if the iterator has more elements.
     */
    public boolean hasNext();

    /** Returns the next element in the iteration.  Calling this method
     *  repeatedly until the {@link #hasNext()} method returns false will
     *  return each element in the underlying collection exactly once.
     *
     *  @return the next element in the iteration.
     *  @exception on Error in archive access
     */
    public IValue next() throws Exception;
}
