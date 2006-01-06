/*
  org.iterx.miru.context.stream.TestStreamRequestContextImpl

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

import java.net.URI;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.iterx.miru.io.stream.StreamSource;
import org.iterx.miru.io.stream.StreamSourceImpl;
import junit.framework.TestCase;

public class TestStreamRequestContextImpl<T extends StreamRequestContextImpl> extends TestCase {

    public static final String SOURCE_ENCODING  = System.getProperty("file.encoding");
    public static final String SOURCE_TYPE      = "text/plain";
    public static final URI SOURCE_URI          = URI.create("/");
    public static final byte[] SOURCE_DATA      = "HELLO WORLD!".getBytes();

    public StreamSourceImpl streamSource;
    private InputStream inputStream;

    protected void setUp() throws Exception {

        inputStream = new ByteArrayInputStream(SOURCE_DATA);
        streamSource = new StreamSourceImpl(inputStream,
                                            SOURCE_ENCODING);
        streamSource.setContentLength(SOURCE_DATA.length);
        streamSource.setContentType("text/plain");
    }

    protected void tearDown() throws Exception {

        streamSource = null;
        inputStream = null;
    }

    public void testConstructors() throws IOException {

           assertNotNull(new StreamRequestContextImpl(SOURCE_URI, streamSource));
           try {
               new StreamRequestContextImpl(SOURCE_URI, null);
               fail("streamSource == null");
           }
           catch(Exception e){}
           try {
               new StreamRequestContextImpl(null, streamSource);
               fail("uri == null");
           }
           catch(Exception e){}
    }

    public T createRequestContext(URI uri, StreamSource streamSource) {

            return (T) new StreamRequestContextImpl(uri, streamSource);
        }

    public void testContentLengthAccessors() {
        T request;

        request = createRequestContext(SOURCE_URI, streamSource);
        assertEquals(SOURCE_DATA.length, request.getContentLength());
    }

    public void testContentTypeAccessors() {
        T request;

        request = createRequestContext(SOURCE_URI, streamSource);
        assertEquals(SOURCE_TYPE, request.getContentType());
    }

    public void testCharacterEncodingAccessors() {
        T request;

        request = createRequestContext(SOURCE_URI, streamSource);
        assertTrue(encodingEquals(SOURCE_ENCODING,
                                  request.getCharacterEncoding()));
    }

    public void testInputStream() throws IOException {
        T request;

        request = createRequestContext(SOURCE_URI, streamSource);
        assertEquals(inputStream, request.getInputStream());
        assertNull(request.getReader());
        assertEquals(inputStream, request.getInputStream());
    }

    public void testReader() throws IOException {
        T request;

        request = createRequestContext(SOURCE_URI, streamSource);
        assertNotNull(request.getReader());
        assertNull(request.getInputStream());
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
