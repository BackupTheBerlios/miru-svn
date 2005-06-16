/*
  org.iterx.miru.context.TestWebResponseContextImpl

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

import java.io.IOException;
import java.io.Writer;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.nio.charset.Charset;

import junit.framework.TestCase;

import org.iterx.miru.context.WebResponseContext;
import org.iterx.miru.context.WebResponseContextImpl;

public class TestWebResponseContextImpl extends TestCase {

    private static final String ENCODING = System.getProperty("file.encoding");

    public void testConstructors() throws IOException {
	WebResponseContextImpl response;
	Writer writer;
	
	response = new WebResponseContextImpl(System.out);
	assertNotNull(response);
	assertEquals(System.out, response.getOutputStream());
	assertEquals(null, response.getCharacterEncoding());
	

	response = new WebResponseContextImpl(System.out, ENCODING);
	assertNotNull(response);
	assertEquals(System.out, response.getOutputStream());
	assertTrue(encodingEquals(ENCODING, response.getCharacterEncoding()));

	response = new WebResponseContextImpl((writer = new OutputStreamWriter(System.out)));
	assertNotNull(response);
	assertEquals(writer, response.getWriter());
	assertTrue(encodingEquals(ENCODING, response.getCharacterEncoding()));
	
	response = new WebResponseContextImpl
	    ((writer = new OutputStreamWriter(System.out, ENCODING)));
	assertNotNull(response);
	assertEquals(writer, response.getWriter());
	assertTrue(encodingEquals(ENCODING, response.getCharacterEncoding()));
    }

    public void testStatusAccessors() {
	WebResponseContextImpl response;
	int status;

	response = new WebResponseContextImpl(System.out);

	assertEquals(WebResponseContext.OK, response.getStatus());	

	response.setStatus(WebResponseContext.SERVER_ERROR);
	assertEquals(WebResponseContext.SERVER_ERROR, response.getStatus());
    }


    public void testPropertyAccessors() {    
        WebResponseContextImpl response;
        String value;

	response = new WebResponseContextImpl(System.out);
        
        assertNull(response.getProperty("a"));
        response.setProperty("a", (value = "value"));
        assertEquals(value, response.getProperty("a"));
        response.setProperty("b", (value = "value"));
        assertEquals(value, response.getProperty("b"));        
        response.setProperty("b", (value = ""));
        assertEquals(value, response.getProperty("b"));        
        response.setProperty("a", null);
        assertNull(response.getProperty("a"));
        response.setProperty("b", null);
        assertNull(response.getProperty("b"));
    }

    public void testContentLengthAccessors() {
	WebResponseContextImpl response;
	int status;

	response = new WebResponseContextImpl(System.out);

	assertEquals(-1, response.getContentLength());	

	response.setContentLength(4096);
	assertEquals(4096, response.getContentLength());
    }

    public void testContentTypeAccessors() {
	WebResponseContextImpl response;
	String value;

	response = new WebResponseContextImpl(System.out);	
	assertNull(response.getContentType());
	
	response.setContentType((value = "text/xml"));
	assertEquals(value, response.getContentType());
	
	response.setContentType(null);
	assertNull(response.getContentType());

    }

    public void testCharacterEncodingAccessors() {
	WebResponseContextImpl response;
	String value;

	response = new WebResponseContextImpl(System.out);	
	assertNull(response.getCharacterEncoding());
	
	response.setCharacterEncoding((value = ENCODING));
	assertTrue(encodingEquals(value, response.getCharacterEncoding()));
	
	response.setCharacterEncoding(null);
	assertNull(response.getCharacterEncoding());
    }


    public void testOutputStream() throws IOException {
	WebResponseContextImpl response;
	Writer writer;
	
	response = new WebResponseContextImpl(System.out);
	assertEquals(System.out, response.getOutputStream());
	assertNull(response.getWriter());

	response = new WebResponseContextImpl
	    ((writer = new OutputStreamWriter(System.out)));
	assertNull(response.getOutputStream());
	assertEquals(writer, response.getWriter());
	assertNull(response.getOutputStream());
    }

    public void testWriter() throws IOException {
	WebResponseContextImpl response;
	Writer writer;
	
	response = new WebResponseContextImpl
	    ((writer = new OutputStreamWriter(System.out)));
	assertNull(response.getOutputStream());
	assertEquals(writer, response.getWriter());
	assertNull(response.getOutputStream());
	
	response = new WebResponseContextImpl(System.out);
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
