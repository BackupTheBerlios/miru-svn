/*
  org.iterx.miru.util.TestMiruUtils

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

 package org.iterx.miru.util;

import java.net.URI;

import junit.framework.TestCase;
import org.iterx.miru.matcher.Matches;

public class TestMiruUtils extends TestCase {

    private static final URI BASE = URI.create("baseUri:///");
    private static final URI PATH = URI.create("/");

    private static Matches MATCHES = new Matches();
    private static Object[][] TESTS = {
        { PATH, PATH.toString(), null, null },
        { BASE, PATH.toString(), BASE, null },
        { PATH, PATH.toString(), null, MATCHES },
        { BASE, PATH.toString(), BASE, MATCHES },
        { BASE, PATH.toString(), BASE, MATCHES },
        { URI.create("0"), "${key[0]}", null, MATCHES },
        { URI.create("1"), "${key[1]}", null, MATCHES },
        { URI.create(BASE + "0"), "${key[0]}", BASE, MATCHES },
        { URI.create(BASE + "1"), "${key[1]}", BASE, MATCHES },
        { null, null, null, null},
        { null, null, BASE, null},
        { null, null, BASE, MATCHES},
        { null, "${unknown[0]}", BASE, MATCHES },
        { null, "${unknown}", BASE, MATCHES },
        { null, "${key[3]}", BASE, MATCHES },
    };

    static {
        MATCHES.put("key", new String[] { "0", "1" });
    }

    public void testResolve() {

        for(int i = 0; i < TESTS.length; i++) {
            Object result;

            result = TESTS[i][0];
            if(result == null){
                try {
                    MiruUtils.resolve((String) TESTS[i][1],
                                      (URI) TESTS[i][2],
                                      (Matches) TESTS[i][3]);
                    fail("Failed to detect malformed parameter.");
                }
                catch(Exception e) {}
            }
            else assertEquals(result,
                              MiruUtils.resolve((String) TESTS[i][1],
                                                (URI) TESTS[i][2],
                                                (Matches) TESTS[i][3]));
        }

    }

}
