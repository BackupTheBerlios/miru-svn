/*
  org.iterx.miru.servlet.ServletResponseContext

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

package org.iterx.miru.support.servlet.context;

import java.io.Writer;
import java.io.OutputStream;
import java.io.IOException;

import java.util.Map;
import java.util.HashMap;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.iterx.miru.context.WebResponseContext;

public final class ServletResponseContext implements WebResponseContext {

    private Map properties;
    private ServletResponse servletResponse;
    private int length, status;

    {
	status = OK;
	length = -1;
    }
    public ServletResponseContext(ServletResponse servletResponse) {
	        
        if(servletResponse == null) 
            throw new IllegalArgumentException("servletResponse == null");

        properties = new HashMap();
	this.servletResponse = servletResponse;
    }
    
    public int getStatus() {

	return status;
    }

    public void setStatus(int status) {
	
	if(servletResponse instanceof HttpServletResponse) 
	    ((HttpServletResponse) servletResponse).setStatus(status);
	this.status = status;
    }  

    public String getProperty(String name) {

        return (String) properties.get(name);
    }

    public void setProperty(String name, String value) {

        if(servletResponse instanceof HttpServletResponse) 
	    ((HttpServletResponse) servletResponse).setHeader(name, value);
        properties.put(name, value);
    }


    public ServletResponse getServletResponse() {

	return servletResponse;
    }

    public String getCharacterEncoding() {

	return servletResponse.getCharacterEncoding();
    }
    
    public void setCharacterEncoding(String encoding) {

	servletResponse.setCharacterEncoding(encoding);
    }

    public String getContentType() {

	return servletResponse.getContentType();
    }

    public void setContentType(String type) {

	servletResponse.setContentType(type);
    }

    public int getContentLength() {

	return length;
    }

    public void setContentLength(int length) {
	
	servletResponse.setContentLength(length);
	this.length = length;
    }

    public OutputStream getOutputStream() throws IOException {

	return servletResponse.getOutputStream();
    }

    public Writer getWriter() throws IOException {

	return servletResponse.getWriter();
    }

}
