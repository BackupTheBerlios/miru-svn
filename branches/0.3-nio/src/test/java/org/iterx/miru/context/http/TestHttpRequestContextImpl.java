/*
  org.iterx.miru.context.context.TestHttpRequestContextImpl

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

package org.iterx.miru.context.http;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.io.IOException;

import org.iterx.miru.io.stream.StreamSource;
import org.iterx.miru.context.stream.TestStreamRequestContextImpl;


public class TestHttpRequestContextImpl<T extends HttpRequestContextImpl> extends TestStreamRequestContextImpl<T> {

    public T createRequestContext(URI uri, StreamSource streamSource) {

        return (T) new HttpRequestContextImpl(uri, streamSource);
    }

    public void testConstructors() throws IOException {

        assertNotNull(new HttpRequestContextImpl(SOURCE_URI, streamSource));
        try {
            new HttpRequestContextImpl(SOURCE_URI, null);
            fail("streamSource == null");
        }
        catch(Exception e){}
        try {
            new HttpRequestContextImpl(null, streamSource);
            fail("uri == null");
        }
        catch(Exception e){}
    }

    public void testURIAccessors() throws URISyntaxException {
        T request;
        URI uri;

        uri = new URI("/dev/null");

        request = createRequestContext(SOURCE_URI, streamSource);
        assertEquals(SOURCE_URI, request.getURI());

        request.setURI(uri);
        assertEquals(uri, request.getURI());

        try {
            request.setURI(null);
            fail("uri == null");
        }
        catch(Exception e) {}
    }

    public void testPropertyAccessors() {
        T request;
        String value;

        request = createRequestContext(SOURCE_URI, streamSource);
        assertNull(request.getHeader("key"));

        request.setHeader("key", (value = "value"));
        assertEquals(value, request.getHeader("key"));

        request.setHeader("key", null);
        assertNull(request.getHeader("key"));

        request.setHeader("KEY", (value = "value"));
        assertEquals(value, request.getHeader("key"));
        assertEquals(value, request.getHeader("kEy"));
        assertEquals(value, request.getHeader("KEY"));
        request.setHeader("KeY", null);
        assertNull(request.getHeader("KEY"));
    }

    public void testParameterAccessors() {
        T request;
        String[] values;
        String value;

        request = createRequestContext(SOURCE_URI, streamSource);
        assertEquals(0, (request.getParameterNames()).length);
        assertNull(request.getParameter("a"));


        request.setParameter("a", (value = "value"));
        assertEquals(value, request.getParameter("a"));
        assertEquals(1, (values = request.getParameterValues("a")).length);
        assertEquals(value, values[0]);

        values = new String[]{ value, "" };
        request.setParameterValues("b", Arrays.asList(values));
        assertTrue(Arrays.equals(values, request.getParameterValues("b")));
        assertEquals(value, request.getParameter("b"));

        assertEquals(2, (values = request.getParameterNames()).length);
        assertEquals("a", values[0]);
        assertEquals("b", values[1]);

        request.setParameter("a", null);
        assertNull(request.getParameter("a"));
        assertEquals(1, (request.getParameterNames()).length);
        assertEquals(value, request.getParameter("b"));

        request.setParameter("b", null);
        assertNull(request.getParameter("b"));
        assertEquals(0, (request.getParameterNames()).length);

        request.addParameterValue("a", "value");
        assertEquals(1, (request.getParameterNames()).length);
        assertEquals(value, request.getParameter("a"));

        request.removeParameterValue("a", "value");
        assertEquals(0, (request.getParameterNames()).length);
    }



}
