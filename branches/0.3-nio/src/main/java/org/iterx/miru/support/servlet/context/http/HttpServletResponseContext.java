/*
  org.iterx.miru.support.servlet.context.context.HttpServletResponseContext

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

package org.iterx.miru.support.servlet.context.http;

import java.io.Writer;
import java.io.OutputStream;
import java.io.IOException;
import java.io.Closeable;

import java.util.Map;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.iterx.miru.context.http.HttpResponseContext;

public final class HttpServletResponseContext
    implements HttpResponseContext, Closeable {

    private HttpServletResponse httpServletResponse;
    private OutputStream outputStream;
    private Writer writer;

    private Map<String, String> properties;

    private int length = -1;
    private int status = OK;


    public HttpServletResponseContext(HttpServletResponse httpResponse) {
	        
        if(httpResponse == null)
            throw new IllegalArgumentException("httpServletResponse == null");

        properties = new HashMap<String, String>();
        this.httpServletResponse = httpResponse;
    }
    
    public int getStatus() {

        return status;
    }

    public void setStatus(int status) {

        httpServletResponse.setStatus(status);
        this.status = status;
    }  

    public String getHeader(String name) {

        return properties.get(name);
    }

    public void setHeader(String name, String value) {

        httpServletResponse.setHeader(name, value);
        properties.put(name, value);
    }


    public HttpServletResponse getHttpServletResponse() {

        return httpServletResponse;
    }

    public String getCharacterEncoding() {

        return httpServletResponse.getCharacterEncoding();
    }
    
    public void setCharacterEncoding(String encoding) {

        httpServletResponse.setCharacterEncoding(encoding);
    }

    public String getContentType() {

        return httpServletResponse.getContentType();
    }

    public void setContentType(String type) {

        httpServletResponse.setContentType(type);
    }

    public int getContentLength() {

        return length;
    }

    public void setContentLength(int length) {
	
        httpServletResponse.setContentLength(length);
        this.length = length;
    }

    public OutputStream getOutputStream() throws IOException {

        try {
            return (outputStream = httpServletResponse.getOutputStream());
        }
        catch(IllegalStateException e) {}
        return null;
    }

    public Writer getWriter() throws IOException {

        try {
            return (writer = httpServletResponse.getWriter());
        }
        catch(IllegalStateException e) {}
        return null;
    }

    public void close() throws IOException {
        IOException exception;

        exception = null;
        if(outputStream != null)
            try { outputStream.close(); } catch(IOException e) { exception = e; }
        if(writer != null)
            try { writer.close(); } catch(IOException e) { exception = e; }
        if(exception != null) throw exception;
    }

}
