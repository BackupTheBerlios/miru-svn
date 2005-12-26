/*
  org.iterx.util.SystemUtils

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public
  License along with this library; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

  Copyright (C)2004-2005 Darren Graves <darren@iterx.org>
  All Rights Reserved.
*/

package org.iterx.util;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class SystemUtils {

    private static final Log LOGGER = LogFactory.getLog(SystemUtils.class);

    private static final String SERVICES = "META-INF/services/";

    private SystemUtils() {}

    public static String getProperty(String key) {
        String value;

        if((value = System.getProperty(key)) == null) {
            ClassLoader classLoader;
            URL url;

            classLoader = (SystemUtils.class).getClassLoader();
            if((url = classLoader.getResource(SERVICES + key)) != null) {
                try {
                    value = (String) url.getContent();
                }
                catch(IOException e) {
                    LOGGER.warn("Failed to retrieve property '" +
                                SERVICES + key + "'.", e);
                }
            }
        }
        return value;
    }

    public static void setProperty(String key, String value) {

        if(value == null) System.clearProperty(key);
        else System.setProperty(key, value);
    }

}
