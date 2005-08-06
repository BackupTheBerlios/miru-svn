/*
  org.iterx.miru.support.servlet.context.http.HttpServletResponseContext

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

import java.util.Map;
import java.util.HashMap;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.iterx.miru.context.http.HttpResponseContext;

public final class HttpServletResponseContext implements HttpResponseContext {

    private HttpServletResponse httpResponse;
    private Map properties;
    private int length, status;

    {
        status = OK;
        length = -1;
    }

    public HttpServletResponseContext(HttpServletResponse httpResponse) {
	        
        if(httpResponse == null)
            throw new IllegalArgumentException("httpResponse == null");

        properties = new HashMap();
        this.httpResponse = httpResponse;
    }
    
    public int getStatus() {

        return status;
    }

    public void setStatus(int status) {

        httpResponse.setStatus(status);
        this.status = status;
    }  

    public String getHeader(String name) {

        return (String) properties.get(name);
    }

    public void setHeader(String name, String value) {

        httpResponse.setHeader(name, value);
        properties.put(name, value);
    }


    public ServletResponse getHttpResponse() {

        return httpResponse;
    }

    public String getCharacterEncoding() {

        return httpResponse.getCharacterEncoding();
    }
    
    public void setCharacterEncoding(String encoding) {

        httpResponse.setCharacterEncoding(encoding);
    }

    public String getContentType() {

        return httpResponse.getContentType();
    }

    public void setContentType(String type) {

        httpResponse.setContentType(type);
    }

    public int getContentLength() {

        return length;
    }

    public void setContentLength(int length) {
	
        httpResponse.setContentLength(length);
        this.length = length;
    }

    public OutputStream getOutputStream() throws IOException {

        return httpResponse.getOutputStream();
    }

    public Writer getWriter() throws IOException {

        return httpResponse.getWriter();
    }

}
