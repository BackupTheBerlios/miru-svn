/*
  org.iterx.miru.context.stream.TestStreamResponseContextImpl

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


package org.iterx.miru.context.stream;

import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import junit.framework.TestCase;
import org.iterx.miru.io.stream.StreamTargetImpl;
import org.iterx.miru.io.stream.StreamTarget;

public class TestStreamResponseContextImpl<T extends StreamResponseContextImpl> extends TestCase {

    public static final String TARGET_ENCODING = System.getProperty("file.encoding");
    public static final String TARGET_TYPE = "text/plain";
    public static final byte[] TARGET_DATA = "HELLO WORLD!".getBytes();

    public StreamTargetImpl streamTarget;
    private OutputStream outputStream;

    protected void setUp() throws Exception {

        outputStream = new ByteArrayOutputStream();
        streamTarget = new StreamTargetImpl(outputStream);
    }

    protected void tearDown() throws Exception {

        streamTarget = null;
        outputStream = null;
    }

    public void testConstructors() throws IOException {

        assertNotNull(new StreamResponseContextImpl(streamTarget));
        try {
            new StreamResponseContextImpl(null);
            fail("streamSource == null");
        }
        catch(Exception e) {}
    }

    public T createResponseContext(StreamTarget streamTarget) {

        return (T) new StreamResponseContextImpl(streamTarget);
    }

    public void testContentLengthAccessors() {
        T response;

        response = createResponseContext(streamTarget);
        assertEquals(-1, response.getContentLength());

        response.setContentLength(TARGET_DATA.length);
        assertEquals(TARGET_DATA.length, response.getContentLength());

        response.setContentLength(-1);
        assertEquals(-1, response.getContentLength());

        try {
            response.setContentLength(-2);
            fail("length < -1");
        }
        catch(Exception e) {}
    }

    public void testContentTypeAccessors() {
        T response;

        response = createResponseContext(streamTarget);
        assertNull(response.getContentType());

        response.setContentType(TARGET_TYPE);
        assertEquals(TARGET_TYPE, response.getContentType());

        response.setContentType(null);
        assertNull(response.getContentType());

    }

    public void testCharacterEncodingAccessors() {
        T response;

        response = createResponseContext(streamTarget);
        assertNull(response.getCharacterEncoding());

        response.setCharacterEncoding(TARGET_ENCODING);
        assertTrue(encodingEquals(TARGET_ENCODING, response.getCharacterEncoding()));

        response.setCharacterEncoding(null);
        assertNull(response.getCharacterEncoding());
    }


    public void testOutputStream() throws IOException {
        T response;

        response = createResponseContext(streamTarget);
        assertEquals(outputStream, response.getOutputStream());
        assertNull(response.getWriter());

        assertEquals(outputStream, response.getOutputStream());
    }

    public void testWriter() throws IOException {
        T response;

        response = createResponseContext(streamTarget);
        assertNotNull(response.getWriter());
        assertNull(response.getOutputStream());

        assertNotNull(response.getWriter());
    }

    private static boolean encodingEquals(String encodingA,
                                          String encodingB) {

        try {
            return (Charset.forName(encodingA)).equals(Charset.forName(encodingB));
        }
        catch(Exception e) {}

        return false;
    }

}
