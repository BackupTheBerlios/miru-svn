/*
  org.iterx.miru.context.TestResponseContextImpl

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

import junit.framework.TestCase;

import org.iterx.miru.context.ResponseContext;
import org.iterx.miru.context.ResponseContextImpl;

public class TestResponseContextImpl extends TestCase {

    private static final String ENCODING = System.getProperty("file.encoding");

    public void testConstructors() throws IOException {
	ResponseContextImpl response;
	Writer writer;
	
	response = new ResponseContextImpl(System.out);
	assertNotNull(response);
	assertEquals(System.out, response.getOutputStream());
	assertEquals(null, response.getCharacterEncoding());
	

	response = new ResponseContextImpl(System.out, ENCODING);
	assertNotNull(response);
	assertEquals(System.out, response.getOutputStream());
	assertTrue(encodingEquals(ENCODING, response.getCharacterEncoding()));

	response = new ResponseContextImpl((writer = new OutputStreamWriter(System.out)));
	assertNotNull(response);
	assertEquals(writer, response.getWriter());
	assertTrue(encodingEquals(ENCODING, response.getCharacterEncoding()));
	
	response = new ResponseContextImpl
	    ((writer = new OutputStreamWriter(System.out, ENCODING)));
	assertNotNull(response);
	assertEquals(writer, response.getWriter());
	assertTrue(encodingEquals(ENCODING, response.getCharacterEncoding()));
    }

    public void testStatusAccessors() {
	ResponseContextImpl response;
	int status;

	response = new ResponseContextImpl(System.out);

	assertEquals(ResponseContext.OK, response.getStatus());	

	response.setStatus(ResponseContext.SERVER_ERROR);
	assertEquals(ResponseContext.SERVER_ERROR, response.getStatus());
    }


    public void testPropertyAccessors() {    
        ResponseContextImpl response;
        String value;

	response = new ResponseContextImpl(System.out);
        
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
	ResponseContextImpl response;
	int status;

	response = new ResponseContextImpl(System.out);

	assertEquals(-1, response.getContentLength());	

	response.setContentLength(4096);
	assertEquals(4096, response.getContentLength());
    }

    public void testContentTypeAccessors() {
	ResponseContextImpl response;
	String value;

	response = new ResponseContextImpl(System.out);	
	assertNull(response.getContentType());
	
	response.setContentType((value = "text/xml"));
	assertEquals(value, response.getContentType());
	
	response.setContentType(null);
	assertNull(response.getContentType());

    }

    public void testCharacterEncodingAccessors() {
	ResponseContextImpl response;
	String value;

	response = new ResponseContextImpl(System.out);	
	assertNull(response.getCharacterEncoding());
	
	response.setCharacterEncoding((value = ENCODING));
	assertTrue(encodingEquals(value, response.getCharacterEncoding()));
	
	response.setCharacterEncoding(null);
	assertNull(response.getCharacterEncoding());
    }


    public void testOutputStream() throws IOException {
	ResponseContextImpl response;
	Writer writer;
	
	response = new ResponseContextImpl(System.out);
	assertEquals(System.out, response.getOutputStream());
	assertNull(response.getWriter());

	response = new ResponseContextImpl
	    ((writer = new OutputStreamWriter(System.out)));
	assertNull(response.getOutputStream());
	assertEquals(writer, response.getWriter());
	assertNull(response.getOutputStream());
    }

    public void testWriter() throws IOException {
	ResponseContextImpl response;
	Writer writer;
	
	response = new ResponseContextImpl
	    ((writer = new OutputStreamWriter(System.out)));
	assertNull(response.getOutputStream());
	assertEquals(writer, response.getWriter());
	assertNull(response.getOutputStream());
	
	response = new ResponseContextImpl(System.out);
	assertNotNull(response.getWriter());
	assertNull(response.getOutputStream());
    }

    private static boolean encodingEquals(String encodingA, 
					  String encodingB) {

	return ((encodingA.replaceAll("[^a-zA-Z0-9]", "")).equalsIgnoreCase
		(encodingB.replaceAll("[^a-zA-Z0-9]", "")));
    }
}
