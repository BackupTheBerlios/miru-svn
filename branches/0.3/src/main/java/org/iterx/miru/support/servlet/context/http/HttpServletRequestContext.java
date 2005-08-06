/*
  org.iterx.miru.support.servlet.context.http.HttpServletRequestContext

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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.io.InputStream;
import java.io.IOException;
import java.io.Reader;

import javax.servlet.http.HttpServletRequest;

import org.iterx.miru.context.http.HttpRequestContext;

public final class HttpServletRequestContext implements HttpRequestContext {

    private HttpServletRequest httpRequest;
    private URI uri;

    public HttpServletRequestContext(HttpServletRequest httpRequest) {

        if(httpRequest == null)
            throw new IllegalArgumentException("httpRequest == null");
        this.httpRequest = httpRequest;
    }

    public HttpServletRequest getHttpServletRequest() {

        return httpRequest;
    }

    public URI getURI() {

        if(uri == null) {
            try {
                String scheme;
                int port;

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
            catch(URISyntaxException e) {}
        }

        return uri;
    }


    public String getHeader(String key) {

        return httpRequest.getHeader(key);
    }

    public String getParameter(String name) {

        return httpRequest.getParameter(name);
    }

    public String[] getParameterValues(String name) {

        return httpRequest.getParameterValues(name);
    }


    public String[] getParameterNames() {
        Set names;

        return ((String[])
            ((names = (httpRequest.getParameterMap()).keySet()).toArray
                ((Object[]) new String[names.size()])));
    }

    public String getContentType() {

        return httpRequest.getContentType();
    }

    public int getContentLength() {

        return httpRequest.getContentLength();
    }

    public String getCharacterEncoding() {

        return httpRequest.getCharacterEncoding();
    }
    
    public InputStream getInputStream() throws IOException {

        return httpRequest.getInputStream();
    }

    public Reader getReader() throws IOException {

        return httpRequest.getReader();
    }

}
