/*
  org.iterx.miru.context.ResponseContextImpl

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

import java.util.Map;
import java.util.HashMap;

import java.io.Writer;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;

import org.iterx.miru.context.ResponseContext;

public class ResponseContextImpl implements ResponseContext {

    protected Map properties;
    protected boolean _mutable;

    private Writer writer;
    private OutputStream out;

    private String encoding, type;
    private int length;
    private int status;

    {
	_mutable = true;
       
	status = OK;
	length = -1;
    }
    protected ResponseContextImpl() {}

    public ResponseContextImpl(OutputStream out) {
	
	this(out, null);
    }

    public ResponseContextImpl(Writer writer) {
	
	this(writer, null);
    }

    public ResponseContextImpl(OutputStream out, String encoding) {

	properties = new HashMap();
	this.out = out;
	this.encoding = encoding;
    }

    public ResponseContextImpl(Writer writer, String encoding) {

	if(encoding == null && 
	   writer instanceof OutputStreamWriter)
	    encoding = ((OutputStreamWriter) writer).getEncoding();

	properties = new HashMap();
	this.writer = writer;
	this.encoding = encoding;
    }    


    public int getStatus() {

	return status;
    }

    public void setStatus(int status) {

	this.status = status;
    }  

    public String getProperty(String name) {

        return (String) properties.get(name);
    }

    public void setProperty(String name, String value) {

        properties.put(name, value);
    }

    public String getCharacterEncoding() {
	
	return encoding;
    }
    
    public void setCharacterEncoding(String encoding) {
	assert (_mutable) : "immutable";

	this.encoding = encoding;
    }
    
    public String getContentType() {

	return type;
    }

    public void setContentType(String type) {
	assert (_mutable) : "immutable";

	this.type = type;
    }

    public int getContentLength() {
	
	return length;
    }

    public void setContentLength(int length) {
	assert (_mutable) : "immutable";
	assert (length >= -1) : "invalid length";

	this.length = length;
    }

    public OutputStream getOutputStream() throws IOException {

	if(_mutable && out != null) {
	    _mutable = false;
	    writer = null;
	}
	return out;
    }

    public Writer getWriter() throws IOException {

	if(_mutable) {
	    if(out != null) 
		writer = ((encoding != null)?
			  new OutputStreamWriter(out, encoding) :
			  new OutputStreamWriter(out));
	    _mutable = false;
	    out = null;
	}
	return writer;
    }
}
