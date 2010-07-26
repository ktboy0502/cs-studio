/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.trends.databrowser.preferences;

import java.util.ArrayList;

import org.csstudio.platform.logging.CentralLogger;
import org.csstudio.trends.databrowser.Activator;
import org.csstudio.trends.databrowser.model.ArchiveDataSource;
import org.csstudio.trends.databrowser.model.ArchiveRescale;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;

/** Helper for reading preference settings
 *  
 *  @author Kay Kasemir
 */
@SuppressWarnings("nls")
public class Preferences
{
    /** Regular expression for separator between list items */
    static final String ITEM_SEPARATOR_RE = "\\*";
    
    /** Regular expression for separator between components within an item */
    static final String COMPONENT_SEPARATOR_RE = "\\|";

    /** Separator between list items */
    static final String ITEM_SEPARATOR = "*";
    
    /** Separator between components within an item */
    static final String COMPONENT_SEPARATOR = "|";

    /** Preference tags.
     *  For explanation of the settings see preferences.ini
     */
    final public static String TIME_SPAN = "time_span",
                               SCAN_PERIOD = "scan_period",
                               BUFFER_SIZE = "live_buffer_size",
                               UPDATE_PERIOD = "update_period",
                               LINE_WIDTH = "line_width",
                               ARCHIVE_FETCH_DELAY = "archive_fetch_delay",
                               PLOT_BINS = "plot_bins",
                               URLS = "urls",
                               ARCHIVES = "archives",
                               ARCHIVE_RESCALE = "archive_rescale";
    
    public static double getTimeSpan()
    {
        final IPreferencesService prefs = Platform.getPreferencesService();
        if (prefs == null) // Allow some JUnit tests without prefs
            return 60.0;
        return prefs.getDouble(Activator.PLUGIN_ID, TIME_SPAN, 60.0*60.0, null);
    }

    public static double getScanPeriod()
    {
        final IPreferencesService prefs = Platform.getPreferencesService();
        return prefs.getDouble(Activator.PLUGIN_ID, SCAN_PERIOD, 1.0, null);
    }

    public static int getLiveSampleBufferSize()
    {
        final IPreferencesService prefs = Platform.getPreferencesService();
        if (prefs == null) // Allow some JUnit tests without prefs
            return 5000;
        return prefs.getInt(Activator.PLUGIN_ID, BUFFER_SIZE, 5000, null);
    }

    public static double getUpdatePeriod()
    {
        final IPreferencesService prefs = Platform.getPreferencesService();
        if (prefs == null) // Allow some JUnit tests without prefs
            return 1.0;
        return prefs.getDouble(Activator.PLUGIN_ID, UPDATE_PERIOD, 1.0, null);
    }

    public static int getLineWidths()
    {
        final IPreferencesService prefs = Platform.getPreferencesService();
        if (prefs == null)
            return 2;
        return prefs.getInt(Activator.PLUGIN_ID, LINE_WIDTH, 2, null);
    }

    public static long getArchiveFetchDelay()
    {
        final IPreferencesService prefs = Platform.getPreferencesService();
        return prefs.getLong(Activator.PLUGIN_ID, ARCHIVE_FETCH_DELAY, 1000, null);
    }

    public static int getPlotBins()
    {
        final IPreferencesService prefs = Platform.getPreferencesService();
        return prefs.getInt(Activator.PLUGIN_ID, PLOT_BINS, 800, null);
    }

    public static String[] getArchiveServerURLs()
    {
        final IPreferencesService prefs = Platform.getPreferencesService();
        final String urls = prefs.getString(Activator.PLUGIN_ID, URLS, "", null).trim();
        if (urls.length() <= 0)
            return new String[0];
        return urls.split("\\*");
    }

    public static ArchiveDataSource[] getArchives()
    {
        final ArrayList<ArchiveDataSource> archives = new ArrayList<ArchiveDataSource>();
        final IPreferencesService prefs = Platform.getPreferencesService();
        final String urls = prefs.getString(Activator.PLUGIN_ID, ARCHIVES, "", null);
        // data source specs are separated by '*'
        final String specs[] = urls.split(ITEM_SEPARATOR_RE);
        for (String spec : specs)
        {
            // Each spec is "<name>|<key>|<url>"
            if (spec.length() <= 0)
                continue;
            try
            {
                final String segs[] = spec.split(COMPONENT_SEPARATOR_RE);
                final String name = segs[0];
                final int key = Integer.parseInt(segs[1]);
                final String url = segs[2];
                archives.add(new ArchiveDataSource(url, key, name));
            }
            catch (Throwable ex)
            {
                throw new Error("Error in archive preference '" + spec + "'");
            }
        }
        return archives.toArray(new ArchiveDataSource[archives.size()]);
    }
    
    /** @return Archive rescale setting */
    static public ArchiveRescale getArchiveRescale()
    {
        final IPreferencesService prefs = Platform.getPreferencesService();
        if (prefs == null)
            return ArchiveRescale.STAGGER;
        try
        {
            return ArchiveRescale.valueOf(
                    prefs.getString(Activator.PLUGIN_ID, ARCHIVE_RESCALE,
                                    ArchiveRescale.STAGGER.name(), null));
        }
        catch (Throwable ex)
        {
            CentralLogger.getInstance().getLogger(Preferences.class)
                .error(ex.getMessage());
        }
        return ArchiveRescale.STAGGER;
    }
}
