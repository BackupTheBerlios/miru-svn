/*
  org.iterx.miru.context.context.TestHttpResponseContextImpl

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

import java.io.IOException;

import org.iterx.miru.io.stream.StreamTarget;
import org.iterx.miru.context.stream.TestStreamResponseContextImpl;

public class TestHttpResponseContextImpl<T extends HttpResponseContextImpl> extends TestStreamResponseContextImpl<T> {

    private static final int TARGET_STATUS = HttpResponseContext.OK;

    public T createResponseContext(StreamTarget streamTarget) {

        return (T) new HttpResponseContextImpl(streamTarget);
    }

    public void testConstructors() throws IOException {
        HttpResponseContextImpl responseContext;

        responseContext = new HttpResponseContextImpl(streamTarget);
        assertNotNull(responseContext);
        assertEquals(TARGET_STATUS, responseContext.getStatus());

        try {
            new HttpResponseContextImpl(null);
            fail("streamSource == null");
        }
        catch(Exception e) {}
    }

    public void testStatusAccessors() {
        T response;

        response = createResponseContext(streamTarget);
        assertEquals(TARGET_STATUS, response.getStatus());

        response.setStatus(HttpResponseContext.SERVER_ERROR);
        assertEquals(HttpResponseContext.SERVER_ERROR, response.getStatus());
    }


    public void testPropertyAccessors() {
        T response;
        String value;

        response = createResponseContext(streamTarget);

        assertNull(response.getHeader("a"));
        response.setHeader("a", (value = "value"));
        assertEquals(value, response.getHeader("a"));
        response.setHeader("b", (value = "value"));
        assertEquals(value, response.getHeader("b"));
        response.setHeader("b", (value = ""));
        assertEquals(value, response.getHeader("b"));
        response.setHeader("a", null);
        assertNull(response.getHeader("a"));
        response.setHeader("b", null);
        assertNull(response.getHeader("b"));
    }

}
