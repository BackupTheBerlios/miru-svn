/*
  org.iterx.miru.servlet.ServletRequestContext

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

package org.iterx.miru.servlet.context;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import java.io.Reader;
import java.io.InputStream;
import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.iterx.miru.context.WebRequestContext;

public final class ServletRequestContext implements WebRequestContext {

    private ServletRequest request;
    private URI uri;

    public ServletRequestContext(ServletRequest request) {

	this.request = request;
    }

    public ServletRequest getServletRequest() {
	
	return request;
    }

    public URI getURI() {

	if(uri == null) {
	    try {
		if(request instanceof HttpServletRequest) {
		    HttpServletRequest httpRequest;
		    String scheme;
		    int port;

		    httpRequest = (HttpServletRequest) request;
		    uri = new URI((scheme = httpRequest.getScheme()),
				  null,
				  httpRequest.getLocalName(),
				  ((((port = httpRequest.getLocalPort()) == 80 &&
				     "http".equals(scheme)) ||
				    ((port == 443) && 
				     "https".equals(scheme)))? -1 : port),
				  httpRequest.getRequestURI(),
				  null,
				  null);
		}
		else {
		    uri = new URI(request.getScheme(),
				  null,
				  request.getLocalName(), 
				  request.getLocalPort(),
				  null,
				  null,
				  null);
		}
	    }
	    catch(URISyntaxException e) {}
	}
	return uri;
    }


    public String getProperty(String key) {

	return ((request instanceof HttpServletRequest)?
		((HttpServletRequest) request).getHeader(key) : null);
    }

    public String getParameter(String name) {

	return request.getParameter(name);
    }

    public String[] getParameterValues(String name) {

	return request.getParameterValues(name);
    }


    public String[] getParameterNames() {
	Set names;

	return ((String[]) 
		((names = (request.getParameterMap()).keySet()).toArray
		 ((Object[]) new String[names.size()])));
    }

    public String getContentType() {

	return request.getContentType();
    }

    public int getContentLength() {

	return request.getContentLength();
    }

    public String getCharacterEncoding() {

	return request.getCharacterEncoding();
    }
    
    public InputStream getInputStream() throws IOException {

	return request.getInputStream();
    }

    public Reader getReader() throws IOException {

	return request.getReader();
    }

}
