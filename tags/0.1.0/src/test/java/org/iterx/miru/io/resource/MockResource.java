/*
  org.iterx.miru.io.MockResource

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

package org.iterx.miru.io.resource;

import java.io.Reader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.net.URI;

import org.iterx.miru.io.Resource;

public class MockResource implements Resource {

    private String type, encoding;
    private InputStream in;
    private Reader reader;
    private byte[] content;
    private URI uri;

    {
	content = new byte[0];
    }
    
    public MockResource() {

    }

    public MockResource(URI uri) {

	this.uri = uri;
    }

    public URI getURI() {

	return uri;
    }

    public void setURI(URI uri) {

	this.uri = uri;
    }

    public byte[] getContent() {

	return content;
    }

    public void setContent(byte[] content) {
	if(content == null) 
	    throw new IllegalArgumentException("content == null");
	
	this.content = content;
    }


    public boolean exists() {
	
	return true;
    }
    
    public void reset() {

	reader = null;
	in = null;
    }


    public String getProperty(String property) {

	return null;
    }

    public int getContentLength() {
	
	return content.length;
    }
    
    public String getContentType() {

	return type;
    }

    public void setContentType(String type) {

	this.type = type;
    }

    public String getCharacterEncoding() {

	return encoding;
    }

    public void setCharacterEncoding(String encoding) {

	this.encoding = encoding;
    }

    public InputStream getInputStream() throws IOException {

	if(reader != null) return null;
	else if(in == null) 
	    in = new ByteArrayInputStream(content);

	return in;
    }

    public Reader getReader() throws IOException {

	if(in != null) return null;
	else if(reader == null) {
	    reader = 
		((encoding != null)?
		 new InputStreamReader
		 (new ByteArrayInputStream(content), encoding) :
		 new InputStreamReader
		 (new ByteArrayInputStream(content)));
	}
	return reader;
    }

    
}
