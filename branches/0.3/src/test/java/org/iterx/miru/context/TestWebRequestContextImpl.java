/*
  org.iterx.miru.context.TestWebRequestContextImpl

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

package org.iterx.miru.context;

import java.io.Reader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.nio.charset.Charset;

import java.net.URI;
import java.net.URISyntaxException;

import junit.framework.TestCase;

import org.iterx.miru.context.WebRequestContextImpl;

public class TestWebRequestContextImpl extends TestCase {
    
    private static final String ENCODING = System.getProperty("file.encoding");
  
    public void testConstructors() throws IOException {
	WebRequestContextImpl request;
	Reader reader;
	
	request = new WebRequestContextImpl(System.in);
	assertNotNull(request);
	assertEquals(System.in, request.getInputStream());
	assertEquals(null, request.getCharacterEncoding());

	request = new WebRequestContextImpl(System.in, ENCODING);
	assertNotNull(request);
	assertEquals(System.in, request.getInputStream());
	assertTrue(encodingEquals(ENCODING, request.getCharacterEncoding()));

	request = new WebRequestContextImpl((reader = new InputStreamReader(System.in)));
	assertNotNull(request);
	assertEquals(reader, request.getReader());

        System.out.println(ENCODING);
        System.out.println(request.getCharacterEncoding());
	assertTrue(encodingEquals(ENCODING, request.getCharacterEncoding()));
	
	request = new WebRequestContextImpl
	    ((reader = new InputStreamReader(System.in, ENCODING)));
	assertNotNull(request);
	assertEquals(reader, request.getReader());
	assertTrue(encodingEquals(ENCODING, request.getCharacterEncoding()));
    }

    public void testURIAccessors() throws URISyntaxException {
	WebRequestContextImpl request;
	URI uri;

	uri = new URI("/dev/null");

	request = new WebRequestContextImpl(System.in);
	assertNull(request.getURI());

	request.setURI(uri);
	assertEquals(uri, request.getURI());

	request.setURI(null);
	assertNull(request.getURI());
    }

    public void testPropertyAccessors() {
	WebRequestContextImpl request;
	String value;

	request = new WebRequestContextImpl(System.in);
	assertNull(request.getProperty("key"));

	request.setProperty("key", (value = "value"));
	assertEquals(value, request.getProperty("key"));

	request.setProperty("key", null);
	assertNull(request.getProperty("key"));

	request.setProperty("KEY", (value = "value"));
	assertEquals(value, request.getProperty("key"));
	assertEquals(value, request.getProperty("kEy"));
	assertEquals(value, request.getProperty("KEY"));
	request.setProperty("KeY", null);
	assertNull(request.getProperty("KEY"));
    }

    public void testParameterAccessors() {
	WebRequestContextImpl request;
	String[] values;
	String value;

	request = new WebRequestContextImpl(System.in);
	assertEquals(0, (request.getParameterNames()).length);
	assertNull(request.getParameter("a"));


	request.setParameter("a", (value = "value"));
	assertEquals(value, request.getParameter("a"));
	assertEquals(1, (values = request.getParameterValues("a")).length);
	assertEquals(value, values[0]);

	request.setParameterValues("b", (values = new String[]{ value, ""}));
	assertEquals(values, request.getParameterValues("b"));
	assertEquals(value, request.getParameter("b"));

	assertEquals(2, (values = request.getParameterNames()).length);
	assertEquals("a", values[0]);
	assertEquals("b", values[1]);

	request.setParameter("a", null);
	assertNull(request.getParameter("a"));
	assertEquals(1, (values = request.getParameterNames()).length);
	assertEquals(value, request.getParameter("b"));

	request.setParameter("b", null);
	assertNull(request.getParameter("b"));
	assertEquals(0, (request.getParameterNames()).length);
    }

    public void testContentLengthAccessors() {
	WebRequestContextImpl request;

	request = new WebRequestContextImpl(System.in);
	assertEquals(-1, request.getContentLength());
    }

    public void testContentTypeAccessors() {
	WebRequestContextImpl request;
	String value;

	request = new WebRequestContextImpl(System.in);
	assertNull(request.getContentType());

	request.setContentType((value = "text/xml"));
	assertEquals(value, request.getContentType());

	request.setContentType(null);
	assertNull(request.getContentType());
    }

    public void testCharacterEncodingAccessors() {
	WebRequestContextImpl request;

	request = new WebRequestContextImpl(System.in);
	assertNull(request.getCharacterEncoding());
    }

    public void testInputStream() throws IOException {
	WebRequestContextImpl request;
	Reader reader;

	request = new WebRequestContextImpl(System.in);
	assertEquals(System.in, request.getInputStream());
	assertNull(request.getReader());


	request = new WebRequestContextImpl
	    ((reader = new InputStreamReader(System.in)));
	assertNull(request.getInputStream());
	assertEquals(reader, request.getReader());
	assertNull(request.getInputStream());
    }

    public void testReader() throws IOException {
	WebRequestContextImpl request;
	Reader reader;

	request = new WebRequestContextImpl
	    ((reader = new InputStreamReader(System.in)));
	assertNull(request.getInputStream());
	assertEquals(reader, request.getReader());
	assertNull(request.getInputStream());

	request = new WebRequestContextImpl(System.in);
	assertNotNull(request.getReader());
	assertNull(request.getInputStream());
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
