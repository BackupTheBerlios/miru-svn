/*
  org.iterx.miru.context.http.TestHttpResponseContextImpl

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
import java.io.Writer;
import java.io.OutputStreamWriter;

import java.nio.charset.Charset;

import junit.framework.TestCase;

public class TestHttpResponseContextImpl extends TestCase {

    private static final String ENCODING = System.getProperty("file.encoding");

    public void testConstructors() throws IOException {
        HttpResponseContextImpl response;
        Writer writer;

        response = new HttpResponseContextImpl(System.out);
        assertNotNull(response);
        assertEquals(System.out, response.getOutputStream());
        assertEquals(null, response.getCharacterEncoding());


        response = new HttpResponseContextImpl(System.out, ENCODING);
        assertNotNull(response);
        assertEquals(System.out, response.getOutputStream());
        assertTrue(encodingEquals(ENCODING, response.getCharacterEncoding()));

        response = new HttpResponseContextImpl((writer = new OutputStreamWriter(System.out)));
        assertNotNull(response);
        assertEquals(writer, response.getWriter());
        assertTrue(encodingEquals(ENCODING, response.getCharacterEncoding()));

        response = new HttpResponseContextImpl
            ((writer = new OutputStreamWriter(System.out, ENCODING)));
        assertNotNull(response);
        assertEquals(writer, response.getWriter());
        assertTrue(encodingEquals(ENCODING, response.getCharacterEncoding()));
    }

    public void testStatusAccessors() {
        HttpResponseContextImpl response;

        response = new HttpResponseContextImpl(System.out);

        assertEquals(HttpResponseContext.OK, response.getStatus());

        response.setStatus(HttpResponseContext.SERVER_ERROR);
        assertEquals(HttpResponseContext.SERVER_ERROR, response.getStatus());
    }


    public void testPropertyAccessors() {
        HttpResponseContextImpl response;
        String value;

        response = new HttpResponseContextImpl(System.out);

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

    public void testContentLengthAccessors() {
        HttpResponseContextImpl response;

        response = new HttpResponseContextImpl(System.out);

        assertEquals(-1, response.getContentLength());

        response.setContentLength(4096);
        assertEquals(4096, response.getContentLength());
    }

    public void testContentTypeAccessors() {
        HttpResponseContextImpl response;
        String value;

        response = new HttpResponseContextImpl(System.out);
        assertNull(response.getContentType());

        response.setContentType((value = "text/xml"));
        assertEquals(value, response.getContentType());

        response.setContentType(null);
        assertNull(response.getContentType());

    }

    public void testCharacterEncodingAccessors() {
        HttpResponseContextImpl response;
        String value;

        response = new HttpResponseContextImpl(System.out);
        assertNull(response.getCharacterEncoding());

        response.setCharacterEncoding((value = ENCODING));
        assertTrue(encodingEquals(value, response.getCharacterEncoding()));

        response.setCharacterEncoding(null);
        assertNull(response.getCharacterEncoding());
    }


    public void testOutputStream() throws IOException {
        HttpResponseContextImpl response;
        Writer writer;

        response = new HttpResponseContextImpl(System.out);
        assertEquals(System.out, response.getOutputStream());
        assertNull(response.getWriter());

        response = new HttpResponseContextImpl
            ((writer = new OutputStreamWriter(System.out)));
        assertNull(response.getOutputStream());
        assertEquals(writer, response.getWriter());
        assertNull(response.getOutputStream());
    }

    public void testWriter() throws IOException {
        HttpResponseContextImpl response;
        Writer writer;

        response = new HttpResponseContextImpl
            ((writer = new OutputStreamWriter(System.out)));
        assertNull(response.getOutputStream());
        assertEquals(writer, response.getWriter());
        assertNull(response.getOutputStream());

        response = new HttpResponseContextImpl(System.out);
        assertNotNull(response.getWriter());
        assertNull(response.getOutputStream());
    }

    private static boolean encodingEquals(String encodingA,
                                          String encodingB) {

        try {
            return (Charset.forName(encodingA)).equals
                (Charset.forName(encodingB));
        }
        catch(Exception e) {}

        return false;
    }
}
