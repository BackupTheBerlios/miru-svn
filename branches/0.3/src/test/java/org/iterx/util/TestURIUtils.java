/*
  org.iterx.util.TestURIUtils

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

import java.net.URI;

import junit.framework.TestCase;

public class TestURIUtils extends TestCase {

    private static final URI BASE = URI.create("base:///");
    private static final URI PATH = URI.create("/");

    public void testResolve() {
        String[] values;
        String uri;

        uri = PATH.toString();
        assertEquals(PATH, URIUtils.resolve(uri, null));
        assertEquals(BASE, URIUtils.resolve(uri, BASE));

        uri = "{0}";
        values = new String[] { PATH.toString() };
        assertEquals(PATH, URIUtils.resolve(uri, null, values));
        assertEquals(BASE, URIUtils.resolve(uri, BASE, values));

        try {
            URIUtils.resolve("{0}", null);
            fail("Failed to detect malformed uri.");
        }
        catch(Exception e) {}

        try {
            URIUtils.resolve("{0}", BASE);
            fail("Failed to detect malformed uri.");
        }
        catch(Exception e) {}
    }


}
