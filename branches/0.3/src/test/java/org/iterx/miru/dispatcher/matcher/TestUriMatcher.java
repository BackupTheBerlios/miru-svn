/*
  org.iterx.miru.dispatcher.matcher.TestUriMatcher

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

package org.iterx.miru.dispatcher.matcher;

import junit.framework.TestCase;
import org.iterx.miru.context.MockProcessingContext;
import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.http.MockHttpRequestContext;
import org.iterx.miru.context.http.MockHttpResponseContext;

public class TestUriMatcher extends TestCase {

    private static final String URI        = "http://localhost:8000/a/path?query_string";
    private static final Integer MASK_URI  = new Integer(UriMatcher.MASK_URI);
    private static final Integer MASK_PATH = new Integer(UriMatcher.MASK_PATH);
    private static final Integer MASK_HOST = new Integer(UriMatcher.MASK_SCHEME|UriMatcher.MASK_AUTHORITY);

    private static Object[][] TESTS = {
        { MASK_URI, "^$", null},
        { MASK_URI, "does-not-exist", null},
        { MASK_URI, ".*", new Object[]{ URI }},
        { MASK_URI, "^http://", new Object[]{ URI }},
        { MASK_URI, "?query_string$", new Object[]{ URI }},
        { MASK_URI, "/a/path", new Object[]{ URI }},
        { MASK_URI, "//(\\w*):(\\d*)/", new Object[]{ URI, "localhost", "8000" }},
        { MASK_PATH, "localhost", null},
        { MASK_PATH, ".*", new Object[]{ "/a/path" }},
        { MASK_PATH, "(path)", new Object[]{ "/a/path", "path" }},
        { MASK_HOST, "/a/path", null },
        { MASK_HOST, "^http://", new Object[]{ "http://localhost:8000" }},
        { MASK_HOST, "//(\\w*):(\\d*)", new Object[]{ "http://localhost:8000", "localhost", "8000" }},
    };

    public void testConstructors() {

        assertEquals(UriMatcher.MASK_URI, (new UriMatcher()).getMask());
        assertEquals(UriMatcher.MASK_PATH, (new UriMatcher(UriMatcher.MASK_PATH)).getMask());
    }


    public void testMaskAccessors() {
        UriMatcher matcher;

        matcher = new UriMatcher();
        assertEquals(UriMatcher.MASK_URI, matcher.getMask());

        matcher.setMask(UriMatcher.MASK_PATH);
        assertEquals(UriMatcher.MASK_PATH, matcher.getMask());

        matcher.setMask(UriMatcher.MASK_SCHEME|UriMatcher.MASK_PATH);
        assertEquals(UriMatcher.MASK_SCHEME|UriMatcher.MASK_PATH, matcher.getMask());
    }

    public void testPatternAccessors() {
        UriMatcher matcher;

        matcher = new UriMatcher();

        assertEquals(".*", matcher.getPattern());

        matcher.setPattern("\\w+");
        assertEquals("\\w+", matcher.getPattern());

        try {
            matcher.setPattern(null);
            fail("Failed to detect 'null' pattern.");
        }
        catch(IllegalArgumentException e) {}

        try {
            matcher.setPattern("\\q");
            fail("Failed to detect invalid pattern.");
        }
        catch(IllegalArgumentException e) {}

    }

    public void testMatches() {
        ProcessingContext context;
        UriMatcher matcher;


        matcher = new UriMatcher();
        context = new MockProcessingContext(new MockHttpRequestContext(URI),
                                            new MockHttpResponseContext());

        for(int i = 0; i < TESTS.length; i++) {
            Object[] values;

            matcher.setMask(((Integer) TESTS[i][0]).intValue());
            matcher.setPattern((String) TESTS[i][1]);


            if((values = (Object[]) TESTS[i][2]) != null) {
                Object[] matches;

                assertTrue(matcher.hasMatches(context));
                assertNotNull(matches = matcher.getMatches(context));
                assertEquals(values.length, matches.length);
                for(int j = 0; j < values.length; j++) {
                    assertEquals(values[j], matches[j]);
                }
            }
            else {
                assertFalse(matcher.hasMatches(context));
                assertNull(matcher.getMatches(context));
            }
        }
    }

}
