/*
  org.iterx.miru.support.servlet.context.context.HttpServletRequestContext

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
import java.io.Closeable;

import javax.servlet.http.HttpServletRequest;

import org.iterx.miru.context.http.HttpRequestContext;

public final class HttpServletRequestContext
    implements HttpRequestContext, Closeable {

    private HttpServletRequest httpServletRequest;
    private InputStream inputStream;
    private Reader reader;
    private URI uri;

    public HttpServletRequestContext(HttpServletRequest httpRequest) {

        if(httpRequest == null)
            throw new IllegalArgumentException("httpServletRequest == null");
        this.httpServletRequest = httpRequest;
    }

    public HttpServletRequest getHttpServletRequest() {

        return httpServletRequest;
    }

    public URI getURI() {

        if(uri == null) {
            try {
                String scheme;
                int port;

                uri = new URI((scheme = httpServletRequest.getScheme()),
                              null,
                              httpServletRequest.getLocalName(),
                              ((((port = httpServletRequest.getLocalPort()) == 80 &&
                                 "context".equals(scheme)) ||
                                                        ((port == 443) &&
                                                         "https".equals(scheme)))? -1 : port),
                              httpServletRequest.getRequestURI(),
                              null,
                              null);
            }
            catch(URISyntaxException e) {}
        }

        return uri;
    }


    public String getHeader(String key) {

        return httpServletRequest.getHeader(key);
    }

    public String getParameter(String name) {

        return httpServletRequest.getParameter(name);
    }

    public String[] getParameterValues(String name) {

        return httpServletRequest.getParameterValues(name);
    }


    public String[] getParameterNames() {
        Set<String> names;

        names = httpServletRequest.getParameterMap().keySet();
        return names.toArray(new String[names.size()]);
    }

    public String getContentType() {

        return httpServletRequest.getContentType();
    }

    public int getContentLength() {

        return httpServletRequest.getContentLength();
    }

    public String getCharacterEncoding() {

        return httpServletRequest.getCharacterEncoding();
    }
    
    public InputStream getInputStream() throws IOException {

        try {
            return (inputStream = httpServletRequest.getInputStream());
        }
        catch(IllegalStateException e) {}
        return null;
    }

    public Reader getReader() throws IOException {
        try {
            return (reader = httpServletRequest.getReader());
        }
        catch(IllegalStateException e) {}
        return null;
    }

    public void close() throws IOException {
        IOException exception;

        exception = null;
        if(inputStream != null)
            try { inputStream.close(); } catch(IOException e) { exception = e;}
        if(reader != null)
            try { reader.close(); } catch(IOException e) { exception = e;}
        if(exception != null) throw exception;
    }

}
