/*
  org.iterx.miru.matcher.TestUriMatcher

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

package org.iterx.miru.matcher;

import junit.framework.TestCase;
import org.iterx.miru.context.MockProcessingContext;
import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.ResponseContext;
import org.iterx.miru.context.http.MockHttpRequestContext;
import org.iterx.miru.context.http.MockHttpResponseContext;

public class TestUriMatcher extends TestCase {

    private static final String ID         = "id";
    private static final String URI        = "http://localhost:8000/a/path?query_string";
    private static final Integer MASK_URI  = new Integer(UriMatcher.MASK_URI);
    private static final Integer MASK_PATH = new Integer(UriMatcher.MASK_PATH);
    private static final Integer MASK_HOST = new Integer(UriMatcher.MASK_SCHEME|UriMatcher.MASK_AUTHORITY);

    private static Object[][] TESTS = {
          { MASK_URI, "^$", null},
          { MASK_URI, "does-not-exist", null},
          { MASK_URI, ".*", new String[]{ URI }},
          { MASK_URI, "^http://", new String[]{ URI }},
          { MASK_URI, "?query_string$", new String[]{ URI }},
          { MASK_URI, "/a/path", new String[]{ URI }},
          { MASK_URI, "//(\\w*):(\\d*)/", new String[]{ URI, "localhost", "8000" }},
          { MASK_PATH, "localhost", null},
          { MASK_PATH, ".*", new String[]{ "/a/path" }},
          { MASK_PATH, "(path)", new String[]{ "/a/path", "path" }},
          { MASK_HOST, "/a/path", null },
          { MASK_HOST, "^http://", new String[]{ "http://localhost:8000" }},
          { MASK_HOST, "//(\\w*):(\\d*)", new String[]{ "http://localhost:8000", "localhost", "8000" }},
      };
  
    public void testConstructors() {

        assertEquals(UriMatcher.MASK_PATH, (new UriMatcher()).getMask());
        assertEquals(UriMatcher.MASK_URI, (new UriMatcher(UriMatcher.MASK_URI)).getMask());
    }


    public void testMaskAccessors() {
        UriMatcher matcher;

        matcher = new UriMatcher();
        assertEquals(UriMatcher.MASK_PATH, matcher.getMask());

        matcher.setMask(UriMatcher.MASK_URI);
        assertEquals(UriMatcher.MASK_URI, matcher.getMask());

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
        ProcessingContext<RequestContext, ResponseContext> processingContext;
        UriMatcher<RequestContext, ResponseContext> matcher;


        matcher = new UriMatcher<RequestContext, ResponseContext>(ID);
        processingContext = new MockProcessingContext(MockHttpRequestContext.newInstance(URI),
                                                      MockHttpResponseContext.newInstance());

        for(Object[] test : TESTS) {
            Object[] values;

            matcher.setMask(((Integer) test[0]).intValue());
            matcher.setPattern((String) test[1]);


            if((values = (String[]) test[2]) != null) {
                Matches matches;
                String[] strings;

                assertTrue(matcher.hasMatches(processingContext));
                assertNotNull(matches = matcher.getMatches(processingContext));
                assertNotNull(strings = matches.get(ID));
                assertEquals(values.length, strings.length);
                for(int j = 0; j < values.length; j++) {
                    assertEquals(values[j], strings[j]);
                }
            }
            else {
                assertFalse(matcher.hasMatches(processingContext));
                assertNull(matcher.getMatches(processingContext));
            }
        }
    }

}
